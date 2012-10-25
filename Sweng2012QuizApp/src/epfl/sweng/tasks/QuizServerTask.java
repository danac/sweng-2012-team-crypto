package epfl.sweng.tasks;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;


import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask for communication between the App and the Sweng Quiz question server
 */
abstract class QuizServerTask extends AsyncTask<Object, Void, QuizQuestion> {
    
	/**
	 * Local Variable holding the callback interface passed through the constructor 
	 */
	private IQuizServerCallback mCallback;
	
	/**
	 * Constructor
	 * @param IQuizServerCallback callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public QuizServerTask(IQuizServerCallback callback) {
		mCallback = callback;
	}
	
	/**
	 * Calls back the onSuccess method of the interface defined when creating the task
	 * @param QuizQuestion question the question returned by the server
	 */
	@Override
	protected void onPostExecute(QuizQuestion question) {
		mCallback.onSuccess(question);
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
	 */
	final protected QuizQuestion handleQuizServerRequest(HttpUriRequest request) {
		try {
			if (Globals.LOG_QUESTIONSERVER_REQUESTS) {				
				Log.i("SERVER", "==== Sweng QuizQuestion Server Request ====");
				Log.i("SERVER", request.getRequestLine().toString());
				for (Header header : request.getAllHeaders()) {
					Log.i("SERVER", header.toString());
				}
				
				if (request instanceof HttpPost) {
					Log.i("SERVER", EntityUtils.toString(((HttpPost) request).getEntity()));
				}
			}
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			HttpResponse response = SwengHttpClientFactory.getInstance().execute(request);
			String body = responseHandler.handleResponse(response);
			
			if (Globals.LOG_QUESTIONSERVER_REQUESTS) {
				Log.i("SERVER", "==== Sweng QuizQuestion Server Response ====");
				Log.i("SERVER", response.getStatusLine().getStatusCode() + " "
						+ response.getStatusLine().getReasonPhrase());
				for (Header header : response.getAllHeaders()) {
					Log.i("SERVER", header.toString());
				}
				Log.i("SERVER", body);
			}
			
			return new QuizQuestion(body);
    	} catch (JSONException e) {
    		cancel(false);
    	} catch (ClientProtocolException e) {
    		cancel(false);
    	} catch (IOException e) {
    		cancel(false);
    	}
		return null;
	}
}
