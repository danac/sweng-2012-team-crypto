package epfl.sweng.test.exam1;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.test.robotium.SoloInteraction;
import epfl.sweng.test.shared.CommonEditQuestion;

/**
 * Checking Exercise 2.
 * 
 * The test suite checks each sub-property of every bullet individually, then
 * combines them in various ways.
 * 
 * The exam states: "Each bullet point corresponds to a property, and all three
 * properties must be checked independently of each other. Each property
 * violation should increase the return value of your audit method by one."
 * 
 * The most frequent mistake was to increase by one for each sub-property
 * violation.
 * 
 */
public class Ex2AuditTest extends ActivityInstrumentationTestCase2<EditQuestionActivity> {

    private Solo mSolo;
    private SoloInteraction mInteraction;
    private EditQuestionActivity mActivity;

    private EditText mQuestionField;
    private EditText mTagsField;
    private EditText mFirstAnswerField;

    private static final int MAX_NB_PROPERTIES = 3;

    public Ex2AuditTest() {
        super(EditQuestionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        mSolo = new Solo(getInstrumentation(), getActivity());
        mInteraction = new SoloInteraction(mSolo, this);
        mActivity = getActivity();

        mTagsField = mInteraction.getHintedField(CommonEditQuestion.TAGS_HINT);
        assertNotNull("Could not find tags field", mTagsField);

        mFirstAnswerField = mInteraction.getHintedField(CommonEditQuestion.ANSWER_HINT);
        assertNotNull("Could not find answer field", mFirstAnswerField);

        mQuestionField = mInteraction.getHintedField(CommonEditQuestion.QUESTION_HINT);
        assertNotNull("Could not find question field", mQuestionField);
    }

    /**
     * By default, a freshly started activity must not have any audit
     * violations.
     */
    public void testAuditCheckPasses() {
        EditQuestionActivity activity = getActivity();
        assertEquals("The audit method failed on a freshly started activity", 0,
                CommonEditQuestion.invokeAuditErrors(activity));
    }

    /**
     * An EditText widget exists to enter the question. This widget has its hint
     * set to "Type in the question's text body".
     */
    public void testE2B1QuestionTestBodyHint() {
        mInteraction.setHint(mQuestionField, "blabla");
        assertEquals("Bad number of violations", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    /**
     * The widget has its visibility property set to VISIBLE.
     */
    public void testE2B1QuestionTestBodyVisible() {
        mInteraction.setVisibility(mQuestionField, View.INVISIBLE);
        assertEquals("Bad number of violations", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testE2B1QuestionTestBodyInvisibleAndIncorrectHint() {
        mInteraction.setHint(mQuestionField, "blabla");
        mInteraction.setVisibility(mQuestionField, View.INVISIBLE);
        assertEquals("Incorrect hint and visibility should count as one violation", 1,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    /**
     * There exist zero or more EditText widgets to enter answers. These have
     * their hint set to "Type in the answer".
     */
    public void testE2B2AnswerHint() {
        mInteraction.setHint(mFirstAnswerField, "blabla");
        assertEquals("Bad number of violations", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    /**
     * Their visibility properties are set to VISIBLE.
     */
    public void testE2B2QuestionTagsVisible() {
        mInteraction.setVisibility(mFirstAnswerField, View.INVISIBLE);
        assertEquals("Bad number of violations", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testE2B2QuestionTagsInvisibleAndIncorrectHint() {
        mInteraction.setHint(mFirstAnswerField, "blabla");
        mInteraction.setVisibility(mFirstAnswerField, View.INVISIBLE);
        assertEquals("Incorrect hint and visibility should count as one violation", 1,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    /**
     * An EditText widget exists to enter tags. This widget has its hint set to
     * "Type in the question's tags".
     */
    public void testE2B3QuestionTagsHint() {
        mInteraction.setHint(mTagsField, "blabla");
        assertEquals("Bad number of violations", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    /**
     * The widget has its visibility property set to VISIBLE.
     */
    public void testE2B3QuestionTagsVisible() {
        mInteraction.setVisibility(mTagsField, View.INVISIBLE);
        assertEquals("Bad number of violations", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testE2B3QuestionTagsInvisibleAndIncorrectHint() {
        mInteraction.setHint(mTagsField, "blabla");
        mInteraction.setVisibility(mTagsField, View.INVISIBLE);
        assertEquals("Incorrect hint and visibility should count as one violation", 1,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testBadQuestionHintBadAnswerVisibility() {
        mInteraction.setHint(mQuestionField, "blabla");
        mInteraction.setVisibility(mFirstAnswerField, View.INVISIBLE);
        assertEquals("Bad number of violations", 2, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testBadQuestionHintBadAnswerHint() {
        mInteraction.setHint(mQuestionField, "blabla");
        mInteraction.setHint(mFirstAnswerField, "blabla");
        assertEquals("Bad number of violations", 2, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testBadQuestionVisibilityBadAnswerVisibility() {
        mInteraction.setVisibility(mQuestionField, View.INVISIBLE);
        mInteraction.setVisibility(mFirstAnswerField, View.INVISIBLE);
        assertEquals("Bad number of violations", 2, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testBadQuestionHintBadAnswerHintBadTagsHint() {
        mInteraction.setHint(mQuestionField, "blabla");
        mInteraction.setHint(mFirstAnswerField, "blabla");
        mInteraction.setHint(mTagsField, "blabla");
        assertEquals("Bad number of violations", MAX_NB_PROPERTIES, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testBadQuestionVisibilityBadAnswerHintBadTagsHint() {
        mInteraction.setVisibility(mQuestionField, View.INVISIBLE);
        mInteraction.setHint(mFirstAnswerField, "blabla");
        mInteraction.setHint(mTagsField, "blabla");
        assertEquals("Bad number of violations", MAX_NB_PROPERTIES, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testBadQuestionVisibilityBadAnswerHintBadTagsVisibility() {
        mInteraction.setVisibility(mQuestionField, View.INVISIBLE);
        mInteraction.setHint(mFirstAnswerField, "blabla");
        mInteraction.setVisibility(mTagsField, View.INVISIBLE);
        assertEquals("Bad number of violations", MAX_NB_PROPERTIES, CommonEditQuestion.invokeAuditErrors(mActivity));
    }
}
