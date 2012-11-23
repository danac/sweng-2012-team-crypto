package epfl.sweng.tasks;

import epfl.sweng.quizzes.Quiz;
/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizReceivedCallback {
	/**
	 * Function called if the Quiz was received successfully
	 */
	void onSuccess(Quiz quizzes);
	
	/**
	 * Function called if there was an error receiving the Quiz
	 */
	void onError();
	
	
}
