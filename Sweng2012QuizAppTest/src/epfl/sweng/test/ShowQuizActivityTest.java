package epfl.sweng.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.quizzes.ShowQuizActivity;
import epfl.sweng.test.mocking.MockHttpClient;
import epfl.sweng.test.tools.TestingTricks;
/**
 * First test case...
 */
public class ShowQuizActivityTest extends
		ActivityInstrumentationTestCase2<ShowQuizActivity> {
	
	private Solo solo;
	
	private static final double TEST_SCORE = 13.58;

	private static final int QUIZ_ID = 125;
	
	public ShowQuizActivityTest() {
		super(ShowQuizActivity.class);

	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent(getInstrumentation().getContext(), ShowQuizActivity.class);
		intent.putExtra("id", QUIZ_ID);
		setActivityIntent(intent);
		SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
	}


	public void testAlertDialog() {
		TestingTricks.authenticateMe(solo);
		solo.assertCurrentActivity("A quiz is being displayed",
				ShowQuizActivity.class);
	    getActivity().runOnUiThread(new Runnable() {
	        public void run() {
	        	getActivity().displayScoreAlertDialog(TEST_SCORE);
	        }
	    });
		assertTrue("Could not find the dialog!", solo.searchText("13.58"));
		solo.clickOnText("OK");
	}
	/* End list of the different tests to be performed */
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();

        SwengHttpClientFactory.setInstance(null);
	}

}