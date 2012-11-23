package epfl.sweng.tasks;


import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class ReloadPersonalRating extends QuizServerTask {
    
	private QuizQuestion mQuestion;
	
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public ReloadPersonalRating(final IQuestionPersonalRatingReloadedCallback callback, final QuizQuestion question) {
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(JSONTokener response) {
				try {
					question.setVerdict((JSONObject) response.nextValue());
					callback.onReloadedSuccess(question);
				} catch (JSONException e) {
					onError();
				}
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
		mQuestion = question;
	}
	
	/**
	 * Method updating the personal rating
	 * @param question the Question to update
	 */
	@Override
	protected JSONTokener doInBackground(Object... args) {
		return handleQuizServerRequest(
					new HttpGet(Globals.QUESTION_BY_ID_URL + mQuestion.getId() + "/rating"));
	}
	
}
