package epfl.sweng.editquestions;

import java.util.ArrayList;
import java.util.List;

import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion.QuizQuestionParam;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * A class to handle all the EditTexts of an activity.
 */
public class OnEditTextFocusChangeListener implements OnFocusChangeListener {

	private EditQuestionActivity mActivity;
	
	public OnEditTextFocusChangeListener(EditQuestionActivity activity) {
		super();
		mActivity = activity;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
		if (hasFocus) {
			return;
		}
			
		// User has entered the question's text
		if (v.getId() == mActivity.getResources().getInteger(R.id.edit_question_text)) {
			mActivity.buildQuestionFromView(v, QuizQuestionParam.QUESTION,
					((EditText) v).getText().toString());
		}
		
		// User has entered tags
		else if (v.getId() == mActivity.getResources().getInteger(R.id.edit_tags)) {
			String tags = ((EditText) v).getText().toString();
			int i = 0;
			while (i < tags.length()) {
				
				// j is the index of the first character of a tag
				int j = i;
				while (Character.isLetterOrDigit(tags.charAt(i))) {
					i++;
				}
				
				// i is the index of the last character of a tag
				String tag = tags.substring(j, i);
				mActivity.buildQuestionFromView(v, QuizQuestionParam.TAG, tag);
				
				// Move i to the next tag
				while (!Character.isLetterOrDigit(tags.charAt(i))) {
					i++;
				}
			}
		}
		
		// User has entered an answer
		else if ( ((EditText) v).getTag().toString() ==
				mActivity.getResources().getText(R.string.edit_answer_hint)) {
			
			mActivity.buildQuestionFromView(v, QuizQuestionParam.ANSWER,
					((EditText) v).getText().toString());
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
