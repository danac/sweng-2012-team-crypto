package epfl.sweng.test;

import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.test.mocking.MockHttpClient;
import epfl.sweng.test.tools.TestingTricks;
/**
 * First test case...
 */
public class ShowAvailableQuizzesActivityTest extends
		ActivityInstrumentationTestCase2<ShowAvailableQuizzesActivity> {
	
	private Solo solo;
	
	public ShowAvailableQuizzesActivityTest() {
		super(ShowAvailableQuizzesActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
        SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/* Begin list of the different tests to be performed */

	public void testDisplayQuizzes() {
		TestingTricks.authenticateMe(solo);
		solo.assertCurrentActivity("Quizzes are being displayed",
				ShowAvailableQuizzesActivity.class);

		assertTrue(solo.searchText("First Quiz"));
		assertTrue(solo.searchText("Second Quiz"));
	}
	
		
	
	/* End list of the different tests to be performed */
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();

        SwengHttpClientFactory.setInstance(null);
	}

}