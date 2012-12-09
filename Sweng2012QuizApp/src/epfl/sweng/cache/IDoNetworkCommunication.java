package epfl.sweng.cache;

/**
 * Provides Callback functions for Network Communication
 */
public interface IDoNetworkCommunication {
	/**
	 * Function called if the Network Communication was successful
	 */
	void onSuccess();
	/**
	 * Function called if the Network Communication failed
	 */
	void onError();
}
