package epfl.sweng.test.exam2;

import java.util.List;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;
import epfl.sweng.test.mocking.MockHttpClient;
import android.test.AndroidTestCase;

public class ResponseHandlingTest extends AndroidTestCase {
	
	private QuestionSearchCommunication questionSearch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		questionSearch = QuestionSearchCommunicationFactory.getInstance();
        SwengHttpClientFactory.setInstance(new MockHttpClient());
	}
	

	public void testEmptyResponseWith200() {
		boolean hasThrown= false;
		
		
		try {
			questionSearch.getQuestionsByOwner("emptywith200");
		} catch (CommunicationException e) {
			hasThrown = true;
		}
		
		assertTrue(hasThrown);
	}

	public void testEmptyResponseWith404() {
		boolean hasThrown= false;
		
		
		try {
			assertTrue(questionSearch.getQuestionsByOwner("emptywith404").isEmpty());
		} catch (CommunicationException e) {
			hasThrown = true;
		}
		
		assertFalse(hasThrown);
	}
	

	public void testHasWrongOwner() {
		boolean hasThrown= false;
		
		
		try {
			questionSearch.getQuestionsByOwner("haswrongowner");
		} catch (CommunicationException e) {
			hasThrown = true;
		}
		
		assertTrue(hasThrown);
	}
	

	public void testHasWrongTag() {
		boolean hasThrown= false;
		
		
		try {
			questionSearch.getQuestionsByTag("haswrongtag");
		} catch (CommunicationException e) {
			hasThrown = true;
		}
		
		assertTrue(hasThrown);
	}
	
	@Override
	protected void tearDown() throws Exception {
        SwengHttpClientFactory.setInstance(null);
	}
}
