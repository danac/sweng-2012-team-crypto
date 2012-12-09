package epfl.sweng.tasks.interfaces;

import org.apache.http.HttpResponse;

/**
 * Provides Callback functions for Server Communication Response
 */
public interface IQuizServerCallback {
	/**
	 * Function called if the question was received successfully
	 */
	void onSuccess(HttpResponse json);
	/**
	 * Function called if there was an error
	 */
	void onError();

}
