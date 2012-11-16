package epfl.sweng.test.exam2;

import java.io.IOException;

import epfl.sweng.servercomm.search.CommunicationException;
import android.test.AndroidTestCase;

/**
 * 
 * @author cyril
 *
 */
public class CommunicationExceptionTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testConstructors() {
		assertNotNull(new CommunicationException());
		assertNotNull(new CommunicationException("CommunicationException Thrown"));
		assertNotNull(new CommunicationException(new IOException()));
		assertNotNull(new CommunicationException("CommunicationException Thrown", new IOException()));
	}
}
