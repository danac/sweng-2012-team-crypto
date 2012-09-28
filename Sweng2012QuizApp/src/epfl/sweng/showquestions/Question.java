package epfl.sweng.showquestions;

/**
 * Simple data structure holding the data retrieved from the web service
 */
public class Question {
    private String mQuestion;
    private String[] mAnswers;
    private int mSolutionIndex;
    private String[] mTags;
    private String mOwner;
    private int mId;

    /**
     * Constructor setting default values for the question and the answers
     */
    public Question() {
        super();
        mQuestion = "Choose an answer:";
    	mAnswers = new String[] {
			"Answer 1",
			"Answer 2",
			"Answer 3",
			"Answer 4"
    	};
    }


    /**
     * Get multiple choice question
     * @return String The question
     */
    public String getQuestion() {
    	return mQuestion;
    }
    
    /**
     * Get owner of the dataset
     * @return String The owner
     */
    public String getOwner() {
    	return mOwner;
    }
    
    /**
     * Get an array of possible answers
     * @return String[] string holding the possible answers
     */
    public String[] getAnswers() {
    	return mAnswers;
    }

    /**
     * Get an array of tags
     * @return String[] string holding the possible answers
     */
    public String[] getTags() {
    	return mTags;
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
     * @param String[] answers the answers to be set
     */
    public void setAnswers(String[] answers) {
    	mAnswers = answers;
    }
    
    /**
     * Set the possible tags
     * @param String[] tags the tags to be set
     */
    public void setTags(String[] tags) {
    	mTags = tags;
    }
    
    /**
     * Set the question
     * @param String question the question to be set
     */
    public void setQuestion(String question) {
    	mQuestion = question;
    }
    
    /**
     * Set the index of the right solution in the given array of the possible answers
     * @param int solutionIndex the index of the correct answer
     */
    public void setSolutionIndex(int solutionIndex) {
    	mSolutionIndex = solutionIndex;
    }
    
    /**
     * Set the id of the question-answers dataset
     * @param int id the id of the dataset
     */
    public void setId(int id) {
    	mId = id;
    }
    
    
    
}