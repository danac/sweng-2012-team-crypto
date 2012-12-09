package epfl.sweng.tasks.interfaces;

/**
 * Provides Callback functions for sending cached content to the server
 */
public interface ISendCachedContentCallback {
	/**
	 * Function called if the sending succeeded
	 */
	void onSuccess();
	/**
	 * Function called if the sending failed
	 */
	void onError();
}
