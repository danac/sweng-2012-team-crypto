package epfl.sweng.editquestions;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.quizquestions.QuizQuestion.QuizQuestionParam;

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
        
        Button submitButton = (Button) findViewById(R.id.edit_button_submit);
        submitButton.setEnabled(false);
        
        ButtonListener buttonListener = new ButtonListener(this);
        List<Button> listButtons = buttonListener.findAllButtons(
        		(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
        for (Button button : listButtons) {
        	button.setOnClickListener(new ButtonListener(this));
        }
        
        EditTextWatcher editTextWatcher = new EditTextWatcher(this);
        List<EditText> listEditTexts = editTextWatcher.findAllEditTexts(
        		(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
        for (EditText editText : listEditTexts) {
        	editText.addTextChangedListener(new EditTextWatcher(this, editText));
        }
    }

    /**
     * Add a field to mEditedQuestion from the user input in a View
     * Calls EditText.setError() if invalid param
     * @param view The View from which value comes from
     * @param param enum of the parameters
     * @param value value of the field to add
     * @return true if mEditedQuestion is valid according to mEditedQuestion.auditErrors()
     */
    public boolean buildQuestionFromView(View view, QuizQuestionParam param, String value) {
    	
    	switch (param) {
    	
	    	case QUESTION:
	    		if (!mEditedQuestion.checkString(value)) {
	    			((EditText) view).setError("The question must be non-empty or have less than 500 characters");
	    		} else {
	    			((EditText) view).setError(null);
	    		}
	    		mEditedQuestion.setQuestion(value);
	    		break;
	    		
	    	case ANSWER:
	    		if (!mEditedQuestion.checkString(value)) {
	    			((EditText) view).setError("An answer must be non-empty or have less than 500 characters");
	    		} else {
	    			((EditText) view).setError(null);
	    		}
	    		mEditedQuestion.addAnswerAtIndex(value, 
    					((ViewGroup) view.getParent().getParent()).indexOfChild((View) view.getParent()));
	    		break;
	    		
	    	case SOLUTION_INDEX:
	    		// A right answer has been marked as wrong
	    		if (value.equals("null")) {
	    			mEditedQuestion.setSolutionIndex(-1);
	    			Toast toast = Toast.makeText(this, "One answer should be marked as correct", Toast.LENGTH_SHORT);
	    			toast.show();
	    		// The answer with index -value has been removed
	    		} else if (Integer.parseInt(value)<0) {
	    			int index = -Integer.parseInt(value);
	    			if (index == mEditedQuestion.getSolutionIndex()) {
	    				Toast toast = Toast.makeText(this,
	    						"One answer should be marked as correct", Toast.LENGTH_SHORT);
		    			toast.show();
	    			}
	    			mEditedQuestion.removeAnswerAtIndex(index);
	    		// An answer has been marked as correct
	    		} else {
	    			mEditedQuestion.setSolutionIndex(Integer.parseInt(value));
	    		}
	    		break;
	    		
	    	case TAGS:    		
	    		String[] tags = value.split("[^a-zA-Z0-9']");
	    		boolean flag = true;
	    		for (String tag : tags) {
	    			flag = flag && mEditedQuestion.checkTag(tag);
	    			if (!flag) {
	    				((EditText) view).setError("A tag must be less than 20 alphanumeric characters");
	    				break;
	    			}
	    		}
				if (flag) {
					((EditText) view).setError(null);
				}
				Set<String> tagsSet = new HashSet<String>(Arrays.asList(tags));
				mEditedQuestion.setTags(tagsSet);
				System.out.println("Number of tags: " + mEditedQuestion.getTags().length);
				System.out.println("Tags:");
				for (String tag : mEditedQuestion.getTags()) {
					System.out.println(tag);
				}
				break;
	    		
	    	default:
	    		break;
    	}
    	
    	Button submitButton = (Button) findViewById(R.id.edit_button_submit);
    	if (mEditedQuestion.auditErrors(0) == 0) {
            submitButton.setEnabled(true);
    	} else {
    		submitButton.setEnabled(false);
    	}
    	
    	return mEditedQuestion.auditErrors(0) == 0;
    }
    

	public QuizQuestion getQuestion() {
		return mEditedQuestion;
	}

	
    public void displaySubmitError() {
    	Toast.makeText(this, "An error occured while submitting the Question", Toast.LENGTH_SHORT).show();
		
    }
    
	public void displaySuccess(QuizQuestion question) {
		finish();
		startActivity(getIntent());
		Toast.makeText(this, "Question successfully sumitted, has id "+ question.getId(), 
				Toast.LENGTH_SHORT).show();		
	}
}
