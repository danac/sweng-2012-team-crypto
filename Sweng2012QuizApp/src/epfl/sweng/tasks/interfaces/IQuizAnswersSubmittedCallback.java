package epfl.sweng.tasks.interfaces;

/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizAnswersSubmittedCallback {
	/**
	 * Function called if the quiz answers were submitted successfully
	 */
	void onSubmitSuccess(double score);

	/**
	 * Function called if there was an error submitting the Question
	 */
	void onError();

}
