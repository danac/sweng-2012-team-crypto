package epfl.sweng.test.exam2;

import java.util.List;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;

/**
 * The tests for exercise 1.
 *
 */
public class Exercise1Test extends ServerSearchTest {
	
	public final static String TEST_OWNER = "randomowner";
	public final static String GET_OWNED_BY = 
			"GET (?:https?://[^/]+)?/quizquestions/ownedby/([^/\\s]+)\\b";
	public final static String SEARCH_RESULT =
			"{ \"id\": \"123\", \"owner\": \"randomowner\", \"solutionIndex\": 0, "
			+ "\"question\": \"what is the answer to life, the universe and everything?\", "
			+ "\"answers\": [ \"42\", \"24\" ], "
			+ "\"tags\": [ \"h2g2\", \"trivia\" ] }";
	
	private QuestionSearchCommunication questionSearch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		questionSearch = QuestionSearchCommunicationFactory.getInstance();
	}

	@Override
	protected String getSearchTerm() {
		return TEST_OWNER;
	}

	@Override
	protected String getRequestPattern() {
		return GET_OWNED_BY;
	}

	@Override
	protected List<QuizQuestion> invokeSearch(String term) throws CommunicationException {
		return questionSearch.getQuestionsByOwner(term);
	}

	@Override
	protected String getQuestionJSON(String term) {
		return SEARCH_RESULT;
	}

}
