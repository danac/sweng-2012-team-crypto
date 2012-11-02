package epfl.sweng.tasks;

import epfl.sweng.quizquestions.QuizQuestion;
/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizServerCallback {
	/**
	 * Function called if the question was received successfully
	 */
	void onSuccess(QuizQuestion question);
	/**
	 * Function called if there was an error
	 */
	void onError();
}
