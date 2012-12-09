package epfl.sweng.quizquestions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Simple data structure holding the data retrieved from the web service
 */
public class QuizQuestion {

	private final static int MAX_NUMBER_OF_ANSWERS = 10;
	private final static int MIN_NUMBER_OF_ANSWERS = 2;
	private final static int MAX_LENGTH_OF_TAGS = 20;
	private final static int MAX_LENGTH_OF_STRINGS = 500;
    private String mQuestion;
    private List<String> mAnswers;
    private int mSolutionIndex;
    private int mAnswerIndex = -1;
    private Set<String> mTags;
    private String mOwner;
    private int mId;
    
    private int mLikeCount = 0;
    private int mDislikeCount = 0;
    private int mIncorrectCount =0;
    private String mVerdict = "";
    
    
    
    /**
     * Possible QuizQuestion Parameters
     */
    public enum QuizQuestionParam {
    	QUESTION, ANSWER, SOLUTION_INDEX, TAGS, OWNER, ID
    }

	/** The constructor for quiz questions defined by the user
	* @param text The body of the question, as input by the user
	* @param answers The list of possible answers of the question, as input by the user
	* @param solutionIndex The index identifying the correct answer, as input by the user
	* @param tags The set of tags of the question, as input by the user
	* @param id The id of the question
	* @param owner The owner of the question
	*/
	public QuizQuestion(String txt, List<String> answers, int solutionIdx, Set<String> tags, int id, String owner) {
		setQuestion(txt);
		setId(id);
		setSolutionIndex(solutionIdx);
		setAnswers(answers);
		setTags(tags);
		setOwner(owner);
	}
	
    /**
     * Constructor setting default values for the question and the answers
     */
    public QuizQuestion() {
        //super();  //Is this instruction useful? /Manu
        mQuestion = "";
        mAnswers = new ArrayList<String>();
        mAnswers.add("");
        mTags = new HashSet<String>();
        mSolutionIndex = -1;
        mOwner = "anonymous";
        mId = 1;
    	
    }
	
	/** 
	 * The constructor for a quiz questions received as a JSON strings.
     * @param json The JSON string received from the Sweng2012QuizApp server
     */
	public QuizQuestion(String json) throws JSONException {
		this(new JSONObject(json));
	}

	/** 
	 * The constructor for a quiz questions received as a JSON Object.
     * @param json The JSON string received from the Sweng2012QuizApp server
     */
	public QuizQuestion(JSONObject json) throws JSONException {
		
		JSONArray answersJSON = json.getJSONArray("answers");
		List<String> answers = new ArrayList<String>();
		for (int i=0; i<answersJSON.length(); i++) {
			answers.add(answersJSON.getString(i));
		}

		JSONArray tagsJSON = json.getJSONArray("tags");
		Set<String> tags = new HashSet<String>();
		for (int i=0; i<tagsJSON.length(); i++) {
			tags.add(tagsJSON.getString(i));
		}
		
		setQuestion(json.getString("question"));
		setId(json.getString("id"));
		setSolutionIndex(json.getInt("solutionIndex"));
		setAnswers(answers);
		setTags(tags);
		setOwner(json.getString("owner"));
	}
	
	/** Returns the number of rep invariant violations*/
	public int auditErrors(int depth) {
		
		int errorCount = 0;

		//Check that no string is larger than 500 characters nor empty or only whitespaces
		if (!checkString(mQuestion)) {
			errorCount++;
		}
		Iterator<String> iter1 = mAnswers.iterator();
		while (iter1.hasNext()) {
	    	String answer = iter1.next();
	    	if (!checkString(answer)) {
	    		errorCount++;
	    	}
	    }
		Iterator<String> iter2 = mTags.iterator();
		while (iter2.hasNext()) {
	    	String tag = iter2.next();
	    	if (!checkString(tag)) {
	    		errorCount++;
	    	}
	    }
		if (!checkString(mOwner)) {
			errorCount++;
		}
		
//		NOT NEEDED ANYMORE SINCE ID IS AN INT /DANA		
//		if (!checkString(mId)) {
//			errorCount++;
//		}
		
		
		// Check the number of answers
		if (!checkNbAnswers()) {
			errorCount++;
		}
		
		// Check the tags
		Iterator<String> iter3 = mTags.iterator();
	    while (iter3.hasNext()) {
	    	String tag = iter3.next();
	    	if (!checkTag(tag)) {
	    		errorCount++;
	    	}
	    }
		

		// Check the index of the right answer
	    if (mSolutionIndex>=mAnswers.size() || mSolutionIndex<0) {
	    	errorCount++;
	    }
	    
	    // Check owner
    	boolean ownerOK = true;
	    for (int i = 0; i < mOwner.length(); i++) {
    	    if (!Character.isLetterOrDigit(mOwner.charAt(i))) {
    	    	ownerOK = false;
    	    	break;
    	    }
    	}
	    
	    if (!ownerOK) {
    		errorCount++;
    	}
		
	    // Check the ID
//    	boolean idOK = true;
//	    for (int i = 0; i < mId.length(); i++) {
//    	    if (!Character.isLetterOrDigit(mId.charAt(i))) {
//    	    	idOK = false;
//    	    	break;
//    	    }
//    	}
//		NOT NEEDED ANYMORE SINCE ID IS AN INT /DANA	    	    
	    if (mId<=0) {
    		errorCount++;
    	}
		
	    
	    
	    return errorCount;
			
	}
	
	/**
	 * Checks that a String is non empty nor only whitespaces, nor longer than MAX_LENGTH_OF_STRINGS
	 * @param string The string
	 * @return true if non empty nor too long
	 */
	public boolean checkString(String string) {
		
		if (string == null || string.length() == 0) {
			return false;
		} else {
			boolean onlyWhiteSpace = true;
			for (int i = 0; i < string.length(); i++) {
				onlyWhiteSpace = onlyWhiteSpace && Character.isWhitespace(string.charAt(i));
			}
			return string.length() <= MAX_LENGTH_OF_STRINGS && !onlyWhiteSpace;
		}
	}
	
	/**
	 * Checks that the number of answers is between MIN_NUMBER_OF_ANSWERS and MAX_NUMBER_OF_ANSWERS
	 * @return true if between the bounds
	 */
	public boolean checkNbAnswers() {
		return mAnswers.size()>=MIN_NUMBER_OF_ANSWERS && mAnswers.size()<=MAX_NUMBER_OF_ANSWERS;
	}
	
	/**
	 * Checks that a tag is shorter than MAX_LENGTH_OF_TAGS and made of alphanumerics characters
	 * @param tag The tag
	 * @return true if short enough and alphanumeric
	 */
	public boolean checkTag(String tag) {
		boolean tagOK = true;
		
    	if (tag.length()>MAX_LENGTH_OF_TAGS) {
    		tagOK = false;
    	
    	} else {
	    
    		for (int i = 0; i < tag.length(); i++) {
	    	    if (!Character.isLetterOrDigit(tag.charAt(i))) {
	    	    	tagOK = false;
	    	    	break;
	    	    }
	    	}
    	}
    	
	    return tagOK;
	}
	
    /**
     * Get multiple choice question
     * @return The question
     */
    public String getQuestion() {
    	return mQuestion;
    }
    
    /**
     * Get owner of the dataset
     * @return The owner
     */
    public String getOwner() {
    	return mOwner;
    } 
    
    /**
     * Set owner of the dataset
     * @param owner
     */
    public void setOwner(String owner) {
    	mOwner = owner;
    }
    
    /**
     * Get an array of possible answers
     * @return string holding the possible answers
     */
    public String[] getAnswers() {
    	return mAnswers.toArray(new String[mAnswers.size()]);
    }

    /**
     * Get an array of tags
     * @return string holding the possible answers
     */
    public String[] getTags() {
    	return mTags.toArray(new String[mTags.size()]);
    }
    
    /**
     * Get the index of the right solution in the given array of the possible answers
     * @return int the index of the correct answer
     */
    public int getSolutionIndex() {
    	return mSolutionIndex;
    }
    
    /**
     * Get id of the question-answer dataset
     * @return
     */
    public int getId() {
    	return mId;
    }
    
    /**
     * Set the possible answers
     * @param List<String> answers the answers to be set
     */
    public void setAnswers(List<String> answers) {
    	mAnswers = answers;	
    }
    
    /**
     * Add an answer or modify an existing one
     * @param answer Answer to be added/modified
     * @param index The index of the answer
     */
    public void addAnswerAtIndex(String answer, int index) {
    	// Replace an existing answer
    	if (mAnswers.size() >= index+1) {
    		mAnswers.set(index, answer);
    	// Push a new answer
    	} else if (mAnswers.size() == index) {
    		mAnswers.add(answer);
    	// Add empty answers and push new one
    	} else {
    		while (mAnswers.size() < index) {
    			mAnswers.add("");
    		}
    		mAnswers.add(answer);
    	}
    	System.out.println("Size of mAnswers: " + mAnswers.size());
    	
    }
    
    /**
     * Removes the answer at a given index from mEditedQuestion 
     * @param index The index of the answer to be removed
     */
    public void removeAnswerAtIndex(int index) {

    	if (mSolutionIndex < index) {
    		mAnswers.remove(index);
    	} else if (mSolutionIndex == index) {
    		mSolutionIndex = -1;
    		mAnswers.remove(index);
    	} else {
    		mSolutionIndex--;
    		mAnswers.remove(index);
    	}
    }
    
    /**
     * Set the possible tags
     * @param Set<String> tags the tags to be set
     */
    public void setTags(Set<String> tags) {
    	
    	mTags = tags;
    	// Avoid ConcurrentModificationException by storing tags to be removed first
    	Stack<String> toBeRemoved = new Stack<String>();
    	for (String tag : mTags) {
    		if (tag.equals("")) {
    			toBeRemoved.push(tag);
    		}
    	}
    	
    	while (!toBeRemoved.isEmpty()) {
    		mTags.remove(toBeRemoved.pop());
    	}
    }

    /**
     * Set the question
     * @param question the question to be set
     */
    public void setQuestion(String question) {
    	mQuestion = question;
    }
    
    /**
     * Set the index of the right solution in the given array of the possible answers
     * @param solutionIndex the index of the correct answer
     */
    public void setSolutionIndex(int solutionIndex) {
    	mSolutionIndex = solutionIndex;
    }
    /*
    /**
     * Set the id of the question-answers dataset
     * @param id the id of the dataset
     */
    public void setId(String id) {
    	mId = Integer.parseInt(id);
    }

    /**
     * Set the id of the question-answers dataset
     * @param id the id of the dataset
     */
    public void setId(int id) {
    	mId = id;
    }
    
    /**
     * Get the contents of the QuizQuestion in json string format
     * @return the question json string as understood by the Sweng Quiz Server
     * @throws JSONException
     */
    public String getJSONString() throws JSONException {

    	JSONObject json = new JSONObject();
    	JSONArray answersArray = new JSONArray();
    	JSONArray tagsArray = new JSONArray();
    	
    	for (String answer : mAnswers) {
	    	answersArray.put(answer);
    	}
    	json.put("answers", answersArray);
    	

    	for (String tag : mTags) {
        	tagsArray.put(tag);
        }
    	json.put("tags", tagsArray);
    	
    	json.put("question", mQuestion); 
    	json.put("solutionIndex", mSolutionIndex); 
    	json.put("id", mId); 
    	json.put("owner", mOwner); 
    	
    	return json.toString();

    }
    
    /**
     * Set the verdict
     * @param verdict the verdict to be set
     */
    public void setVerdict(String verdict) {
    	mVerdict = verdict;
    }
    
    /**
     * Set Verdict stats from JSON
     * @throws JSONException 
     * @param json JSON response from Quiz Server
     */
    public void setVerdictStats(JSONObject json) throws JSONException {
    	
    	if (json.getInt("likeCount") < 0 || json.getInt("dislikeCount") < 0 || json.getInt("incorrectCount") < 0) {
    		throw new JSONException("Negative Verdict Stat");
    	} else {
    		mLikeCount = json.getInt("likeCount");
    		mDislikeCount = json.getInt("dislikeCount");
    		mIncorrectCount = json.getInt("incorrectCount");
    	}
    }

    /**
     * Set Verdict from JSON
     * @throws JSONException 
     * @param json JSON response from Quiz Server
     */
    public void setVerdict(JSONObject json) throws JSONException {
    	
    	if (json.has("verdict")) {
    		if (!(json.getString("verdict").equals("incorrect") 
    				|| json.getString("verdict").equals("like") 
    				|| json.getString("verdict").equals("dislike"))) {
    			throw new JSONException("Invalid verdict");
    		}
       		mVerdict = json.getString("verdict");
    	} else {
    		mVerdict = "";
    	}
    }
    
    /**
     * Set Verdict from JSON and update stats
     * @throws JSONException 
     * @param json JSON response from Quiz Server
     */
    public void setVerdictAndUpdateStats(JSONObject json) throws JSONException {
    	
    	String oldVerdict = mVerdict;
    	setVerdict(json);
    	
    	if (oldVerdict.equals("like")) {
    		mLikeCount--;
    	} else if (oldVerdict.equals("dislike")) {
    		mDislikeCount--;
    	} else if (oldVerdict.equals("incorrect")) {
    		mIncorrectCount--;
    	}
    	
    	if (mVerdict.equals("like")) {
    		mLikeCount++;
    	} else if (mVerdict.equals("dislike")) {
    		mDislikeCount++;
    	} else if (mVerdict.equals("incorrect")) {
    		mIncorrectCount++;
    	}
    }
    
        
    
    /**
     * Set the like count
     * @return the like count
     */
    public int getLikeCount() {
    	return mLikeCount;
    }
    
    /**
     * Get the dislike count
     * @return the dislike count
     */
    public int getDislikeCount() {
    	return mDislikeCount;
    }
    
    /**
     * Get the incorrect count
     * @return the incorrect count
     */
    public int getIncorrectCount() {
    	return mIncorrectCount;
    }
    
    /**
     * Get the verdict
     * @param verdict the verdict to be set
     */
    public String getVerdict() {
    	return mVerdict;
    }

    public void setAnswerIndex(int index) {
		mAnswerIndex = index;
	}
    
    public int getAnswerIndex() {
		return mAnswerIndex;
	}
    
}
