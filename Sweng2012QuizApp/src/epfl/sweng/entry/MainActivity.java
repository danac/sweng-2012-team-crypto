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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void goToDisplayActivity(View view) {
    	Intent displayActivityIntent = new Intent(this, ShowQuestionsActivity.class);
    	startActivity(displayActivityIntent);
    }
    
    public void goToSubmitActivity(View view) {
    	Intent displayActivityIntent = new Intent(this, EditQuestionActivity.class);
    	startActivity(displayActivityIntent);
    }
}
