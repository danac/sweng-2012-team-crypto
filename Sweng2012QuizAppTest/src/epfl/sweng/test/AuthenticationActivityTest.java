package epfl.sweng.test;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.mocking.MockHttpClient;
/**
 * Testing the authentication activity
 */
public class AuthenticationActivityTest extends
		ActivityInstrumentationTestCase2<AuthenticationActivity> {

	private final static String TEST_USERNAME = "Toto";
	private final static String TEST_PASSWORD = "tutu";
	
	private Solo solo;
	
	public AuthenticationActivityTest() {
		super(AuthenticationActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
        SwengHttpClientFactory.setInstance(new MockHttpClient());
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/* Begin list of the different tests to be performed */

	public void testMenu() {
		solo.assertCurrentActivity("Authentication form is being displayed",
				AuthenticationActivity.class);

		for (EditText et: solo.getCurrentEditTexts()) {
			if (et.getTag().toString() 
				== getActivity().getResources().getText(epfl.sweng.R.string.auth_login_hint)) {
				solo.enterText(et, TEST_USERNAME);
			} else if (et.getTag().toString() 
				== getActivity().getResources().getText(epfl.sweng.R.string.auth_pass_hint)) {
				solo.enterText(et, TEST_PASSWORD);
			}				
		}
		
		solo.clickOnButton("Log in using Tequila");
		
	}


	/* End list of the different tests to be performed */
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}