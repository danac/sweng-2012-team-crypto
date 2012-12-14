package epfl.sweng.authentication;

/**
 * Provides Callback functions for Session Creation
 */
public interface IOfflineStateChangedListener {
	/**
	 * Called if session offline state changed
	 */
	void onOfflineStateChanged(boolean newOfflineState);
}
