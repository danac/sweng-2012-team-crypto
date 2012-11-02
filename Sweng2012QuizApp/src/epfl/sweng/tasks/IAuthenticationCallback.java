package epfl.sweng.tasks;

/**
 * Provides Callback functions for Authentication
 */
public interface IAuthenticationCallback {
	/**
	 * Function called if the Authentication succeeded
	 */
	void onSuccess(String sessionId);
	/**
	 * Function called if the Authentication failed
	 */
	void onError();
}
