package epfl.sweng.tasks.interfaces;

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
	 * @param mExcept 
	 */
	void onError(Exception mExcept);
}
