package epfl.sweng.test;
import org.json.JSONException;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tasks.SubmitQuestion;
/**
 * First test case...
 */
public class EditQuestionActivityTest extends
		ActivityInstrumentationTestCase2<EditQuestionActivity> {
	
	private Solo solo;
	private final static int WAIT_TIME = 4000;
	
	public EditQuestionActivityTest() {
		super(EditQuestionActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/* Begin list of the different tests to be performed */

	public void testEditQuestion() {

		solo.assertCurrentActivity("Edit Question Form is being displayed",
                EditQuestionActivity.class);

		
		assertTrue(solo.searchText("Submit"));

		solo.clickOnButton("\\+");
		solo.clickOnButton("\\-");
		solo.clickOnButton("\u2718");
		
		assertTrue(solo.searchText("\u2714"));
		
		solo.clickOnButton("Submit");

    	assertTrue(solo.waitForText("\u2718 An error occured while submitting the Question"));
	}

	
	public void testNoNetwork() {
		
		solo.assertCurrentActivity("Edit question form is being displayed",
                EditQuestionActivity.class);
    	new SubmitQuestion(getActivity()).execute(new QuizQuestion(), "http://www.google.com");
    	assertTrue(solo.waitForText("\u2718 An error occured while submitting the Question"));
    	solo.sleep(WAIT_TIME);
    	try {
			new SubmitQuestion(getActivity()).execute(new QuizQuestion(QuizQuestionTest.VALID_QUESTION_JSON), 
					"http://0.0.0.0");
		} catch (JSONException e) {
			fail();
		}
    	assertTrue(solo.waitForText("\u2718 An error occured while submitting the Question"));
	}
	
	/* End list of the different tests to be performed */
	
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}