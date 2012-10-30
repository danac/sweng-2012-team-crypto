package epfl.sweng.editquestions;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion.QuizQuestionParam;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * A class to handle all the EditTexts of an activity.
 */
public class EditTextWatcher implements TextWatcher {

	/**
	 * Reference to the EditQuestionActivity
	 */
	private EditQuestionActivity mActivity;
	
	/**
	 * Reference to the EditText to be watched
	 */
	private EditText mEditText;
	
	/**
	 * Constructor
	 * @param EditQuestionActivity activity the activity the editText to be watched is placed in
	 * @param EditText editText the editText to be watched
	 */
	public EditTextWatcher(EditQuestionActivity activity, EditText editText) {
		super();
		mActivity = activity;
		mEditText = editText;
	}
	
	/**
	 * Constructor
	 * @param EditQuestionActivity activity the activity the editText to be watched is placed in
	 */	
	public EditTextWatcher(EditQuestionActivity activity) {
		super();
		mActivity = activity;
	}

	public void afterTextChanged(Editable s) {
		
	}
	
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    	
    	// User has entered the question's text
		if (mEditText.getTag().toString() == mActivity.getResources().getText(R.string.edit_question_hint)) {
			mActivity.buildQuestionFromView(mEditText, QuizQuestionParam.QUESTION,
					mEditText.getText().toString());
		
		
		// User has entered tags
		} else if (mEditText.getTag().toString() == mActivity.getResources().getText(R.string.edit_tags_hint)) {
			mActivity.buildQuestionFromView(mEditText, QuizQuestionParam.TAGS,
					mEditText.getText().toString());
		
		
		// User has entered an answer
		} else if (mEditText.getTag().toString() == mActivity.getResources().getText(R.string.edit_answer_hint)) {
			mActivity.buildQuestionFromView(mEditText, QuizQuestionParam.ANSWER,
					mEditText.getText().toString());
		}

    }

}
