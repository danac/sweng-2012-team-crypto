package epfl.sweng.showquestions;

/**
 * Simple data structure holding the data retrieved from the web service
 */
public class Question {
    private String question;
    private String[] answers;
    private int solutionIndex;
    private String[] tags;
    private String owner;
    private int id;

    /**
     * Constructor setting default values for the question and the answers
     */
    public Question() {
        super();
        question = "Choose an answer:";
    	answers = new String[] {
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
    	return question;
    }
    
    /**
     * Get an array of possible answers
     * @return String[] string holding the possible answers
     */
    public String[] getAnswers() {
    	return answers;
    }
    
    /**
     * Set the possible answers
     * @param String[] _answers the answers to be set
     */
    public void setAnswers(String[] _answers) {
    	answers = _answers;
    }
    
    /**
     * Set the possible tags
     * @param String[] _tags the tags to be set
     */
    public void setTags(String[] _tags) {
    	tags = _tags;
    }
    
    /**
     * Set the question
     * @param String _question the question to be set
     */
    public void setQuestion(String _question) {
    	question = _question;
    }
    
    /**
     * Set the index of the right solution in the given array of the possible answers
     * @param int _solutionIndex the index of the correct answer
     */
    public void setSolutionIndex(int _solutionIndex) {
    	solutionIndex = _solutionIndex;
    }
    
    /**
     * Set the id of the question-answers dataset
     * @param int id the id of the dataset
     */
    public void setId(int _id) {
    	id = _id;
    }
    
    
    
}