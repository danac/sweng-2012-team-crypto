package epfl.sweng.quizzes;

import epfl.sweng.R;
import epfl.sweng.authentication.SessionManager;
import epfl.sweng.entry.MainActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Class allowing to take a quiz and hand in the answers to the server
 * @author dchriste
 *
 */
public class ShowQuizActivity extends Activity {
	
	private Quiz mQuiz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz);
        
        if (!SessionManager.getInstance().isAuthenticated()) {
        	finish();
        	Intent mainActivityIntent = new Intent(this, MainActivity.class);
        	startActivity(mainActivityIntent);
        }
        
        Intent startingIntent = getIntent();
        String quizID = startingIntent.getStringExtra(ShowAvailableQuizzesActivity.class.getName());
    }

    public void displayScoreAlertDialog(double score) {
    	String displayedText = new String(getText(R.string.quiz_score_alert_dialog_text).toString() + " " + score);
    	AlertDialog.Builder alert=new AlertDialog.Builder(this);
    	alert.setMessage(displayedText);
    	alert.setTitle(R.string.quiz_score_alert_dialog_title);
    	
    	alert.setPositiveButton(R.string.quiz_score_alert_dialog_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Nothing special for the time being... //Dana
            }
        });
    	Log.i("ALERT_DIALOG",displayedText);
    	AlertDialog alertBox = alert.create();
    	alertBox.show();
    }
    
    public void previousQuestion() {
    	
    }
    
    public void nextQuestion() {
    	
    }
    
    public void handInQuiz() {
    	
    }
}
