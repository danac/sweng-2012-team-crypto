package epfl.sweng.tasks;

import epfl.sweng.quizquestions.QuizQuestion;


import android.os.AsyncTask;

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
	
	
}
