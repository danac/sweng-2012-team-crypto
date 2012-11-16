package epfl.sweng.editquestions;

import java.util.List;
import epfl.sweng.R;
import epfl.sweng.globals.Globals;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.quizquestions.QuizQuestion.QuizQuestionParam;
import epfl.sweng.tasks.IQuizServerCallback;
import epfl.sweng.tasks.SubmitQuestion;
import epfl.sweng.tools.GUITools;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.EditText;

/**
 * A class to handle all the Buttons of an activity
 */
public class ButtonListener implements OnClickListener {

	
	/**
	 * Reference to the EditQuestionActivity
	 */
	private EditQuestionActivity mActivity;
	
	/**
	 * Constructor
	 * @param activity reference to the EditQuestionActivity
	 */
	public ButtonListener(EditQuestionActivity activity) {
		super();
		mActivity = activity;
	}

	/**
	 * A generic onClick method to manage the different buttons of the activity
	 * @param v the button clicked
	 */
	@Override
	public void onClick(View v) {
		String buttonTag = ((Button) v).getTag().toString();
		LinearLayout answersContainer = (LinearLayout) mActivity.findViewById(R.id.edit_answers_container);
		
		System.out.println("Button " + buttonTag + " clicked");
		
		// On click '+' button: add a new answer, set its Listeners and update question
		if (buttonTag == mActivity.getResources().getText(R.string.plus_sign)) {
			mActivity.getLayoutInflater().inflate(R.layout.edit_new_answer, answersContainer);
			
			// Set OnClickListeners
			List<Button> listNewButton = GUITools.findAllButtons(
					(ViewGroup) answersContainer.getChildAt(answersContainer.getChildCount()-1));
			for (Button button : listNewButton) {
				button.setOnClickListener(new ButtonListener(mActivity));
			}
			
			// Set EditTextWatcher
			List<EditText> listNewEditTexts= GUITools.findAllEditTexts(
					(ViewGroup) answersContainer.getChildAt(answersContainer.getChildCount()-1));
			for (EditText editText : listNewEditTexts) {
				editText.addTextChangedListener(new EditTextWatcher(mActivity, editText));
			}
			
			// Prevent user from entering more than MAX_NUMBER_OF_ANSWERS answers
			if (answersContainer.getChildCount() >= Globals.MAX_NUMBER_OF_ANSWERS) {  
				v.setEnabled(false);
			}
			
			// Update question
			mActivity.getQuestion().addAnswerAtIndex("", mActivity.getQuestion().getAnswers().length);
			Button submitButton = (Button) mActivity.findViewById(R.id.edit_button_submit);
	    	if (mActivity.getQuestion().auditErrors(0) == 0) {
	            submitButton.setEnabled(true);
	    	} else {
	    		submitButton.setEnabled(false);
	    	}
		
		// On click '-' button: remove corresponding answer, enable '+' and update question
		} else if (buttonTag == mActivity.getResources().getText(R.string.hyphen_minus)) {
			int index = ((ViewGroup) v.getParent().getParent()).indexOfChild((View) v.getParent());
			answersContainer.removeView((View) v.getParent());
			Button newAnswerButton = (Button) mActivity.findViewById(R.id.edit_button_new_answer);
			newAnswerButton.setEnabled(true);
			System.out.println("Removed answer at index: " + index);
			mActivity.getQuestion().removeAnswerAtIndex(index);
	    	Button submitButton = (Button) mActivity.findViewById(R.id.edit_button_submit);
	    	if (mActivity.getQuestion().auditErrors(0) == 0) {
	            submitButton.setEnabled(true);
	    	} else {
	    		submitButton.setEnabled(false);
	    	}
		
		
		// On click 'X' button: mark all other right answers as wrong and this one as right and update question
		} else if (buttonTag == mActivity.getResources().getText(R.string.heavy_ballot_x)) {
			List<Button> listWrongButton = GUITools.findAllButtons(
					(ViewGroup) answersContainer, mActivity.getResources().getText(R.string.heavy_check_mark));
			for (Button button : listWrongButton) {
				button.setText(mActivity.getResources().getText(R.string.heavy_ballot_x));
				button.setTag(mActivity.getResources().getText(R.string.heavy_ballot_x));
			}
			
			((Button) v).setText(mActivity.getResources().getText(R.string.heavy_check_mark));
			((Button) v).setTag(mActivity.getResources().getText(R.string.heavy_check_mark));
			mActivity.buildQuestionFromView(v, QuizQuestionParam.SOLUTION_INDEX,
					Integer.toString(((ViewGroup) v.getParent().getParent()).indexOfChild((View) v.getParent())));
		
		
		// On click 'V' button: check corresponding answer as wrong and update question
		} else if (buttonTag == mActivity.getResources().getText(R.string.heavy_check_mark)) {
			((Button) v).setText(mActivity.getResources().getText(R.string.heavy_ballot_x));
			((Button) v).setTag(mActivity.getResources().getText(R.string.heavy_ballot_x));
			mActivity.buildQuestionFromView(v, QuizQuestionParam.SOLUTION_INDEX, "null");
		
		
		// On click 'Submit' button
		} else if (buttonTag == mActivity.getResources().getText(R.string.edit_button_submit)) {
			
			new SubmitQuestion(new IQuizServerCallback() {
				public void onSuccess(QuizQuestion question) {
					mActivity.displaySuccess(question);
				}
				public void onError() {
					mActivity.displaySubmitError();
				}
			}).execute(mActivity.getQuestion());
			
			if (mActivity.getQuestion().auditErrors(0) != 0) {
			} else {
				Intent intent = new Intent(mActivity, EditQuestionActivity.class);
				mActivity.finish();
				mActivity.startActivity(intent);
			}

		}
	}


}
