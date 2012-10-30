package epfl.sweng.authentication;

import epfl.sweng.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/**
 * Activity to allow users to authenticate against the Tequila and SWENG servers
 */
public class AuthenticationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_authentication, menu);
        return true;
    }
    
	/**
	 * Display an error in case the authentication failed
	 */
    public void displayAuthError() {
    	Toast.makeText(this, getString(R.string.auth_error_text), Toast.LENGTH_LONG).show();
    	
		
    }
	/**
	 * Display a confirmation when the authentication was successful
	 */    
    public void displayAuthSuccess() {
		finish();
		Toast.makeText(this, getString(R.string.auth_success_text), Toast.LENGTH_LONG).show();		
	}
    
    /**
     * Method starting the authentication process
     */
    public void doAuthentication(View view) {
    	SessionManager.authenticate(this);
    }

}
