package epfl.sweng.tasks;

import java.util.List;

import epfl.sweng.quizzes.Quiz;
/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizzesReceivedCallback {
	/**
	 * Function called if the quizzes were received successfully
	 */
	void onSuccess(List<Quiz> quizzes);
	
	/**
	 * Function called if there was an error receiving the Quizzes
	 */
	void onError();
	
	
}
