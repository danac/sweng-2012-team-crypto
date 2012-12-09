package epfl.sweng.tasks;



import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import epfl.sweng.globals.Globals;
import epfl.sweng.quizzes.Quiz;
import epfl.sweng.tasks.interfaces.IQuizReceivedCallback;
import epfl.sweng.tasks.interfaces.IQuizServerCallback;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class LoadQuiz extends QuizServerTask {
	
	private int mQuizId;
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public LoadQuiz(final IQuizReceivedCallback callback, int quizId) {
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(HttpResponse response) {
				Quiz quiz = new Quiz();
				try {
					quiz = new Quiz(getJSONObject(response));
				} catch (JSONException e) {
					onError();
					return;
				} catch (ClassCastException e) {
					onError();
					return;
				} catch (ParseException e) {
					onError();
					return;
				} catch (IOException e) {
					onError();
					return;
				} 
				callback.onSuccess(quiz);
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
		mQuizId = quizId;
	}
	
	/**
	 * Method fetching the random question
	 * @param url (optional) an alternative url for the QuizServer "fetch random question location
	 */
	@Override
	protected HttpResponse doInBackground(Object... urls) {
    	String url = "";
    	
		if (urls.length == 0) {
			url = Globals.QUIZ_BY_ID_URL + mQuizId;
		} else {
			url = (String) urls[0];
		}
		
		return handleQuizServerRequest(new HttpGet(url));
	}
	
}
