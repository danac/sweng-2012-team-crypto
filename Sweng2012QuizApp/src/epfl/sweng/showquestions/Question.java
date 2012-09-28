package epfl.sweng.showquestions;

/**
 * Simple data structure holding the data retrieved from the web service
 *
 */
public class Question {
    private String question;
    private String[] answers;
    private int rightAnswer;
    private String[] tags;
    private String owner;
    private int id;
    
    public String getQuestion() {
    	return question;
    }
    
    public String[] getAnswers() {
    	return answers;
    }
      
    
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

}