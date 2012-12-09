package epfl.sweng.tasks;


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.interfaces.IQuestionPersonalRatingReloadedCallback;
import epfl.sweng.tasks.interfaces.IQuizServerCallback;

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
			public void onSuccess(HttpResponse response) {
				try {
					question.setVerdict(getJSONObject(response));
					callback.onReloadedSuccess(question);
				} catch (JSONException e) {
					onError();
				} catch (ParseException e) {
					onError();
				} catch (IOException e) {
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
	protected HttpResponse doInBackground(Object... args) {
		return handleQuizServerRequest(
					new HttpGet(Globals.QUESTION_BY_ID_URL + mQuestion.getId() + "/rating"));
	}
	
}
