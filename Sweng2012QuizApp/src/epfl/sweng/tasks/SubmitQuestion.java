package epfl.sweng.tasks;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import epfl.sweng.globals.Globals;
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
	public SubmitQuestion(final IQuizQuestionSubmittedCallback callback) {
		
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(JSONTokener response) {
				try {
					callback.onSubmitSuccess(new QuizQuestion((JSONObject) response.nextValue()));
				} catch (JSONException e) {
					callback.onError();
				}
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
	}
	

	/**
	 * Method submitting the new question
	 * 
	 * @param QuizQuestion question the Question to be submitted
	 * @param String url (optional) an alternative url for the QuizServer submit location
	 */
	@Override
	protected JSONTokener doInBackground(Object... args) {
		String url = "";
		QuizQuestion question = (QuizQuestion) args[0];
		if (args.length == 1) {
			url = Globals.SUBMIT_QUESTION_URL;
		} else {
			url = (String) args[1];
		}
		
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new StringEntity(question.getJSONString()));
		} catch (UnsupportedEncodingException e) {
			cancel(false);
		} catch (JSONException e) {
			cancel(false);
		}
		post.setHeader("Content-type", "application/json");
		
		return handleQuizServerRequest(post);
	}
	
}
