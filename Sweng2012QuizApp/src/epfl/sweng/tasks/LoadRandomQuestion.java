package epfl.sweng.tasks;


import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class LoadRandomQuestion extends QuizServerTask {
    
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public LoadRandomQuestion(final IQuizQuestionReceivedCallback callback) {
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(QuizQuestion question) {
				callback.onSuccess(question);
				
				new UpdateQuestionRating(new IQuizServerCallback() {
					
					@Override
					public void onSuccess(QuizQuestion question) {
						callback.onSuccess(question);
					}
					
					@Override
					public void onError() {
						callback.onRatingError();
					}
				}).execute(question);
			}
			
			@Override
			public void onError() {
				callback.onQuestionError();
			}
		});
	}
	
	/**
	 * Method fetching the random question
	 * @param url (optional) an alternative url for the QuizServer "fetch random question location
	 */
	@Override
	protected QuizQuestion doInBackground(Object... urls) {
    	String url = "";
		if (urls.length == 0) {
			url = Globals.RANDOM_QUESTION_URL;
		} else {
			url = (String) urls[0];
		}
		
		try {
			JSONObject responseJSON = handleQuizServerRequest(new HttpGet(url));
			QuizQuestion question = new QuizQuestion();
			if (responseJSON != null) {
				question = new QuizQuestion(responseJSON);
			} else {
				question = null;
				cancel(false);
			}
			return question;
		} catch (JSONException e) {
			cancel(false);
			return null;
		}		
	}
	
}
