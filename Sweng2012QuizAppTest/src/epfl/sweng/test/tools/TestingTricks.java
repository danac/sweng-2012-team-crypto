package epfl.sweng.test.tools;

import com.jayway.android.robotium.solo.Solo;

import android.app.Activity;
import android.widget.EditText;

/**
 * Various GUI manipulation tools...
 */
public class TestingTricks {
	private final static String TEST_USERNAME = "valid";
	private final static String TEST_PASSWORD = "tutu";
	
	public static void authenticateMe(Activity activity, Solo solo) {
		
		for (EditText et: solo.getCurrentEditTexts()) {
			if (et.getTag().toString() 
				== activity.getResources().getText(epfl.sweng.R.string.auth_login_hint)) {
				solo.enterText(et, TEST_USERNAME);
			} else if (et.getTag().toString()
				== activity.getResources().getText(epfl.sweng.R.string.auth_pass_hint)) {
				solo.enterText(et, TEST_PASSWORD);
			}
		}				
		solo.clickOnButton("Log in using Tequila");
	}
}