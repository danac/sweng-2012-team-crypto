package epfl.sweng.test.exam1;

import java.util.regex.Pattern;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.test.robotium.SoloInteraction;
import epfl.sweng.test.shared.CommonEditQuestion;

/**
 * Checking Exercise 5
 * 
 */
public class Ex5AuditTest extends ActivityInstrumentationTestCase2<EditQuestionActivity> {

    private Solo mSolo;
    private SoloInteraction mInteraction;
    private EditQuestionActivity mActivity;
    private Button mSubmit;

    public Ex5AuditTest() {
        super(EditQuestionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation(), getActivity());
        mInteraction = new SoloInteraction(mSolo, this);
        mActivity = getActivity();

        mSubmit = mSolo.getButton(CommonEditQuestion.SUBMIT_TEXT);
        assertNotNull("Could not find the submit button", mSubmit);
    }

    private void enterTextIntoHintedField(String hint, String text) {
        EditText field = mInteraction.getHintedField(hint);
        mSolo.enterText(field, text);
        getInstrumentation().waitForIdleSync();
    }

    private void enterQuestion() {
        enterTextIntoHintedField(CommonEditQuestion.QUESTION_HINT, CommonEditQuestion.QUESTION_TEXT);
        enterTextIntoHintedField(CommonEditQuestion.ANSWER_HINT, CommonEditQuestion.FIRST_ANSWER_TEXT);
        mInteraction.clickOnButton(Pattern.quote(CommonEditQuestion.ADD_ANSWER_TEXT));
        enterTextIntoHintedField(CommonEditQuestion.ANSWER_HINT, CommonEditQuestion.SECOND_ANSWER_TEXT);
        enterTextIntoHintedField(CommonEditQuestion.TAGS_HINT, "h2g2, trivia");
        mInteraction.clickOnView(mInteraction.getNthButton(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0));
        getInstrumentation().waitForIdleSync();
    }

    public void testEx5FreshlyStartedActivityEnabledButton() {
        Button submit = mSolo.getButton(CommonEditQuestion.SUBMIT_TEXT);
        assertNotNull("Could not find the submit button", submit);

        mInteraction.setButtonEnabled(submit, true);
        assertEquals("Audit did not catch enabled submit button with an invalid question", 1,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testEx5MinimalValidQuestion() {
        enterQuestion();
        assertEquals("Audit reported incorrect error(s)", 0, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testEx5MinimalValidQuestionDisabledButton() {
        enterQuestion();
        mInteraction.setButtonEnabled(mSubmit, false);
        assertEquals("Audit reported incorrect number of errors", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testEx5InvalidQuestionNoCorrectAnswerDisabledButton() {
        enterTextIntoHintedField(CommonEditQuestion.QUESTION_HINT, CommonEditQuestion.QUESTION_TEXT);
        enterTextIntoHintedField(CommonEditQuestion.ANSWER_HINT, CommonEditQuestion.FIRST_ANSWER_TEXT);
        mInteraction.clickOnButton(Pattern.quote(CommonEditQuestion.ADD_ANSWER_TEXT));
        enterTextIntoHintedField(CommonEditQuestion.ANSWER_HINT, CommonEditQuestion.SECOND_ANSWER_TEXT);
        enterTextIntoHintedField(CommonEditQuestion.TAGS_HINT, "h2g2, trivia");

        // Do not select the correct answer
        // mInteraction.clickOnView(mInteraction.getNthButton(CommonEditQuestion.INCORRECT_ANSWER_TEXT,
        // 0));

        assertEquals("Audit reported incorrect error(s)", 0, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testEx5InvalidQuestionNoCorrectAnswerEnabledButton() {
        enterTextIntoHintedField(CommonEditQuestion.QUESTION_HINT, CommonEditQuestion.QUESTION_TEXT);
        enterTextIntoHintedField(CommonEditQuestion.ANSWER_HINT, CommonEditQuestion.FIRST_ANSWER_TEXT);
        mInteraction.clickOnButton(Pattern.quote(CommonEditQuestion.ADD_ANSWER_TEXT));
        enterTextIntoHintedField(CommonEditQuestion.ANSWER_HINT, CommonEditQuestion.SECOND_ANSWER_TEXT);
        enterTextIntoHintedField(CommonEditQuestion.TAGS_HINT, "h2g2, trivia");

        // Do not select the correct answer
        // mInteraction.clickOnView(mInteraction.getNthButton(CommonEditQuestion.INCORRECT_ANSWER_TEXT,
        // 0));
        mInteraction.setButtonEnabled(mSubmit, true);
        assertEquals("Audit reported incorrect error(s)", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testEx5InvalidQuestionIncompleteAnswerEnabledButton() {
        enterTextIntoHintedField(CommonEditQuestion.QUESTION_HINT, CommonEditQuestion.QUESTION_TEXT);
        enterTextIntoHintedField(CommonEditQuestion.ANSWER_HINT, CommonEditQuestion.FIRST_ANSWER_TEXT);
        mInteraction.clickOnButton(Pattern.quote(CommonEditQuestion.ADD_ANSWER_TEXT));

        // Do not enter any answer here
        // enterTextIntoHintedField(CommonEditQuestion.ANSWER_HINT,
        // CommonEditQuestion.SECOND_ANSWER_TEXT);

        enterTextIntoHintedField(CommonEditQuestion.TAGS_HINT, "abc");

        mInteraction.clickOnView(mInteraction.getNthButton(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0));
        mInteraction.setButtonEnabled(mSubmit, true);
        assertEquals("Audit report incorrect error(s)", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }
}
