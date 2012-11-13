package epfl.sweng.test.exam2;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.mock.FixtureHttpClient;
import epfl.sweng.test.mock.MockHttpClient;
import android.test.InstrumentationTestCase;

/**
 * Base class for tests that exercise the server communication code.
 *
 */
public abstract class ServerCommunicationTest extends InstrumentationTestCase {
	public final static int HTTP_OK = 200;
	public final static int HTTP_NOT_FOUND = 404;
	public final static int HTTP_INTERNAL_ERROR = 500;
	public final static String CONTENT_TYPE_JSON = "application/json";
	
	private MockHttpClient mockHttpClient;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mockHttpClient = 
				new FixtureHttpClient(getInstrumentation().getContext());
		SwengHttpClientFactory.setInstance(mockHttpClient);
	}
	
	@Override
	protected void tearDown() throws Exception {
		SwengHttpClientFactory.setInstance(null);
		super.tearDown();
	}
	
	protected MockHttpClient getMockHttpClient() {
		return mockHttpClient;
	}
}
