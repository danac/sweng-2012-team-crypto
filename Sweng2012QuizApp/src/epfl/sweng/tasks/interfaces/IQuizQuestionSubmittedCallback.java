package epfl.sweng.tasks.interfaces;

import epfl.sweng.quizquestions.QuizQuestion;
/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizQuestionSubmittedCallback {
	/**
	 * Function called if the question was submitted successfully
	 */
	void onSubmitSuccess(QuizQuestion question);

	/**
	 * Function called if there was an error submitting the Question
	 */
	void onError();

}
