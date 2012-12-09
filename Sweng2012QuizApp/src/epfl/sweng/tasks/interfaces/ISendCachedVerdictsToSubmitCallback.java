package epfl.sweng.tasks.interfaces;

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
