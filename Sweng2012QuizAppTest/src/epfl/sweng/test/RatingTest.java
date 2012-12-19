package epfl.sweng.test;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.test.mocking.MockHttpClient;
import epfl.sweng.test.tools.TestingTricks;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Test class for the rating system 
 */
public class RatingTest extends ActivityInstrumentationTestCase2<ShowQuestionsActivity> {
	
	private Solo solo;
	
	public RatingTest() {
		super(ShowQuestionsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
        SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());

	}
	public void testLikeIt() {
		TestingTricks.authenticateMe(solo);
		if (solo.searchText("Show a random question")) {
			solo.clickOnButton("Show a random question");
		}
		solo.clickOnText("Like (");
		assertTrue(solo.searchText("You like the question"));
		
	}
	
	public void testDislikeIt() {
		TestingTricks.authenticateMe(solo);
		if (solo.searchText("Show a random question")) {
			solo.clickOnButton("Show a random question");
		}
		solo.clickOnText("Dislike (");
		assertTrue(solo.searchText("You dislike the question"));
		
	}
	
	public void testIncorrect() {
		TestingTricks.authenticateMe(solo);
		if (solo.searchText("Show a random question")) {
			solo.clickOnButton("Show a random question");
		}
		solo.clickOnText("Incorrect (");
		assertTrue(solo.searchText("You think the question is incorrect"));
				
	}
	
	@Override
	protected void tearDown() throws Exception {
        SwengHttpClientFactory.setInstance(null);
        solo.finishOpenedActivities();
	}
}