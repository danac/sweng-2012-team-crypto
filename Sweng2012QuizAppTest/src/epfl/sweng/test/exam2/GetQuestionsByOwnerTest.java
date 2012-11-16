package epfl.sweng.test.exam2;

import java.util.List;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;
import android.test.AndroidTestCase;

public class GetQuestionsByOwnerTest extends AndroidTestCase {
	
	private QuestionSearchCommunication questionSearch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		questionSearch = QuestionSearchCommunicationFactory.getInstance();
        SwengHttpClientFactory.setInstance(null);
		// TODO: Add here the SwengHttpClientFactory instrumentation
		// to intercept the server communication.
	}
	
	public void testSearchByOwner() throws CommunicationException {
		List<QuizQuestion> questions = 
				questionSearch.getQuestionsByOwner("misev");
		
		questions = questionSearch.getQuestionsByOwner("tutu");
		
		assertTrue(questions.isEmpty());
		
	}
	
	public void testThrowsIllegalArgumentException() throws CommunicationException {
		boolean hasThrown = false;
		try{
			questionSearch.getQuestionsByOwner("ä$ü[");
		} catch(IllegalArgumentException e) {
			hasThrown = true;
		};
		
		assertTrue(hasThrown);
		

		hasThrown = false;
		try{
			questionSearch.getQuestionsByOwner("012345678901234567891");
		} catch(IllegalArgumentException e) {
			hasThrown = true;
		};
		
		assertTrue(hasThrown);
		
		hasThrown = false;
		try{
			questionSearch.getQuestionsByOwner(null);
		} catch(IllegalArgumentException e) {
			hasThrown = true;
		};
		
		assertTrue(hasThrown);
		
	}
	
}
