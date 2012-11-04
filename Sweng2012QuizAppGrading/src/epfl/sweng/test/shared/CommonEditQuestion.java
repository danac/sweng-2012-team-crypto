package epfl.sweng.test.shared;

import epfl.sweng.editquestions.EditQuestionActivity;

/**
 * Common stuff shared between tests (constants, etc)
 * 
 * @author vitaly
 * 
 */
public class CommonEditQuestion {

    public static final String QUESTION_TEXT = "What is the answer to life, the universe, and everything?";
    public static final String FIRST_ANSWER_TEXT = "Forty-two";
    public static final String SECOND_ANSWER_TEXT = "Twenty-seven";
    public static final String FIRST_TAG_TEXT = "h2g2";
    public static final String SECOND_TAG_TEXT = "trivia";

    public static final String SUBMIT_TEXT = "Submit";
    public static final String CORRECT_ANSWER_TEXT = "✔";
    public static final String INCORRECT_ANSWER_TEXT = "✘";
    public static final String REMOVE_ANSWER_TEXT = "-";
    public static final String ADD_ANSWER_TEXT = "+";
    public static final String TAGS_HINT = "Type in the question's tags";
    public static final String ANSWER_HINT = "Type in the answer";
    public static final String QUESTION_HINT = "Type in the question's text body";

    /**
     * Invoke the audit method on the GUI thread
     */
    public static int invokeAuditErrors(EditQuestionActivity activity) {
        AuditInvoker invoker = new AuditInvoker(activity);
        activity.runOnUiThread(invoker);
        invoker.waitForCompletion();
        return invoker.getErrors();
    }

}
