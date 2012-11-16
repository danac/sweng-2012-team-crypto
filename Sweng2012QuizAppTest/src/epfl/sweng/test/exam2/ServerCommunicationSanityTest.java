package epfl.sweng.test.exam2;

import epfl.sweng.servercomm.search.CommunicationException;
import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;
import android.test.AndroidTestCase;

/**
 * 
 * @author cyril
 *
 */
public class ServerCommunicationSanityTest extends AndroidTestCase {
	
	private QuestionSearchCommunication questionSearch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		questionSearch = QuestionSearchCommunicationFactory.getInstance();
	}
	
	public void testInterfaceImplementation() {
		String className = questionSearch.getClass().getName();
		assertEquals("epfl.sweng.servercomm.search.DefaultQuestionSearchCommunication",
				className);
	}
	
	public void testSearchByOwner() throws CommunicationException {
		questionSearch.getQuestionsByOwner("joe");
		
	}
	
	public void testSearchByTag() throws CommunicationException {
		questionSearch.getQuestionsByTag("tag");
		
	}
}
