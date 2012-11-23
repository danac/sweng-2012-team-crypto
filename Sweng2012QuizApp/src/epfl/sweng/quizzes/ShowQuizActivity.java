package epfl.sweng.quizzes;

import epfl.sweng.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Class allowing to take a quiz and hand in the answers to the server
 * @author dchriste
 *
 */
public class ShowQuizActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz);
    }

    private void displayScoreAlertDialog(float score) {
    	String displayedText = new String(getText(R.string.quiz_score_alert_dialog_text).toString() + score);
    	AlertDialog.Builder alert=new AlertDialog.Builder(this);
    	alert.setMessage(displayedText);
    	alert.setTitle(R.string.quiz_score_alert_dialog_title);
    	
    	alert.setPositiveButton(R.string.quiz_score_alert_dialog_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Nothing special for the time being... //Dana
            }
        });
    	
    	alert.show();
    }
}
