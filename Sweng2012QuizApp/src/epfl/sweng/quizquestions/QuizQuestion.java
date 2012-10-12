package epfl.sweng.quizquestions;

import epfl.sweng.showquestions.Question;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

/**
 * Simple data structure holding the data retrieved from the web service
 */
public class QuizQuestion {

	private Question question;
	
	/** The constructor for quiz questions received as JSON strings from the Sweng2012QuizApp server, as in homework #1
	* @param json The JSON string received from the Sweng2012QuizApp server, as in homework #1
	*/
	public QuizQuestion(String json) throws JSONException {
	    ...
	}
	
	/** The constructor for quiz questions defined by the user
	* @param text The body of the question, as input by the user
	* @param answers The list of possible answers of the question, as input by the user
	* @param solutionIndex The index identifying the correct answer, as input by the user
	* @param tags The set of tags of the question, as input by the user
	* @param id The id of the question
	* @param owner The owner of the question
	*/
	public QuizQuestion(String text, List<String> answers, int solutionIndex, Set<String> tags, String id, String owner) {
	    ...
	}

}
