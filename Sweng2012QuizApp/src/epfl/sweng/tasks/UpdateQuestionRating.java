package epfl.sweng.tasks;


import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class UpdateQuestionRating extends QuizServerTask {
    
	
	/**
	 * Constructor
	 * @param callback interface defining the methods to be called
	 * for the outcomes of success (onSuccess) or error (onError)
	 */
	public UpdateQuestionRating(IQuizServerCallback callback) {
		super(callback);
	}
	
	/**
	 * Method updating question the rating
	 * @param question the Question to update
	 */
	@Override
	protected QuizQuestion doInBackground(Object... args) {
    	QuizQuestion question = (QuizQuestion) args[0];

		
		try {
			JSONObject json = handleQuizServerRequest(
					new HttpGet(Globals.QUESTION_BY_ID_URL + question.getId() + "/ratings"));
			if (!isCancelled()) {
				question.setVerdictStats(json);
			}
			return question;
		} catch (JSONException e) {
			cancel(false);
			return question;
		}		
	}
	
}
