package epfl.sweng.entry;

import epfl.sweng.R;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.authentication.IOfflineOnErrorCallback;
import epfl.sweng.authentication.SessionManager;
import epfl.sweng.globals.Globals;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Main application entry of the App
 * @author cyril
 */
public class MainApplication extends Application {
	
	
	/**
	 * Method invoked at the creation of the application. 
	*/
    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.getInstance().setSettings(getSharedPreferences(Globals.PREFS_NAME, 0));
        
        
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			
			@Override
			public void onActivityStopped(Activity activity) {
			}
			
			@Override
			public void onActivityStarted(final Activity activity) {
				if (!activity.getClass().getName().equals("epfl.sweng.authentication.AuthenticationActivity")) {
					checkAuthentication();
				}
				
				SessionManager.getInstance().setOfflineOnErrorCallback(new IOfflineOnErrorCallback() {
					
					@Override
					public void onSessionWentOffline() {
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(activity, 
										getText(R.string.msg_offline_on_error), Toast.LENGTH_LONG).show();
							}
							
						});
						
					}
				});
			}
			
			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
			}
			
			@Override
			public void onActivityResumed(Activity activity) {
			}
			
			@Override
			public void onActivityPaused(Activity activity) {
			}
			
			@Override
			public void onActivityDestroyed(Activity activity) {
			}
			
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			}
		});
        
    }
    

	private void checkAuthentication() {
        if (!SessionManager.getInstance().isAuthenticated()) {
        	Intent authenticationActivityIntent = new Intent(this, AuthenticationActivity.class);
        	authenticationActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(authenticationActivityIntent);
        }
	}
}
