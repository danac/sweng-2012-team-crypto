package epfl.sweng.tasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class LoadRandomQuestion extends QuizServerTask {
    
	
	/**
	 * Constructor
	 * @param IQuizServerCallback callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public LoadRandomQuestion(IQuizServerCallback callback) {
		super(callback);
	}
	
	/**
	 * Method fetching the random question
	 * @param String url (optional) an alternative url for the QuizServer "fetch random question location
	 */
	@Override
	protected QuizQuestion doInBackground(Object... urls) {
    	String url = "";
    	try {
    		if (urls.length == 0) {
    			url = Globals.RANDOM_QUESTION_URL;
    		} else {
    			url = (String) urls[0];
    		}
    		HttpGet request = new HttpGet(url);
    		ResponseHandler<String> response = new BasicResponseHandler();
    		String responseText = SwengHttpClientFactory.getInstance().execute(request, response);
    		
			return new QuizQuestion(responseText);
			
    	} catch (JSONException e) {
    		cancel(true);
    	} catch (ClientProtocolException e) {
    		cancel(true);
    	} catch (IOException e) {
    		cancel(true);
    	}
    	
		return null;
	}
	
}
