package epfl.sweng.quizzes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.quizquestions.QuizQuestion;

/**
 * Data structure holding a quiz received from the server
 */
public class Quiz {

	private List<QuizQuestion> mQuestions;
	private int mId;
	private String mTitle;

	/**
	 * Constructor that sets default values to the parameters
	 */
	public Quiz() {
		mQuestions = new ArrayList<QuizQuestion>();
		mId = -1;
		mTitle = "";
	}
	
	/**
	 * Constructor for a quiz received as a JSON Object from the Sweng2012QuizApp server
	 * @param json
	 * @throws JSONException
	 */
	public Quiz(JSONObject json) throws JSONException {
		mTitle = json.getString("title");
		mId = json.getInt("id");
	}
	
	// Setters
	public void setQuestions(List<QuizQuestion> questions) {
		mQuestions = questions;
	}
	
	public void setId(int id) {
		mId = id;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	// Getters
	public List<QuizQuestion> getQuestions() {
		return mQuestions;
	}
	
	public int getId() {
		return mId;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String toString() {
		return mTitle;
	}
}
