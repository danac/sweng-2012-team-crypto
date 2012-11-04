package epfl.sweng.test.exam1;

import java.lang.reflect.Method;

import android.test.ActivityInstrumentationTestCase2;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.test.shared.AuditInvoker;

/**
 * Testing Exercise 1.
 * 
 * Requirements stated:
 * "You will get the 10 points if your app compiles on Jenkins and we can call your audit method"
 * 
 * In some cases, the auditErrors() method was misspelled, causing compilation
 * error. To give points for the rest of the exercises, we use reflection to
 * still be able to call whatever audit method was implemented.
 */
public class Ex1AuditTest extends ActivityInstrumentationTestCase2<EditQuestionActivity> {

    public Ex1AuditTest() {
        super(EditQuestionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Only check for the presence of the audit method here. Don't touch the
     * activity here because onCreate might crash.
     */
    public void testQuestion1AuditPresent() {
        Method method = AuditInvoker.getNormalAuditMethod("auditErrors");
        assertNotNull("Could not find auditErrors() method", method);
    }
}
