package epfl.sweng.entry;

import epfl.sweng.globals.Globals;
import android.app.Application;
import android.content.SharedPreferences;

/**
 * Main application entry of the App
 * @author cyril
 */
public class MainApplication extends Application {
	
	private static SharedPreferences mSettings;
	
	
	/**
	 * Method invoked at the creation of the application. 
	*/
    @Override
    public void onCreate() {
        super.onCreate();
        mSettings = getSharedPreferences(Globals.PREFS_NAME, 0);
    }
    
    /**
     * Static accessor method to enable the any method to access the shared preferences of the application from anywhere
     * @return the shared preferences settings
     */
    public static SharedPreferences getSettings() {
    	return mSettings;
    }
    
}
