package epfl.sweng.test.exam2;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;
import android.test.AndroidTestCase;
/**
 * 
 * @author cyril
 *
 */
public class GetQuestionsByTagTest extends AndroidTestCase {
	
	private QuestionSearchCommunication questionSearch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		questionSearch = QuestionSearchCommunicationFactory.getInstance();

        SwengHttpClientFactory.setInstance(null);
	}
	
	public void testInterfaceImplementation() {
		String className = questionSearch.getClass().getName();
		assertEquals("epfl.sweng.servercomm.search.DefaultQuestionSearchCommunication",
				className);
	}
	
	public void testSearchByTag() throws CommunicationException {
		questionSearch.getQuestionsByTag("quote");
		
		
	}
	
	public void testThrowsIllegalArgumentException() throws CommunicationException {
		boolean hasThrown = false;
		try {
			questionSearch.getQuestionsByTag("ä$ü[");
		} catch (IllegalArgumentException e) {
			hasThrown = true;
		}
		
		assertTrue(hasThrown);
		
		hasThrown = false;
		try {
			questionSearch.getQuestionsByTag("012345678901234567891");
		} catch (IllegalArgumentException e) {
			hasThrown = true;
		}
		
		assertTrue(hasThrown);
		
		hasThrown = false;
		try {
			questionSearch.getQuestionsByTag(null);
		} catch (IllegalArgumentException e) {
			hasThrown = true;
		}
		
		assertTrue(hasThrown);
				
	}
}
