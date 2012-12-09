package epfl.sweng.tasks;

import java.util.List;

import epfl.sweng.cache.CacheManager;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.interfaces.IQuizQuestionSubmittedCallback;
import epfl.sweng.tasks.interfaces.ISendCachedQuestionsToSubmitCallback;


import android.os.AsyncTask;

/**
 * AsyncTask for sending the cached questions to submit
 */
public class SendCachedQuestionsToSubmit extends AsyncTask<String, Void, String> {
    
	
	private ISendCachedQuestionsToSubmitCallback mCallback;
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public SendCachedQuestionsToSubmit(ISendCachedQuestionsToSubmitCallback callback) {
		mCallback = callback;
	}


	/**
	 * The main process of the AsyncTask, does communication with the quiz server
	 */
	@Override
	protected String doInBackground(String... argv) {
		
		List<QuizQuestion> cachedQuestionsToSubmit = CacheManager.getInstance().getCachedQuestionsToSubmit();
		
		for (final QuizQuestion question : cachedQuestionsToSubmit) {
			new SubmitQuestion(new IQuizQuestionSubmittedCallback() {
				
				@Override
				public void onSubmitSuccess(QuizQuestion q) {
					CacheManager.getInstance().removeSubmittedQuestion(question);
				}
				
				@Override
				public void onError() {
					mCallback.onError();					
				}
			}).execute(question);
		}
		
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
