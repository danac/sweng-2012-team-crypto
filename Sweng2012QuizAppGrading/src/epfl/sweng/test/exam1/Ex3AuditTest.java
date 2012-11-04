package epfl.sweng.test.exam1;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.test.robotium.SoloInteraction;
import epfl.sweng.test.shared.CommonEditQuestion;

/**
 * Checking Exercise 3.
 */
public class Ex3AuditTest extends ActivityInstrumentationTestCase2<EditQuestionActivity> {

    private static final int MAX_NB_PROPERTIES = 4;
    private static final int ANSWER_COUNT = 3;

    private Solo mSolo;
    private SoloInteraction mInteraction;
    private EditQuestionActivity mActivity;

    private Button mSubmitButton;
    private Button mAddAnswerButton;

    public Ex3AuditTest() {
        super(EditQuestionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation(), getActivity());
        mInteraction = new SoloInteraction(mSolo, this);
        mActivity = getActivity();

        mSubmitButton = mSolo.getButton(CommonEditQuestion.SUBMIT_TEXT);
        assertNotNull("Could not find button labeled " + CommonEditQuestion.SUBMIT_TEXT, mSubmitButton);

        mAddAnswerButton = mSolo.getButton(CommonEditQuestion.ADD_ANSWER_TEXT);
        assertNotNull("Could not find button labeled " + CommonEditQuestion.ADD_ANSWER_TEXT, mAddAnswerButton);

    }

    private void checkButtonVisibility(String correctLabel) {
        Button b = mSolo.getButton(correctLabel);
        assertNotNull("Could not find button labeled " + correctLabel, b);
        mInteraction.setVisibility(b, View.INVISIBLE);
        assertEquals("Did not catch invisible button", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    private void checkButtonVisibility(Button b) {
        mInteraction.setVisibility(b, View.INVISIBLE);
        assertEquals("Did not catch invisible button", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    private void checkButtonLabel(String correctLabel, String incorrectLabel) {
        Button b = mSolo.getButton(correctLabel);
        assertNotNull("Could not find button labeled " + correctLabel, b);
        mInteraction.setButtonText(b, incorrectLabel);
        assertEquals("Replaced button label '" + correctLabel + "' with '" + incorrectLabel + "' "
                + "The audit method did not catch the error", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    private void checkButtonLabel(Button b, String correctLabel, String incorrectLabel) {
        mInteraction.setButtonText(b, incorrectLabel);
        assertEquals("Replaced button label '" + correctLabel + "' with '" + incorrectLabel + "' "
                + "The audit method did not catch the error", 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    private void checkButtonLabelAndVisibility(String correctLabel, String incorrectLabel) {
        Button b = mSolo.getButton(correctLabel);
        assertNotNull("Could not find button labeled " + correctLabel, b);
        mInteraction.setButtonText(b, incorrectLabel);
        mInteraction.setVisibility(b, View.INVISIBLE);

        assertEquals("Replaced button label '" + correctLabel + "' with '" + incorrectLabel
                + "' and made it invisible." + "The audit method did not report the correct number of errors", 1,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testE3B1AddNewAnswerLabel() {
        checkButtonLabel(CommonEditQuestion.ADD_ANSWER_TEXT, "bla");
    }

    public void testE3B1AddNewAnswerVisibility() {
        checkButtonVisibility(CommonEditQuestion.ADD_ANSWER_TEXT);
    }

    public void testE3B1AddNewAnswerLabelAndVisibility() {
        checkButtonLabelAndVisibility(CommonEditQuestion.ADD_ANSWER_TEXT, "bla");
    }

    public void testE3B2SubmitLabel() {
        checkButtonLabel(CommonEditQuestion.SUBMIT_TEXT, "bla");
    }

    public void testE3B2SubmitVisibility() {
        checkButtonVisibility(CommonEditQuestion.SUBMIT_TEXT);
    }

    public void testE3B2SubmitLabelAndVisibility() {
        checkButtonLabelAndVisibility(CommonEditQuestion.SUBMIT_TEXT, "bla");
    }

    /**
     * Set of tests to check the presence of the remove button when one answer
     * is present.
     **/
    public void testE3B3RemoveAnswerLabel() {
        checkButtonLabel(CommonEditQuestion.REMOVE_ANSWER_TEXT, "bla");
    }

    public void testE3B3RemoveAnswerVisibility() {
        checkButtonVisibility(CommonEditQuestion.REMOVE_ANSWER_TEXT);
    }

    public void testE3B3RemoveAnswerLabelAndVisibility() {
        checkButtonLabelAndVisibility(CommonEditQuestion.REMOVE_ANSWER_TEXT, "bla");
    }

    private void addEmptyAnswers(int count) {
        for (int i = 0; i < count; ++i) {
            mSolo.clickOnText(CommonEditQuestion.ADD_ANSWER_TEXT);
            getInstrumentation().waitForIdleSync();
        }
    }

    /**
     * Set of tests to check the presence of the remove button when multiple
     * answers are present.
     **/
    public void testE3B3RemoveAnswerLabelMultiple() {
        addEmptyAnswers(ANSWER_COUNT);
        Button b = mInteraction.getNthButtonSafe(CommonEditQuestion.REMOVE_ANSWER_TEXT, ANSWER_COUNT);
        checkButtonLabel(b, CommonEditQuestion.REMOVE_ANSWER_TEXT, "bla");
    }

    public void testE3B3RemoveAnswerVisibilityMultiple() {
        addEmptyAnswers(ANSWER_COUNT);
        Button b = mInteraction.getNthButtonSafe(CommonEditQuestion.REMOVE_ANSWER_TEXT, ANSWER_COUNT);
        checkButtonVisibility(b);
    }

    public void testE3B4ToggleCorrectnessLabelMultiple() {
        addEmptyAnswers(ANSWER_COUNT);

        Button b = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, ANSWER_COUNT);
        mSolo.clickOnView(b);
        getInstrumentation().waitForIdleSync();

        b = mSolo.getButton(CommonEditQuestion.CORRECT_ANSWER_TEXT);
        assertNotNull("Could not find button with label " + CommonEditQuestion.CORRECT_ANSWER_TEXT);

        checkButtonLabel(b, CommonEditQuestion.CORRECT_ANSWER_TEXT, "bla");
    }

    public void testE3B4ToggleCorrectnessVisibilityMultiple() {
        addEmptyAnswers(ANSWER_COUNT);

        Button b = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 2);
        mSolo.clickOnView(b);
        getInstrumentation().waitForIdleSync();

        mInteraction.printButtonsInView();
        b = mSolo.getButton(CommonEditQuestion.CORRECT_ANSWER_TEXT);
        assertNotNull("Could not find button with label " + CommonEditQuestion.CORRECT_ANSWER_TEXT);

        checkButtonVisibility(b);
    }

    public void testB1B2BadPlusLabelBadSubmitLabel() {
        mInteraction.setButtonText(mAddAnswerButton, "bla");
        mInteraction.setButtonText(mSubmitButton, "bla");
        assertEquals("Did not catch incorrect label on plus and submit buttons", 2,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testB1B2BadPlusLabelBadVisibleSubmit() {
        mInteraction.setButtonText(mAddAnswerButton, "bla");
        mInteraction.setVisibility(mSubmitButton, View.INVISIBLE);
        assertEquals("Did not catch incorrect label on plus button and invisible submit button", 2,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testB3B4BadMinusLabelBadToggleLabel() {
        Button minus = mInteraction.getNthButtonSafe(CommonEditQuestion.REMOVE_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to remove first answer", minus);

        Button toggle = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to toggle correctness of first answer", toggle);

        mInteraction.setButtonText(minus, "bla");
        mInteraction.setButtonText(toggle, "bla");

        assertEquals("Did not catch incorrect label on remove answer and toggle correctness buttons", 2,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testB3B4BadMinusLabelBadVisibleToggle() {
        Button minus = mInteraction.getNthButtonSafe(CommonEditQuestion.REMOVE_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to remove first answer", minus);

        Button toggle = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to toggle correctness of first answer", toggle);

        mInteraction.setButtonText(minus, "bla");
        mInteraction.setVisibility(toggle, View.INVISIBLE);

        assertEquals("Did not catch incorrect label on remove answer button and invisible toggle button", 2,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testB1B2B3BadPlusLabelBadSubmitLabelBadRemoveLabel() {
        Button minus = mInteraction.getNthButtonSafe(CommonEditQuestion.REMOVE_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to remove first answer", minus);

        Button toggle = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to toggle correctness of first answer", toggle);

        mInteraction.setButtonText(mAddAnswerButton, "bla");
        mInteraction.setButtonText(minus, "bla");
        mInteraction.setButtonText(toggle, "bla");

        assertEquals("Did not catch incorrect label on add answer, remove answer, and toggle correctness buttons",
                MAX_NB_PROPERTIES - 1, CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testB1B2B3B4AllButtonsInvisible() {
        Button minus = mInteraction.getNthButtonSafe(CommonEditQuestion.REMOVE_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to remove first answer", minus);

        Button toggle = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to toggle correctness of first answer", toggle);

        mInteraction.setVisibility(mSubmitButton, View.INVISIBLE);
        mInteraction.setVisibility(mAddAnswerButton, View.INVISIBLE);
        mInteraction.setVisibility(minus, View.INVISIBLE);
        mInteraction.setVisibility(toggle, View.INVISIBLE);

        assertEquals("Did not catch all invisible buttons", MAX_NB_PROPERTIES,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

    public void testB1B2B3B4AllButtonsWithIncorrectLabel() {
        Button minus = mInteraction.getNthButtonSafe(CommonEditQuestion.REMOVE_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to remove first answer", minus);

        Button toggle = mInteraction.getNthButtonSafe(CommonEditQuestion.INCORRECT_ANSWER_TEXT, 0);
        assertNotNull("Could not find button to toggle correctness of first answer", toggle);

        mInteraction.setButtonText(mSubmitButton, "bla");
        mInteraction.setButtonText(mAddAnswerButton, "bla");
        mInteraction.setButtonText(minus, "bla");
        mInteraction.setButtonText(toggle, "bla");

        assertEquals("Did not catch incorrect labels on all buttons", MAX_NB_PROPERTIES,
                CommonEditQuestion.invokeAuditErrors(mActivity));
    }

}