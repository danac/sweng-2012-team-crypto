package epfl.sweng.editquestions;

import epfl.sweng.R;
import epfl.sweng.R.layout;
import epfl.sweng.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
