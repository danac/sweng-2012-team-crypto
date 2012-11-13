package epfl.sweng.test.exam2;

import java.util.List;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;
import android.test.AndroidTestCase;

/**
 * The sanity checks for exam 2.
 *
 */
public class ServerCommunicationSanityTest extends AndroidTestCase {
	
	private QuestionSearchCommunication questionSearch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		questionSearch = QuestionSearchCommunicationFactory.getInstance();
		// TODO: Add here the SwengHttpClientFactory instrumentation
		// to intercept the server communication.
	}
	
	public void testInterfaceImplementation() {
		String className = questionSearch.getClass().getName();
		assertEquals("epfl.sweng.servercomm.search.DefaultQuestionSearchCommunication",
				className);
	}
	
	public void testSearchByOwner() throws CommunicationException {
		List<QuizQuestion> questions = 
				questionSearch.getQuestionsByOwner("joe");
		
	}
	
	public void testSearchByTag() throws CommunicationException {
		List<QuizQuestion> questions =
				questionSearch.getQuestionsByTag("tag");
		
	}
}
