package epfl.sweng.test;

import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.quizzes.ShowAvailableQuizzesActivity;
import epfl.sweng.test.mocking.MockHttpClient;
import epfl.sweng.test.mocking.NoNetworkServerSimulator;
import epfl.sweng.test.mocking.ServerSimulatorFactory;
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


	public void testDisplayQuizzes() {
		TestingTricks.authenticateMe(solo);
		solo.assertCurrentActivity("Quizzes are being displayed",
				ShowAvailableQuizzesActivity.class);

		assertTrue(solo.searchText("The hardest quiz ever"));
		assertTrue(solo.searchText("Piece of cake"));
	}
	

	public void testNavigateQuiz() {
		TestingTricks.authenticateMe(solo);
		solo.assertCurrentActivity("Quizzes are being displayed",
				ShowAvailableQuizzesActivity.class);

		solo.clickOnText("The hardest quiz ever");
		
		assertTrue(solo.searchText("5, for very large values of 2"));
		
	}	
	
	public void testNoNetwork() {
		ServerSimulatorFactory.setInstance(new NoNetworkServerSimulator());
		solo.goBack();
		getActivity().startActivity(getActivity().getIntent());
		
		solo.assertCurrentActivity("Quizzes are being displayed",
				ShowAvailableQuizzesActivity.class);
		assertTrue(solo.searchText("An error occurred while fetching quizzes."));
		
		ServerSimulatorFactory.setInstance(null);
	}
	
	/* End list of the different tests to be performed */
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();

        SwengHttpClientFactory.setInstance(null);
	}

}