package epfl.sweng.test;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.test.mocking.MockHttpClient;
import epfl.sweng.test.tools.TestingTricks;
import android.test.ActivityInstrumentationTestCase2;
import epfl.sweng.test.mocking.NoNetworkServerSimulator;
import epfl.sweng.test.mocking.QuestionOnlyServerSimulator;
import epfl.sweng.test.mocking.ServerSimulatorFactory;

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
	
	public void testRatingNoNetwork() {
		ServerSimulatorFactory.setInstance(new QuestionOnlyServerSimulator());
		solo.goBack();
		getActivity().startActivity(getActivity().getIntent());
		
		TestingTricks.authenticateMe(solo);
		if (solo.searchText("Show a random question")) {
			solo.clickOnButton("Show a random question");
		}
		solo.clickOnText("Incorrect (");
		assertTrue(solo.searchText("There was an error setting the rating"));
		
        SwengHttpClientFactory.setInstance(null);
	}
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();

        SwengHttpClientFactory.setInstance(null);
	}
}