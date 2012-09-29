import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.showquestions.ShowQuestionsActivity;

/**
 * First test case...
 */
public class ShowQuestionsActivityTest extends
		ActivityInstrumentationTestCase2<ShowQuestionsActivity> {
	
	private Solo solo;
	
	public ShowQuestionsActivityTest() {
		super(ShowQuestionsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/* Begin list of the different tests to be performed */
	
	public void testDisplayQuestion() {
		solo.assertCurrentActivity("A question is being displayed",
                ShowQuestionsActivity.class);
		assertTrue(solo.searchText("?"));
	}
	
	/* End list of the different tests to be performed */
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}