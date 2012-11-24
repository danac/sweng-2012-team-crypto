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
import epfl.sweng.tools.GUITools;

/**
 * Activity enabling the user to edit a question
 *
 */
public class EditQuestionActivity extends Activity {

	/**
	 * private variable holding the the question to be edited
	 */
	private QuizQuestion mEditedQuestion;
	
	/**
	 * Method invoked at the creation of the Activity. 
	 * @param savedInstanceState the saved instance
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);
        mEditedQuestion = new QuizQuestion();
        
        Button submitButton = (Button) findViewById(R.id.edit_button_submit);
        submitButton.setEnabled(false);
        
        List<Button> listButtons = GUITools.findAllButtons(
        		(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
        for (Button button : listButtons) {
        	button.setOnClickListener(new ButtonListener(this));
        }
        
        List<EditText> listEditTexts = GUITools.findAllEditTexts(
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
    
    /**
     * Return the final edited question
     * @return the edited QuizQuestion
     */
	public QuizQuestion getQuestion() {
		return mEditedQuestion;
	}

	/**
	 * Display an error in case the submission to the server failed
	 */
    public void displaySubmitError() {
    	Toast.makeText(this, getString(R.string.submit_question_error_text), Toast.LENGTH_LONG).show();
		
    }
    
    /**
     * Display a success message if the submission succeeded
     * @param question
     */
	public void displaySuccess(QuizQuestion question) {
		finish();
		startActivity(getIntent());
		Toast.makeText(this, String.format(getString(R.string.submit_question_success_text), question.getId()), 
				Toast.LENGTH_SHORT).show();		
	}
	

	/**
	 * Checks that there is an EditText displaying the question and that it is visible.
	 *
	 * @param listEditTexts list of all EditTexts
	 * @return true if no problem, else false
	 */
	private boolean checkEditTextsQuestion(List<EditText> listEditTexts) {
		
		boolean editQuestionOK = true;
		boolean editQuestionExists = false;
		for (EditText e : listEditTexts) {
			if (e.getId() == R.id.edit_question_text) {
				editQuestionExists = true;
				if (e.getHint() != getText(R.string.edit_question_hint)) {
					editQuestionOK = false;
				}
				if (e.getVisibility() != View.VISIBLE) {
					editQuestionOK = false;
				}
				break;
			}
		}
		if (!editQuestionExists || !editQuestionOK) {
			System.out.println("Error in verifying question EditText");
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks that there is an EditText to type in an answer and that it is visible.
	 *
	 * @param listEditTexts list of all EditTexts
	 * @return true if no problem, else false
	 */
	private boolean checkEditTextsAnswers(List<EditText> listEditTexts) {
	
		boolean answerOK = true;
		for (EditText e : listEditTexts) {
			if (e.getTag() == getText(R.string.edit_answer_hint)) {
				if (e.getHint() != getText(R.string.edit_answer_hint)) {
					answerOK = false;
				}
				if (e.getVisibility() != View.VISIBLE) {
					answerOK = false;
				}
			}			
		}
		if (!answerOK) {
			System.out.println("Error in verifying answers EditText");
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * Checks that there is an EditText to type in tags and that it is visible.
	 *
	 * @param listEditTexts list of all EditTexts
	 * @return true if no problem, else false
	 */
	private boolean checkEditTextsTags(List<EditText> listEditTexts) {
		
		boolean editTagOK = true;
		boolean editTagExists = false;
		for (EditText e : listEditTexts) {
			if (e.getId() == R.id.edit_tags) {
				editTagExists = true;
				if (e.getHint() != getText(R.string.edit_tags_hint)) {
					editTagOK = false;
				}
				if (e.getVisibility() != View.VISIBLE) {
					editTagOK = false;
				}
				break;
			}
		}
		if (!editTagExists || !editTagOK) {
			System.out.println("Error in verifying tags EditText");
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks that there is a Button to add a new answer that it is visible.
	 *
	 * @param listButtons list of all EditTexts
	 * @return true if no problem, else false
	 */
	private boolean checkButtonsNewAnswer(List<Button> listButtons) {
	
		boolean newAnswerButtonOK = true;
		boolean newAnswerButtonExists = false;
		for (Button b : listButtons) {
			if (b.getId() == R.id.edit_button_new_answer) {
				newAnswerButtonExists = true;
				if (b.getText() != getText(R.string.plus_sign)) {
					newAnswerButtonOK = false;
				}
				if (b.getVisibility() != View.VISIBLE) {
					newAnswerButtonOK = false;
				}
				break;
			}
		}
		if (!newAnswerButtonExists || !newAnswerButtonOK) {
			System.out.println("Error in verifying new Answer Button");
			return false;
		} else {
			return true;
		}	
	}
	
	/**
	 * Checks that there is a submit Button and that it is visible.
	 *
	 * @param listButtons list of all Buttons
	 * @return true if no problem, else false
	 */
	private boolean checkButtonsSubmit(List<Button> listButtons) {
		
		boolean submitButtonOK = true;
		boolean submitButtonExists = false;
		for (Button b : listButtons) {
			if (b.getId() == R.id.edit_button_submit) {
				submitButtonExists = true;
				if (b.getText() != getText(R.string.edit_button_submit)) {
					submitButtonOK = false;
				}
				if (b.getVisibility() != View.VISIBLE) {
					submitButtonOK = false;
				}
				break;
			}
		}
		if (!submitButtonExists || !submitButtonOK) {
			System.out.println("Error in verifying submit Button");
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Checks that there the remove answer Buttons are there and that they are visible.
	 *
	 * @param listEditText list of all EditTexts
	 * @return true if no problem, else false
	 */
	private boolean checkButtonsRemove(List<EditText> listEditTexts) {
		boolean removeAnswerButtonsOK = true;
		boolean removeAnswerButtonsExist = true;
		for (EditText e : listEditTexts) {
			if (e.getTag() == getText(R.string.edit_answer_hint)) {
				List<Button> listAnswerButtons = GUITools.findAllButtons(
		        		(ViewGroup) e.getParent());
				boolean removeAnswerButtonExists = false;
				for (Button b : listAnswerButtons) {
					if (b.getTag() == getText(R.string.hyphen_minus)) {
						removeAnswerButtonExists = true;
						if (b.getText() != getText(R.string.hyphen_minus)) {
							removeAnswerButtonsOK = false;
						}
						if (b.getVisibility() != View.VISIBLE) {
							removeAnswerButtonsOK = false;
						}
						break;
					}
				}
				removeAnswerButtonsExist = removeAnswerButtonsExist && removeAnswerButtonExists;
			}
		}
		if (!removeAnswerButtonsExist || !removeAnswerButtonsOK) {
			System.out.println("Error in verifying remove answer Buttons");
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 * Checks that there for each answer there is a toggle answer Buttons with the right text and visibility.
	 *
	 * @param listEditText list of all EditTexts
	 * @return true if no problem, else false
	 */
	private boolean checkButtonsToggleAnswer(List<EditText> listEditTexts) {
		boolean toggleButtonsOK = true;
		boolean toggleButtonsExist = true;
		for (EditText e : listEditTexts) {
			if (e.getTag() == getText(R.string.edit_answer_hint)) {
				List<Button> listAnswerButtons = GUITools.findAllButtons(
		        		(ViewGroup) e.getParent());
				boolean toggleButtonExists = false;
				for (Button b : listAnswerButtons) {
					if (b.getTag() == getText(R.string.heavy_check_mark)
						|| b.getTag() == getText(R.string.heavy_ballot_x)) {
						toggleButtonExists = true;
						if (!b.getText().equals(getText(R.string.heavy_check_mark))
							&& !b.getText().equals(getText(R.string.heavy_ballot_x))) {
							System.out.println("Button text: " + b.getText());
							toggleButtonsOK = false;
						}
						if (b.getVisibility() != View.VISIBLE) {
							toggleButtonsOK = false;
						}
					}
				}
				toggleButtonsExist = toggleButtonsExist && toggleButtonExists;
			}
		}
		if (!toggleButtonsExist || !toggleButtonsOK) {
			if (!toggleButtonsExist) {
				System.out.println("Error in verifying toggle answer Buttons: missing Button");
			}
			if (!toggleButtonsOK) {
				System.out.println("Error in verifying toggle answer Buttons: Button text not OK");
			}
			return false;
		} else {
			return true;
		}
	}
	

	/**
	 * Checks that there is at most one correct answer
	 *
	 * @param listButtons list of all Buttons
	 * @return true if no problem, else false
	 */
	private boolean checkUniqueCorrectAnswer(List<Button> listButtons) {
		int correctAnswerCount = 0;
		for (Button b : listButtons) {
			if (b.getText().equals(getText(R.string.heavy_check_mark))) {
				correctAnswerCount++;
			}
		}
		if (correctAnswerCount > 1) {
			System.out.println("Error in verifying that at most one answer is correct");
			return false;
		} else {
			return true;
		}
	}
	

	/**
	 * Checks the state of the submit button
	 *
	 * @return true if no problem, else false
	 */
	private boolean checkSubmitButtonState() {
		boolean submitButtonStateOK = true;
		Button submitButton = (Button) findViewById(R.id.edit_button_submit);
	   	if (mEditedQuestion.auditErrors(0) == 0 && !submitButton.isEnabled()) {
	   		submitButtonStateOK = false;
	   	} else if (mEditedQuestion.auditErrors(0) > 0 && submitButton.isEnabled()) {
	   		submitButtonStateOK = false;
	   	}
	   	if (!submitButtonStateOK) {
	   		System.out.println("Error in verifying the state of the Submit Button");
			return false;
	   	} else {
	   		return true;
		}
	}
	
	/**
	 * Checks the consistency of the graphical user interface.
	 *
	 * @return the number of inconsistencies found.
	 */
	public int auditErrors() {
		
		int errCount = 0;
		
		// Verifying EditTexts
		List<EditText> listEditTexts = GUITools.findAllEditTexts(
       		(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));

		// Verifying question EditText
		if (!checkEditTextsQuestion(listEditTexts)) {
			errCount++;
		}
		
		// Verifying answers EditText
		if (!checkEditTextsAnswers(listEditTexts)) {
			errCount++;
		}

		// Verifying tags EditText
		if (!checkEditTextsTags(listEditTexts)) {
			errCount++;
		}
		
		// Verifying Buttons
		List<Button> listButtons = GUITools.findAllButtons(
       		(ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content));
		
		// Verifying new answer Button
		if (!checkButtonsNewAnswer(listButtons)) {
			errCount++;
		}
		
		// Verifying submit Button
		if (!checkButtonsSubmit(listButtons)) {
			errCount++;
		}

		// Verifying the remove answer Buttons
		if (!checkButtonsRemove(listEditTexts)) {
			errCount++;
		}

		
		// Verifying the toggle answer Buttons
		if (!checkButtonsToggleAnswer(listEditTexts)) {
			errCount++;
		}
		
		// Verifying that there is at most one correct answer
		if (!checkUniqueCorrectAnswer(listButtons)) {
			errCount++;
		}

		// Verifying the state of the Submit Button
		if (!checkSubmitButtonState()) {
			errCount++;
		}
		
	    return errCount;
	}

}
