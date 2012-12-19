package epfl.sweng.test;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.test.mocking.MockHttpClient;
import epfl.sweng.test.tools.TestingTricks;
/**
 * First test case...
 */
public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;

	private static final int SLEEP_TIME = 2000;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
        SwengHttpClientFactory.setInstance(new MockHttpClient());		
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/* Begin list of the different tests to be performed */

	public void testShowRandom() {
		TestingTricks.authenticateMe(solo);
		
		solo.clickOnButton("Show a random question");

		solo.assertCurrentActivity("Random Question is being displayed",
                ShowQuestionsActivity.class);		
	}

	public void testSubmit() {
		TestingTricks.authenticateMe(solo);
		
		solo.clickOnButton("Submit quiz question");
		solo.assertCurrentActivity("Edit Question Form is being displayed",
                EditQuestionActivity.class);

		
		assertTrue(solo.searchText("Submit"));
		
	}

	public void testShowQuizzes() {
		TestingTricks.authenticateMe(solo);
		
		solo.clickOnButton("Take a Quiz");
		solo.assertCurrentActivity("Quizzes list is being displayed",
                ShowAvailableQuizzesActivity.class);

		
		
	}
	
	public void testLogOut() {
		TestingTricks.authenticateMe(solo);
		
		solo.clickOnButton("Log out");
		solo.sleep(SLEEP_TIME);
		assertTrue(solo.searchText("Please login"));
	}

	/* End list of the different tests to be performed */
	
	@Override
	protected void tearDown() throws Exception {
        SwengHttpClientFactory.setInstance(null);
        solo.finishOpenedActivities();
	}

}