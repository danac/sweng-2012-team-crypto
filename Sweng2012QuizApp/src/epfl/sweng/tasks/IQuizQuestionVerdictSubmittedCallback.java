package epfl.sweng.tasks;

import epfl.sweng.quizquestions.QuizQuestion;
/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizQuestionVerdictSubmittedCallback {
	/**
	 * Function called if the question verdict was submitted successfully
	 */
	void onSubmitSuccess(QuizQuestion question);

	
	/**
	 * Function called if the question verdict was successfully reloaded
	 */
	void onReloadedSuccess(QuizQuestion question);

	/**
	 * Function called if there was an error submitting the Question
	 */
	void onSubmitError();

	/**
	 * Function called if there was an error retrieving the new rating of the question
	 */
	void onReloadedError();

}
