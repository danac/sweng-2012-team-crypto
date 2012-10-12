package epfl.sweng.test;

import org.json.JSONException;

import epfl.sweng.quizquestions.QuizQuestion;

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
    }
}