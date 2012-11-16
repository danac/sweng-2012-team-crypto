package epfl.sweng.test;

import org.json.JSONException;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.tasks.IQuizServerCallback;
import epfl.sweng.tasks.LoadRandomQuestion;

import junit.framework.TestCase;

/**
 * Unit test for JSON methods in QuizQuestion 
 */
public class QuizQuestionTest extends TestCase {
    public static final String VALID_QUESTION_JSON = "{"
            + "question: 'What is the answer to life, the universe and everything?', "
            + "answers: ['42', '27'],"
            + "solutionIndex: 0,"
            + "tags : ['h2g2', 'trivia'],"
            + "owner : 'anonymous',"
            + "id : '123'"
            + "}";

    public void testQuestionOK() throws JSONException {
        String json = VALID_QUESTION_JSON;
        assertNotNull(new QuizQuestion(json));
        
        SwengHttpClientFactory.setInstance(null);
        new LoadRandomQuestion(new IQuizServerCallback() {
        	public void onSuccess(QuizQuestion question) {
        	}
        	public void onError() {
        	}
        }).execute();
    }
}