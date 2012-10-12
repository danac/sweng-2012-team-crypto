package epfl.sweng.editquestions;

import epfl.sweng.R;
import epfl.sweng.R.layout;
import epfl.sweng.R.menu;
import epfl.sweng.quizquestions.QuizQuestion;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EditQuestionActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
    }

    public QuizQuestion createQuestion() {
    	QuizQuestion question = new QuizQuestion();
    	// TODO Implement a listener that will hydrate the question
    	// and check that it is valid using question.auditErrors()
    }
    
    
    public void displaySubmitError() {
    	
    }
}
