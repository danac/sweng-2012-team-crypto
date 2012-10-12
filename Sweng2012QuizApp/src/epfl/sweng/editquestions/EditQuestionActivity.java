package epfl.sweng.editquestions;

import epfl.sweng.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * Activity enabling the user to edit a question
 *
 */
public class EditQuestionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_question, menu);
        return true;
    }
    
    
    public void displaySubmitError() {
    	
    }
}
