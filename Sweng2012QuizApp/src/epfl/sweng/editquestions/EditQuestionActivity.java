package epfl.sweng.editquestions;


import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.quizquestions.QuizQuestion.QuizQuestionParam;
import epfl.sweng.editquestions.ButtonListener;

/**
 * Activity enabling the user to edit a question
 *
 */
public class EditQuestionActivity extends Activity {

	private QuizQuestion mEditedQuestion;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        
        mEditedQuestion = new QuizQuestion();
        
        ButtonListener buttonListener = new ButtonListener(this);
        List<Button> listButtons = buttonListener.findAllButtons(
        		(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
        for (Button button : listButtons) {
        	button.setOnClickListener(new ButtonListener(this));
        }
        
        OnEditTextFocusChangeListener onFocusChangeListener = new OnEditTextFocusChangeListener(this);
        List<EditText> listEditTexts = onFocusChangeListener.findAllEditTexts(
        		(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
        for (EditText EditText : listEditTexts) {
        	EditText.setOnFocusChangeListener(new OnEditTextFocusChangeListener(this));
        }
    }

    /**
     * Add a field to mEditedQuestion from the user input in a View
     * Calls EditText.setError() if invalid param
     * @param view The View from which value come from
     * @param param enum of the parameters
     * @param value value of the field to add
     * @return question The modified QuizQuestion
     */
    public void buildQuestionFromView(View view, QuizQuestionParam param, String value) {
    	
    	switch (param) {
    	
    	case QUESTION:
    		if (!mEditedQuestion.checkString(value)) {
    			((EditText) view).setError("The question must be non-empty or have less than 500 characters");
    		} else {
    			mEditedQuestion.setQuestion(value);
    		}
    		return;
    		
    	case ANSWER:
    		if (!mEditedQuestion.checkString(value)) {
    			((EditText) view).setError("An answer must be non-empty or have less than 500 characters");
    		} else {
    			mEditedQuestion.addAnswer(value);
    		}
    		return;
    		
    	case SOLUTION_INDEX:
    		mEditedQuestion.setSolutionIndex(Integer.parseInt(value));
    		return;
    		
    	case TAG:
    		if (!mEditedQuestion.checkTag(value)) {
    			((EditText) view).setError("A tag must be no longer than 20 alphanumeric characters");
    		} else {
    			mEditedQuestion.addTag(value);
    		}
    		return;
    		
    	default:
    		return;
    	}
    }
    
    public void displaySubmitError() {
    	
    }
}
