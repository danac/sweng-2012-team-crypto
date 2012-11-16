package epfl.sweng.tasks;


import java.util.List;

import org.apache.http.client.methods.HttpGet;

import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * QuizServerTask realization that fetches a random Question
 */
public class LoadQuestionsByOwner extends MultipleQuizServerTask {
    
	
	/**
	 * Method fetching the random question
	 * @param url (optional) an alternative url for the QuizServer "fetch random question location
	 */
	@Override
	protected List<QuizQuestion> doInBackground(Object... args) {
    	String url = "";
    	String owner = (String) args[0];
    	
    	
		if (args.length == 1) {
			url = Globals.QUESTION_BY_OWNER_URL;
		} else {
			url = (String) args[1];
		}
		
		List<QuizQuestion> result = handleQuizServerRequest(new HttpGet(url+owner));
		
		for (QuizQuestion question : result) {
			if (!isCancelled() && !question.getOwner().equals(owner)) {
				cancel(false);
			}
		}
		
		return result;
	}
	
}
