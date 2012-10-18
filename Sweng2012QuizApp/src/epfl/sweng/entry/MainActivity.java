package epfl.sweng.entry;

import epfl.sweng.R;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/**
 * Main Activity of the Application
 *
 */
public class MainActivity extends Activity {

	/**
	 * Method invoked at the creation of the Activity. 
	 * @param Bundle savedInstanceState the saved instance
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
	/**
	 * Method invoked at the creation of the Options Menu. 
	 * @param Menu menu the created menu
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**
     * Change view to the ShowQuestionsActivity
     * @param View view reference to the menu button
     */
    public void goToDisplayActivity(View view) {
    	Intent displayActivityIntent = new Intent(this, ShowQuestionsActivity.class);
    	startActivity(displayActivityIntent);
    }

    /**
     * Change view to the EditQuestionActivity
     * @param View view reference to the menu button
     */
    public void goToSubmitActivity(View view) {
    	Intent displayActivityIntent = new Intent(this, EditQuestionActivity.class);
    	startActivity(displayActivityIntent);
    }
}
