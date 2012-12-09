package epfl.sweng.tasks.interfaces;

/**
 * Provides Callback functions for sending cached questions to the server
 */
public interface ISendCachedQuestionsToSubmitCallback {
	/**
	 * Function called if the sending succeeded
	 */
	void onSuccess();
	/**
	 * Function called if the sending failed
	 */
	void onError();
}
