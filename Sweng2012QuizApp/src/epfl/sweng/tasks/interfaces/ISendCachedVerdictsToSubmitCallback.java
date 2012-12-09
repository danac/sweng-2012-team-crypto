package epfl.sweng.tasks.interfaces;

/**
 * Provides Callback functions for sending cached verdicts to the server
 */
public interface ISendCachedVerdictsToSubmitCallback {
	/**
	 * Function called if the sending succeeded
	 */
	void onSuccess();
	/**
	 * Function called if the sending failed
	 */
	void onError();
}
