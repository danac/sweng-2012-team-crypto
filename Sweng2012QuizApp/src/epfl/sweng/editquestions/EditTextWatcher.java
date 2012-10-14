package epfl.sweng.editquestions;

import java.util.ArrayList;
import java.util.List;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion.QuizQuestionParam;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A class to handle all the EditTexts of an activity.
 */
public class EditTextWatcher implements TextWatcher {

	private EditQuestionActivity mActivity;
	private EditText mEditText;
	
	public EditTextWatcher(EditQuestionActivity activity, EditText editText) {
		super();
		mActivity = activity;
		mEditText = editText;
	}
	
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
		if (mEditText.getId() == mActivity.getResources().getInteger(R.id.edit_question_text)) {
			mActivity.buildQuestionFromView(mEditText, QuizQuestionParam.QUESTION,
					mEditText.getText().toString());
		}
		
		// User has entered tags
		else if (mEditText.getId() == mActivity.getResources().getInteger(R.id.edit_tags)) {
			String tags = ((EditText) mEditText).getText().toString();
			int i = 0;
			while (i < tags.length()) {
				
				// j is the index of the first character of a tag
				int j = i;
				while (Character.isLetterOrDigit(tags.charAt(i))) {
					i++;
				}
				
				// i is the index of the last character of a tag
				String tag = tags.substring(j, i);
				mActivity.buildQuestionFromView(mEditText, QuizQuestionParam.TAG, tag);
				
				// Move i to the next tag
				while (!Character.isLetterOrDigit(tags.charAt(i))) {
					i++;
				}
			}
		}
		
		// User has entered an answer
		else if ( ((EditText) mEditText).getTag().toString() ==
				mActivity.getResources().getText(R.string.edit_answer_hint)) {
			
			mActivity.buildQuestionFromView(mEditText, QuizQuestionParam.ANSWER,
					mEditText.getText().toString());
		}

    }


	/**
     * Finds recursively all the EditTexts inside a ViewGroup
     * @param v ViewGroup in which to search
     * @return List<EditText> The list of all the Buttons
     */
    public List<EditText> findAllEditTexts(ViewGroup v) {
    	List<EditText> list = new ArrayList<EditText>();
    	int nbChildren = v.getChildCount();
    	for (int i=0; i<nbChildren; i++) {
    		if (v.getChildAt(i) instanceof EditText) {
    			list.add((EditText)v.getChildAt(i));
    		}
    		else if (v.getChildAt(i) instanceof ViewGroup) {
    			list.addAll(findAllEditTexts((ViewGroup)v.getChildAt(i)));
    		}
    	}
    	return list;
    }

}
