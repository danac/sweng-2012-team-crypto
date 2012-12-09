package epfl.sweng.tasks;

import epfl.sweng.tasks.interfaces.ISendCachedContentCallback;


import android.os.AsyncTask;

/**
 * AsyncTask for sending the cached content when going from offline mode to online mode
 */
public class SendCachedContent extends AsyncTask<String, Void, String> {
    
	
	private ISendCachedContentCallback mCallback;
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public SendCachedContent(ISendCachedContentCallback callback) {
		mCallback = callback;
	}


	/**
	 * The main process of the AsyncTask, does communication with the quiz server
	 */
	@Override
	protected String doInBackground(String... argv) {
		
		
		return "";
	}
	
	/**
	 * When sending was successful
	 */
	@Override
	protected void onPostExecute(String sessionId) {
		mCallback.onSuccess();
	}
	
	/**
	 * Handle a sending error
	 */
	@Override
	protected void onCancelled() {
		mCallback.onError();
	}
}
