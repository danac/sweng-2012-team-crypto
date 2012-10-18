package epfl.sweng.tasks;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;

import epfl.sweng.globals.Globals;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * QuizServerTask realization that submits a new Question
 */
public class SubmitQuestion extends QuizServerTask {
    
	/**
	 * Constructor
	 * @param IQuizServerCallback callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public SubmitQuestion(IQuizServerCallback callback) {
		super(callback);
	}
	

	/**
	 * Method submitting the new question
	 * 
	 * @param QuizQuestion question the Question to be submitted
	 * @param String url (optional) an alternative url for the QuizServer submit location
	 */
	@Override
	protected QuizQuestion doInBackground(Object... args) {
    	try {
    		String url;
    		QuizQuestion question = (QuizQuestion) args[0];
    		if (args.length == 1) {
    			url = Globals.SUBMIT_QUESTION_URL;
    		} else {
    			url = (String) args[1];
    		}
    		

    		HttpPost post = new HttpPost(url);
    		post.setEntity(new StringEntity(question.getJSONString()));
    		post.setHeader("Content-type", "application/json");
    		ResponseHandler<String> handler = new BasicResponseHandler();
    		String response = SwengHttpClientFactory.getInstance().execute(post, handler);
    		System.out.println("Response from server: " + response);
    		return new QuizQuestion(response);
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			cancel(true);
		} catch (ClientProtocolException e) {
			System.out.println(e.getMessage());
			System.out.println(e.toString());
			cancel(true);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			cancel(true);
		}
    	
		return null;
	}
	
}
