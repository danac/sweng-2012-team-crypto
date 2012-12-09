package epfl.sweng.tasks;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.authentication.SessionManager;
import epfl.sweng.globals.Globals;
import epfl.sweng.servercomm.CachedServerCommunication;
import epfl.sweng.servercomm.ContentHelper;
import epfl.sweng.tasks.interfaces.IQuizServerCallback;


import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask for communication between the App and the Sweng Quiz question server
 */
abstract class QuizServerTask extends AsyncTask<Object, Void, HttpResponse> {
    
	/**
	 * Local Variable holding the callback interface passed through the constructor 
	 */
	private IQuizServerCallback mCallback;
	
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public QuizServerTask(IQuizServerCallback callback) {
		mCallback = callback;
	}
	
	/**
	 * Calls back the onSuccess method of the interface defined when creating the task
	 * @param question the question returned by the server
	 */
	@Override
	protected void onPostExecute(HttpResponse response) {
		mCallback.onSuccess(response);
	}

	/**
	 * Calls back the onError method of the interface defined when creating the task if
	 * the task has experienced an Error
	 */
	@Override
	protected void onCancelled() {
		mCallback.onError();
	}
	
	/**
	 * Handle a HTTP request towards quiz server. Always send session id if the user is authenticated
	 * @param request the request
	 * @return the JSONObject as received from the server
	 */
	final public HttpResponse handleQuizServerRequest(HttpUriRequest request) {
		try {
			if (SessionManager.getInstance().isAuthenticated()) {
				request.addHeader("Authorization", "Tequila " + SessionManager.getInstance().getSessionId());
			}
			
			if (Globals.LOG_QUIZSERVER_REQUESTS) {				
				Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, "==== Sweng QuizQuestion Server Request ====");
				Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, request.getRequestLine().toString());
				for (Header header : request.getAllHeaders()) {
					Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, header.toString());
				}
				
				if (request instanceof HttpPost) {
					Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, 
							EntityUtils.toString(((HttpPost) request).getEntity()));
				}
			}
			
			HttpResponse response = CachedServerCommunication.getInstance().execute(request);
			
			Log.i("SERVER", "Replied with status code " + getStatusCode(response));
			if (Globals.LOG_QUIZSERVER_REQUESTS) {
				Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, "==== Sweng QuizQuestion Server Response ====");
				Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
				for (Header header : response.getAllHeaders()) {
					Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, header.toString());
				}
				Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, ContentHelper.getResponseContent(response));
			}
			
			return response;
    	} catch (ClientProtocolException e) {
    		cancel(false);
    	} catch (IOException e) {
    		cancel(false);
    	}
		return null;
	}
	
	public static int getStatusCode(HttpResponse response) {
		return response.getStatusLine().getStatusCode();
	}
	

	protected static JSONObject getJSONObject(HttpResponse response) throws ParseException, JSONException, IOException {
		String content = ContentHelper.getResponseContent(response);
		if (content.equals("")) {
			content = "{}";
		}
		return new JSONObject(content);
	}
	
	protected static JSONArray getJSONArray(HttpResponse response) throws ParseException, JSONException, IOException {
		return new JSONArray(ContentHelper.getResponseContent(response));
	}	

}
