package epfl.sweng.authentication;

import epfl.sweng.R;
import epfl.sweng.R.layout;
import epfl.sweng.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

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
     * Method starting the authentication process
     */
    public void doAuthentication(View view) {
    	
		finish();
    }

}
