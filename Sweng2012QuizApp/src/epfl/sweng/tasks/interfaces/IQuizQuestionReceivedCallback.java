package epfl.sweng.tasks.interfaces;

import epfl.sweng.quizquestions.QuizQuestion;
/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizQuestionReceivedCallback {
	/**
	 * Function called if the question was received successfully
	 */
	void onQuestionSuccess(QuizQuestion question);
	
	/**
	 * Function called if the question rating was received successfully
	 */
	void onRatingSuccess(QuizQuestion question);
	
	/**
	 * Function called if there was an error receiving the Question
	 */
	void onQuestionError();

	/**
	 * Function called if there was an error receiving the Rating
	 */
	void onRatingError();

	
	
}
