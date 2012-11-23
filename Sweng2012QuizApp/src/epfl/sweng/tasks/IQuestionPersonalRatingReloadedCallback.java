package epfl.sweng.tasks;

import epfl.sweng.quizquestions.QuizQuestion;
/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuestionPersonalRatingReloadedCallback {
	/**
	 * Function called if the question rating was successfully fetched
	 */
	void onReloadedSuccess(QuizQuestion question);

	/**
	 * Function called if there was an error submitting the Question
	 */
	void onError();

}
