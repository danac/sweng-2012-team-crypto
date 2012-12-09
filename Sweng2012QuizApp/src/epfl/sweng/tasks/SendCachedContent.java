package epfl.sweng.tasks;

import epfl.sweng.tasks.interfaces.ISendCachedContentCallback;
import epfl.sweng.tasks.interfaces.ISendCachedQuestionsToSubmitCallback;
import epfl.sweng.tasks.interfaces.ISendCachedVerdictsToSubmitCallback;


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
		
		new SendCachedQuestionsToSubmit(new ISendCachedQuestionsToSubmitCallback() {
			
			@Override
			public void onSuccess() {
				new SendCachedVerdictsToSubmit(new ISendCachedVerdictsToSubmitCallback() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						// Here we should fetch all the ratings for the cached questions
						// Then finally update the status to online mode.
					}
					
					@Override
					public void onError() {
						// TODO Auto-generated method stub
						
					}
				}).execute();
			}
			
			@Override
			public void onError() {
				mCallback.onError();
			}
		}).execute();
		
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
