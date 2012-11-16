package epfl.sweng.tasks;


import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;

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
	 * Method updating the rating
	 * @param question the Question to update
	 */
	@Override
	protected QuizQuestion doInBackground(Object... args) {
    	QuizQuestion question = (QuizQuestion) args[0];

		
		try {
			question.setVerdictStats(handleQuizServerRequest(
					new HttpGet(Globals.QUESTION_BY_ID_URL + question.getId() + "/ratings")));
			question.setVerdict(handleQuizServerRequest(
					new HttpGet(Globals.QUESTION_BY_ID_URL + question.getId() + "/rating")));
			return question;
		} catch (JSONException e) {
			cancel(false);
			return question;
		}		
	}
	
}
