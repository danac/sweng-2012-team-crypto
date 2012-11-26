package epfl.sweng.tasks;


import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.interfaces.IQuestionRatingReloadedCallback;
import epfl.sweng.tasks.interfaces.IQuizServerCallback;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class ReloadQuestionRating extends QuizServerTask {
    
	private QuizQuestion mQuestion;
	
	public ReloadQuestionRating(final IQuestionRatingReloadedCallback callback, final QuizQuestion question) {
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(JSONTokener response) {
				try {
					question.setVerdictStats((JSONObject) response.nextValue());
				} catch (ClassCastException e) {
					callback.onError();
					return;
				} catch (JSONException e) {
					callback.onError();
					return;
				}
				callback.onReloadedSuccess(question);
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
		mQuestion = question;
	}
	
	/**
	 * Method updating question the rating
	 * @param question the Question to update
	 */
	@Override
	protected JSONTokener doInBackground(Object... args) {
    	return handleQuizServerRequest(new HttpGet(Globals.QUESTION_BY_ID_URL + mQuestion.getId() + "/ratings"));
	}
	
}
