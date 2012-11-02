package epfl.sweng.authentication;

/**
 * Provides Callback functions for Session Creation
 */
public interface ISessionCreationCallback {
	/**
	 * Function called if the Session is successfully created
	 */
	void onSessionCreateSuccess();
	/**
	 * Function called if the Session could not be created
	 */
	void onSessionCreateError();
}
