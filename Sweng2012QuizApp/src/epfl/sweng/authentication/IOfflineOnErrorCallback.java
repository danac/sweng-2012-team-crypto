package epfl.sweng.authentication;

/**
 * Provides Callback functions for Session Creation
 */
public interface IOfflineOnErrorCallback {
	/**
	 * Called if session went offline because of server error
	 */
	void onSessionWentOffline();
}
