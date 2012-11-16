package epfl.sweng.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;
import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;

/**
 * AsyncTask for communication between the App and the Sweng Quiz question server
 */
abstract class MultipleQuizServerTask extends AsyncTask<Object, Void, List<QuizQuestion>> {
    
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public MultipleQuizServerTask() {
	}
	
	/**
	 * Calls back the onError method of the interface defined when creating the task if
	 * the task has experienced an Error
	 */
	@Override
	protected void onCancelled() {
	}
	
	/**
	 * Handle a HTTP request towards quiz server. Always send session id if the user is authenticated
	 * @param request the request
	 * @return the QuizQuestion as received from the server
	 */
	final protected List<QuizQuestion> handleQuizServerRequest(HttpUriRequest request) {
		int statusCode = 0;
		
		try {
			
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
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			HttpResponse response = SwengHttpClientFactory.getInstance().execute(request);
			
			// TODO next line: if unexpected status code: call Log.e instead of Log.i
			Log.i("SERVER", "Replied with status code " + response.getStatusLine().getStatusCode());
			statusCode = response.getStatusLine().getStatusCode();
			String body = responseHandler.handleResponse(response);
			if (body == null) {
				body = "";
			}
			if (Globals.LOG_QUIZSERVER_REQUESTS) {
				Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, "==== Sweng QuizQuestion Server Response ====");
				Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
				for (Header header : response.getAllHeaders()) {
					Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, header.toString());
				}
				Log.i(Globals.LOGTAG_QUIZSERVER_COMMUNICATION, body);
			}
			
			JSONArray responseArray = new JSONArray(body);
			if (responseArray.length()==0) {
				cancel(false);
			}
			List<QuizQuestion> questionsArray = new ArrayList<QuizQuestion>();
			for (int i=0; i<responseArray.length(); i++) {
				questionsArray.add(new QuizQuestion(responseArray.get(i).toString()));
			}
			
			return questionsArray;
		} catch (JSONException e) {
			cancel(false);
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException" + statusCode);
			if (statusCode == Globals.STATUSCODE_NOTFOUND) {
				return new ArrayList<QuizQuestion>();
			}
			cancel(false);
    	} catch (IOException e) {
    		cancel(false);
    	}
		return null;
	}
	
}
