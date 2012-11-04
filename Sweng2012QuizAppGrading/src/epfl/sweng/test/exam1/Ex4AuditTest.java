package epfl.sweng.test.exam1;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.test.robotium.SoloInteraction;
import epfl.sweng.test.shared.CommonEditQuestion;

/**
 * Checking Exercise 4
 */
public class Ex4AuditTest extends ActivityInstrumentationTestCase2<EditQuestionActivity> {

    private static final int EMPTY_ANSWERS_COUNT = 3;

    private Solo mSolo;
    private SoloInteraction mInteraction;
    private EditQuestionActivity mActivity;

    public Ex4AuditTest() {
        super(EditQuestionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation(), getActivity());
        mInteraction = new SoloInteraction(mSolo, this);
        mActivity = getActivity();

        addEmptyAnswers(EMPTY_ANSWERS_COUNT);
    }

    private void addEmptyAnswers(int count) {
        for (int i = 0; i < count; ++i) {
            mSolo.clickOnText(CommonEditQuestion.ADD_ANSWER_TEXT);
            getInstrumentation().waitForIdleSync();
        }
    }

    public void testE4B4SingleCorrectAnswer() {
        Button b1 = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0);
        mInteraction.setButtonText(b1, CommonEditQuestion.CORRECT_ANSWER_TEXT);

        int actual = CommonEditQuestion.invokeAuditErrors(mActivity);
        int expected = 0;

        assertEquals("Audit failed but only one question was marked correct", expected, actual);
    }

    public void testE4B4MultipleCorrectAnswers() {
        Button b1 = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0);
        mInteraction.setButtonText(b1, CommonEditQuestion.CORRECT_ANSWER_TEXT);

        b1 = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 1);
        mInteraction.setButtonText(b1, CommonEditQuestion.CORRECT_ANSWER_TEXT);

        int actual = CommonEditQuestion.invokeAuditErrors(mActivity);
        int expected = 1;

        assertEquals("Audit method failed to report the right number of errors", expected, actual);
    }

}
