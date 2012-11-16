package epfl.sweng.test.exam2;

import epfl.sweng.servercomm.search.QuestionSearchCommunication;
import epfl.sweng.servercomm.search.QuestionSearchCommunicationFactory;
import android.test.AndroidTestCase;

/**
 * 
 * @author cyril
 *
 */
public class QuestionSearchCommunicationFactoryTest extends AndroidTestCase {
	
	private QuestionSearchCommunication questionSearch;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		questionSearch = QuestionSearchCommunicationFactory.getInstance();
	}
	
	public void testConstructor() {
		assertNotNull(new QuestionSearchCommunicationFactory());
	}
	
	public void testSetInstance() {
		QuestionSearchCommunicationFactory.setInstance(questionSearch);
	}
}
