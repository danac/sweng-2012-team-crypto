package epfl.sweng.tasks;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizzes.Quiz;
import epfl.sweng.tasks.interfaces.IQuizAnswersSubmittedCallback;
import epfl.sweng.tasks.interfaces.IQuizServerCallback;

/**
 * QuizServerTask realization that submits a new Question
 */
public class SubmitQuizAnswers extends QuizServerTask {
    
	
	private Quiz mQuiz;


	/**
	 * Constructor
	 * @param IQuizServerCallback callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public SubmitQuizAnswers(final IQuizAnswersSubmittedCallback callback, final Quiz quiz) {
		
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(JSONTokener response) {
				try {
					JSONObject responseJSON = (JSONObject) response.nextValue();
					double score = responseJSON.getDouble("score");
					callback.onSubmitSuccess(score);
				} catch (JSONException e) {
					callback.onError();
				}
			}
			
			@Override
			public void onError() {
				callback.onError();
			}
		});
		mQuiz = quiz;
	}
	

	/**
	 * Method submitting the new question
	 * 
	 * @param QuizQuestion question the Question to be submitted
	 * @param String url (optional) an alternative url for the QuizServer submit location
	 */
	@Override
	protected JSONTokener doInBackground(Object... args) {
		String url = Globals.SUBMIT_QUIZ_ANSWERS_URL;

		HttpPost post = new HttpPost(String.format(url, mQuiz.getId()));
		try {
			post.setEntity(new StringEntity(mQuiz.getChoicesJSON()));
		} catch (UnsupportedEncodingException e) {
			cancel(false);
			return null;
		} catch (JSONException e) {
			cancel(false);
			return null;
		}
		post.setHeader("Content-type", "application/json");
		
		return handleQuizServerRequest(post);
	}
	
}
