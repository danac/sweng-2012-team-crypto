package epfl.sweng.quizzes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
		List<QuizQuestion> questions = new ArrayList<QuizQuestion>();

		if (json.has("questions")) {
			
			JSONArray questionsJSON = json.getJSONArray("questions");
			for (int i=0; i<questionsJSON.length(); i++) {
				QuizQuestion question = new QuizQuestion();
				JSONObject questionJSON = questionsJSON.getJSONObject(i);
				
				JSONArray answersJSON = questionJSON.getJSONArray("answers");
				List<String> answers = new ArrayList<String>();
				for (int j=0; j<answersJSON.length(); j++) {
					answers.add(answersJSON.getString(j));
				}
				
				question.setQuestion(questionJSON.getString("question"));
				question.setAnswers(answers);
				
				questions.add(question);
			}
		
			setQuestions(questions);
		}
		setId(json.getInt("id"));
		setTitle(json.getString("title"));
	}
	
	/**
	 * Constructor for a quiz received as a JSON string
	 * @param json
	 * @throws JSONException
	 */
	public Quiz(String json) throws JSONException {
		this(new JSONObject(json));
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
		return getTitle();
	}

	/**
	 * Returns the size (number of questions) of the quiz
	 * @return
	 */
	public int size() {
		return mQuestions.size();
	}
	
	public String getChoicesJSON() throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray choicesJSON = new JSONArray();
		
		for (QuizQuestion question : mQuestions) {
			if (question.getAnswerIndex() == -1) {
				choicesJSON.put(JSONObject.NULL);
			} else {				
				choicesJSON.put(question.getAnswerIndex());
			}
		}
		
		json.put("choices", choicesJSON);
		
		return json.toString();
	}
}
