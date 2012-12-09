package epfl.sweng.tasks;


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;

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
			public void onSuccess(HttpResponse response) {
				try {
					question.setVerdictStats(getJSONObject(response));
				} catch (JSONException e) {
					callback.onError();
					return;
				} catch (ParseException e) {
					callback.onError();
					return;
				} catch (IOException e) {
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
	protected HttpResponse doInBackground(Object... args) {
    	return handleQuizServerRequest(new HttpGet(Globals.QUESTION_BY_ID_URL + mQuestion.getId() + "/ratings"));
	}
	
}
