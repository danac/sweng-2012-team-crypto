package epfl.sweng.editquestions;


import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.editquestions.ButtonListener;

/**
 * Activity enabling the user to edit a question
 *
 */
public class EditQuestionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        ButtonListener buttonListener = new ButtonListener(this);
        List<Button> listButtons = buttonListener.findAllButtons(
        		(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
        for (Button button : listButtons) {
        	button.setOnClickListener(new ButtonListener(this));
        }
    }

    public QuizQuestion createQuestionFromUser() {
    	QuizQuestion question = new QuizQuestion();
    	// TODO Implement a listener that will hydrate the question and check its validity
    	return question;
    }
    
    
    public void displaySubmitError() {
    	
    }
}
