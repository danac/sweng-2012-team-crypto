package epfl.sweng.tasks.interfaces;

import org.json.JSONTokener;

/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizServerCallback {
	/**
	 * Function called if the question was received successfully
	 */
	void onSuccess(JSONTokener json);
	/**
	 * Function called if there was an error
	 */
	void onError();

}
