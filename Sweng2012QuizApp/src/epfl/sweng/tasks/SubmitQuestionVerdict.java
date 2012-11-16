package epfl.sweng.tasks;


import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * QuizServerTask realization that fetches the rating of a question
 */
public class SubmitQuestionVerdict extends QuizServerTask {
    
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public SubmitQuestionVerdict(final IQuizQuestionReceivedCallback callback) {
		super(new IQuizServerCallback() {
			
			@Override
			public void onSuccess(final QuizQuestion question) {
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
	 * Method submitting the new question verdict
	 * 
	 * @param question the Question containing the new verdict to be submitted
	 */
	@Override
	protected QuizQuestion doInBackground(Object... args) {
		QuizQuestion question = (QuizQuestion) args[0];
		
		HttpPost post = new HttpPost(Globals.QUESTION_BY_ID_URL + question.getId() + "/rating");
		try {
			JSONObject verdictJson = new JSONObject();
			verdictJson.put("verdict", question.getVerdict());
			post.setEntity(new StringEntity(verdictJson.toString()));
		} catch (UnsupportedEncodingException e) {
			cancel(false);
		} catch (JSONException e) {
			cancel(false);
		}
		post.setHeader("Content-type", "application/json");
		
		handleQuizServerRequest(post);
		return question;
	}
}
