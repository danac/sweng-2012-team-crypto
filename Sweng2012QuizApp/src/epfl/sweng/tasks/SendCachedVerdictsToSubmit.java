package epfl.sweng.tasks;

import java.util.List;

import epfl.sweng.cache.CacheManager;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.interfaces.IQuizQuestionSubmittedCallback;
import epfl.sweng.tasks.interfaces.ISendCachedContentCallback;
import epfl.sweng.tasks.interfaces.ISendCachedQuestionsToSubmitCallback;
import epfl.sweng.tasks.interfaces.ISendCachedVerdictsToSubmitCallback;


import android.os.AsyncTask;

/**
 * AsyncTask for sending the cached questions to submit
 */
public class SendCachedVerdictsToSubmit extends AsyncTask<String, Void, String> {
    
	
	private ISendCachedVerdictsToSubmitCallback mCallback;
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public SendCachedVerdictsToSubmit(ISendCachedVerdictsToSubmitCallback callback) {
		mCallback = callback;
	}


	/**
	 * The main process of the AsyncTask, does communication with the quiz server
	 */
	@Override
	protected String doInBackground(String... argv) {
		
		List<QuizQuestion> cachedVerdictsToSubmit = CacheManager.getInstance().getCachedVerdictsToSubmit();
		
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
