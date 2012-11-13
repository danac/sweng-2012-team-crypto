package epfl.sweng.test.exam2;

import java.util.List;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;

/**
 * The tests for exercise 2.
 *
 */
public class Exercise2Test extends ServerSearchTest {
	public final static String TEST_TAG = "randomtag";
	public final static String GET_TAGGED = 
			"GET (?:https?://[^/]+)?/quizquestions/tagged/([^/\\s]+)\\b";
	
	public final static String SEARCH_RESULT =
			"{ \"id\": \"123\", \"owner\": \"joe\", \"solutionIndex\": 0, "
			+ "\"question\": \"what is the answer to life, the universe and everything?\", "
			+ "\"answers\": [ \"42\", \"24\" ], "
			+ "\"tags\": [ \"randomtag\", \"trivia\" ] }";
	
	private QuestionSearchCommunication questionSearch;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		questionSearch = QuestionSearchCommunicationFactory.getInstance();
	}

	@Override
	protected String getSearchTerm() {
		return TEST_TAG;
	}

	@Override
	protected String getRequestPattern() {
		return GET_TAGGED;
	}

	@Override
	protected List<QuizQuestion> invokeSearch(String term)
		throws CommunicationException {
		return questionSearch.getQuestionsByTag(term);
	}

	@Override
	protected String getQuestionJSON(String term) {
		return SEARCH_RESULT;
	}

}
