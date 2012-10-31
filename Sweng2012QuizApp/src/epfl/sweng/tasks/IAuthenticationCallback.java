package epfl.sweng.tasks;

/**
 * Provides Callback functions for Authentication
 */
public interface IAuthenticationCallback {
	void onSuccess(String sessionId);
	void onError();
}
