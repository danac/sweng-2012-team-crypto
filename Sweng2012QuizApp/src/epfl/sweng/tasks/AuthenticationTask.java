package epfl.sweng.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.globals.Globals;
import epfl.sweng.servercomm.SwengHttpClientFactory;


import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask for authentication towards the Authentication Server
 * @author Cyril Misev <cyril.misev@epfl.ch>
 */
public class AuthenticationTask extends AsyncTask<String, Void, String> {
    
	
	private int mLastStatusCode=0;
	private IAuthenticationCallback mCallback;
	
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public AuthenticationTask(IAuthenticationCallback callback) {
		mCallback = callback;
	}

	/**
	 * Handle a HTTP request, store status code in mLastStatusCode class variable
	 * @param request the request
	 * @return the body of the response
	 */
	private String handleServerRequest(HttpUriRequest request) {
		mLastStatusCode = 0;
		
		try {
			if (Globals.LOG_AUTH_REQUESTS) {				
				Log.i(Globals.LOGTAG_AUTH_COMMUNICATION, "==== Sweng Authentication Request ====");
				Log.i(Globals.LOGTAG_AUTH_COMMUNICATION, request.getRequestLine().toString());
				for (Header header : request.getAllHeaders()) {
					Log.i(Globals.LOGTAG_AUTH_COMMUNICATION, header.toString());
				}
				
			}
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			HttpResponse response = SwengHttpClientFactory.getInstance().execute(request);
			
			Log.i("SERVER", "Replied with status code " + response.getStatusLine().getStatusCode());
			mLastStatusCode = response.getStatusLine().getStatusCode();
			String body = responseHandler.handleResponse(response);
			if (Globals.LOG_AUTH_REQUESTS) {
				Log.i(Globals.LOGTAG_AUTH_COMMUNICATION, "==== Sweng Authentication Response ====");
				Log.i(Globals.LOGTAG_AUTH_COMMUNICATION, response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
				for (Header header : response.getAllHeaders()) {
					Log.i(Globals.LOGTAG_AUTH_COMMUNICATION, header.toString());
				}
				Log.i(Globals.LOGTAG_AUTH_COMMUNICATION, body);
			}
			
			return body;
    	} catch (ClientProtocolException e) {
    		Log.i(Globals.LOGTAG_AUTH_COMMUNICATION, e.getMessage().toString());
    	} catch (IOException e) {
    		cancel(false);
    	}
		return "";
	}

	/**
	 * The main process of the AsyncTask, does communication with the authentication server and the quiz server
	 * @param username user name to authenticate
	 * @param password the password of the user to authenticate
	 * @return the session id as received from the quiz server
	 */
	@Override
	protected String doInBackground(String... argv) {
		
		String authToken = "";
		String username = "";
		String password = "";
		
		username = argv[0];
		password = argv[1];
		
	
		
		// STEP 1,2: Get Token
		JSONObject authTokenJson = new JSONObject();
		try {
			authTokenJson = new JSONObject(handleServerRequest(new HttpGet(Globals.QUIZSERVER_LOGIN_URL)));
				
			if (!isCancelled()) {
				authToken = authTokenJson.getString("token");				
			}
		} catch (JSONException e) {
			cancel(false);
		}
		
		// STEP 3,4: Authenticate using tequila
		if (!isCancelled()) {
			
			HttpPost authRequest = new HttpPost(Globals.AUTHSERVER_LOGIN_URL);
			List<NameValuePair> authParams = new ArrayList<NameValuePair>();
			authParams.add(new BasicNameValuePair("requestkey", authToken));
			authParams.add(new BasicNameValuePair("username", username));
			authParams.add(new BasicNameValuePair("password", password));
			try {
				authRequest.setEntity(new UrlEncodedFormEntity(authParams));
			} catch (UnsupportedEncodingException e) {
				cancel(false);
			}
			
			handleServerRequest(authRequest);
			if (mLastStatusCode!=Globals.STATUSCODE_AUTHSUCCESSFUL) {
				cancel(false);
			}
		}
		// STEP 5,6: Get Session Id
		if (!isCancelled()) {
			authTokenJson.remove("message");
			
			HttpPost sessionRequest = new HttpPost(Globals.QUIZSERVER_LOGIN_URL);
			try {
				sessionRequest.setEntity(new StringEntity(authTokenJson.toString()));
			} catch (UnsupportedEncodingException e) {
				cancel(false);
			}
			if (!isCancelled()) {
				sessionRequest.setHeader("Content-type", "application/json");
				try {
					JSONObject sessionJson = new JSONObject(handleServerRequest(sessionRequest));
					return sessionJson.getString("session");
				} catch (JSONException e) {
					cancel(false);
				}
			}
		}
		
		
		return "";
	}
	
	/**
	 * Handle the received session id if the authentication was successful
	 */
	@Override
	protected void onPostExecute(String sessionId) {
		mCallback.onSuccess(sessionId);
	}
	
	/**
	 * Handle a authentication error
	 */
	@Override
	protected void onCancelled() {
		mCallback.onError();
	}
}
