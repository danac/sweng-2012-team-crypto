package epfl.sweng.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.authentication.SessionManager;
import epfl.sweng.cache.IDoNetworkCommunication;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.test.mocking.InternalErrorServerSimulator;
import epfl.sweng.test.mocking.MockHttpClient;
import epfl.sweng.test.mocking.NoNetworkServerSimulator;
import epfl.sweng.test.mocking.ServerSimulatorFactory;
import epfl.sweng.test.tools.TestingTricks;

/**
 * Test class for the rating system 
 */
public class CachingTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private Solo solo;
	private final static int WAIT_TIME = 2000;
	private final static String TEST_QUESTION = "Test question...";
	private final static String TEST_FALSEANSWER = "False Answer";
	private final static String TEST_RIGHTANSWER = "Right Answer";
	private final static String TEST_TAGS = "test";
	private static final int SLEEP_LISTCHECK = 1000;
	
	public CachingTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
        SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testSwitchMode() {
		TestingTricks.authenticateMe(solo);
		solo.assertCurrentActivity("Are we on the MainActivity?", MainActivity.class);
		CheckBox chkBox = (CheckBox) solo.getView(epfl.sweng.R.id.main_checkbox_offline);
		
		solo.clickOnView(chkBox);
		solo.waitForText((String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_offline));
		assertFalse(SessionManager.getInstance().isOnline());
		assertTrue(chkBox.isChecked());
		
		solo.clickOnView(chkBox);
		solo.waitForText((String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_online));
		assertTrue(SessionManager.getInstance().isOnline());
		assertFalse(chkBox.isChecked());
	}

	private void editAndShowQuestion() {
		if (solo.searchText("Submit quiz question")) {
			solo.clickOnButton("Submit quiz question");
		}
		
		// We first edit a new question while offline to populate the cache...
		solo.assertCurrentActivity("Edit Question Form is being displayed",
                EditQuestionActivity.class);
		solo.sleep(WAIT_TIME);
		solo.clickOnButton("\\+");
    	boolean rightAnswerEntered = false;
		for (EditText et: solo.getCurrentEditTexts()) {
			if (et.getTag().toString() 
				== getActivity().getResources().getText(epfl.sweng.R.string.edit_question_hint)) {
				solo.enterText(et, TEST_QUESTION);
			} else if (et.getTag().toString() 
				== getActivity().getResources().getText(epfl.sweng.R.string.edit_tags_hint)) {
				solo.enterText(et, TEST_TAGS);
			} else if (et.getTag().toString() 
				== getActivity().getResources().getText(epfl.sweng.R.string.edit_answer_hint)) {
				
				if (rightAnswerEntered) {
					solo.enterText(et, TEST_FALSEANSWER);
				} else {
					solo.enterText(et, TEST_RIGHTANSWER);
					rightAnswerEntered = true;
				}
			}
			assertTrue(((EditQuestionActivity) solo.getCurrentActivity()).auditErrors()==0);
		}
		assertTrue(((EditQuestionActivity) solo.getCurrentActivity()).auditErrors()==0);
		solo.clickOnButton("\u2718");
		assertTrue(((EditQuestionActivity) solo.getCurrentActivity()).auditErrors()==0);
		solo.sleep(WAIT_TIME);
		solo.clickOnButton("Submit");
		assertTrue(solo.waitForText("Question successfully submitted"));
		
		// Now we go back to the main activity and display the question (while still offline)
		solo.goBackToActivity("MainActivity");
		
		if (solo.searchText("Show a random question")) {
			solo.clickOnButton("Show a random question");
		}
		
		solo.assertCurrentActivity("A question is being displayed",
                ShowQuestionsActivity.class);
		ListView l = solo.getCurrentListViews().get(0);
		assertNotNull("No list views!", l);
		solo.sleep(SLEEP_LISTCHECK);
		assertTrue("No items in list view!", l.getChildCount()>0);
		
		assertFalse(solo.searchText("error"));
	}
	
	public void testEditAndShowQuestionInCacheAndGoBackOnline() {
		TestingTricks.authenticateMe(solo);
		CheckBox chkBox = (CheckBox) solo.getView(epfl.sweng.R.id.main_checkbox_offline);
		Boolean isChecked = chkBox.isChecked();
		if (!isChecked) {
			solo.clickOnView(chkBox);
		}
		assertTrue(chkBox.isChecked());
		
		solo.sleep(WAIT_TIME);
		
		
		editAndShowQuestion();

		// Eventually, we go back on line to flush the cache
		solo.goBackToActivity("MainActivity");
		
		assertTrue(chkBox.isChecked());
		solo.clickOnView(chkBox);
		
		assertFalse(chkBox.isChecked());
		assertTrue(SessionManager.getInstance().isOnline());
		
		solo.clickOnView(chkBox);

		solo.waitForText(
				(String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_offline));	
	
		solo.clickOnButton("Show a random question");
		
		solo.clickOnText("Like (");
		assertTrue(solo.searchText("You like the question"));
		
		solo.clickOnText("Dislike (");
		assertTrue(solo.searchText("You dislike the question"));
		
		
		solo.clickOnText("Incorrect (");
		assertTrue(solo.searchText("You think the question is incorrect"));
		

		// Eventually, we go back on line to flush the cache
		solo.goBackToActivity("MainActivity");
		
		solo.clickOnView(chkBox);
		
		solo.waitForText(
				(String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_online));	
		
	}

	public void testNoNetworkUponGoingBackOnline() {
		TestingTricks.authenticateMe(solo);

		
		solo.assertCurrentActivity("Quizzes are being displayed",
				MainActivity.class);
		CheckBox chkBox = (CheckBox) solo.getView(epfl.sweng.R.id.main_checkbox_offline);
		Boolean isChecked = chkBox.isChecked();
		if (!isChecked) {
			solo.clickOnText("Offline");
		}
		assertTrue(chkBox.isChecked());
		
		
		ServerSimulatorFactory.setInstance(new NoNetworkServerSimulator());


		editAndShowQuestion();	
		
		// Eventually, we go back on line to flush the cache
		solo.goBackToActivity("MainActivity");
		
		assertTrue(chkBox.isChecked());
		solo.clickOnView(chkBox);
		solo.waitForText(
				(String) getActivity().getResources().getText(epfl.sweng.R.string.online_transition_error));		
		assertFalse(SessionManager.getInstance().isOnline());
		assertTrue(chkBox.isChecked());		
		ServerSimulatorFactory.setInstance(null);
		
		solo.clickOnView(chkBox);
		solo.waitForText(
				(String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_online));		
		
	}

	public void testGoOfflineOnServerError() {
		TestingTricks.authenticateMe(solo);

		
		solo.assertCurrentActivity("Quizzes are being displayed",
				MainActivity.class);
		CheckBox chkBox = (CheckBox) solo.getView(epfl.sweng.R.id.main_checkbox_offline);
		Boolean isChecked = chkBox.isChecked();
		if (isChecked) {
			solo.clickOnText("Offline");
			solo.waitForText((String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_online));
		}
		assertFalse(chkBox.isChecked());
		ServerSimulatorFactory.setInstance(new InternalErrorServerSimulator());

		solo.clickOnButton("Show a random question");
		solo.waitForText(
				(String) getActivity().getResources().getText(epfl.sweng.R.string.msg_offline_on_error));
		
		assertFalse(SessionManager.getInstance().isOnline());

		ServerSimulatorFactory.setInstance(null);

		solo.goBackToActivity("MainActivity");
		chkBox = (CheckBox) solo.getView(epfl.sweng.R.id.main_checkbox_offline);
		assertTrue(chkBox.isChecked());
		
		solo.clickOnText("Offline");
		
		solo.waitForText((String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_online));
		assertTrue(SessionManager.getInstance().isOnline());
		assertFalse(chkBox.isChecked());
	}


	public void testGoOfflineOnIOException() {
		TestingTricks.authenticateMe(solo);

		
		solo.assertCurrentActivity("Quizzes are being displayed",
				MainActivity.class);
		CheckBox chkBox = (CheckBox) solo.getView(epfl.sweng.R.id.main_checkbox_offline);
		Boolean isChecked = chkBox.isChecked();
		if (isChecked) {
			solo.clickOnText("Offline");
			solo.waitForText((String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_online));
		}
		assertFalse(chkBox.isChecked());
		ServerSimulatorFactory.setInstance(new NoNetworkServerSimulator());

		solo.clickOnButton("Show a random question");
		solo.waitForText(
				(String) getActivity().getResources().getText(epfl.sweng.R.string.msg_offline_on_error));
		
		assertFalse(SessionManager.getInstance().isOnline());

		ServerSimulatorFactory.setInstance(null);

		solo.goBackToActivity("MainActivity");
		chkBox = (CheckBox) solo.getView(epfl.sweng.R.id.main_checkbox_offline);
		assertTrue(chkBox.isChecked());
		
		solo.clickOnText("Offline");
		
		solo.waitForText((String) getActivity().getResources().getText(epfl.sweng.R.string.you_are_online));
		assertTrue(SessionManager.getInstance().isOnline());
		assertFalse(chkBox.isChecked());
	}

	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		ServerSimulatorFactory.setInstance(null);
		SessionManager.getInstance().setOnlineState(true, new IDoNetworkCommunication() {
			
			@Override
			public void onSuccess() {
			}
			
			@Override
			public void onError() {
			}
		});
		SwengHttpClientFactory.setInstance(null);
	}
	
}




