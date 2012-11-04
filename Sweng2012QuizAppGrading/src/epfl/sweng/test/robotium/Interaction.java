package epfl.sweng.test.robotium;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

/**
 * Command Design Pattern
 * 
 * @author slv
 * 
 * @param <T>
 */
public abstract class Interaction<T> {

    private final ActivityInstrumentationTestCase2<?> mTest;

    public Interaction(ActivityInstrumentationTestCase2<?> test) {
        mTest = test;
    }

    public void interact(T withWhat) {
        mTest.getInstrumentation().waitForIdleSync();
        int tries = 2;
        junit.framework.AssertionFailedError exception = null;
        while (tries > 0) {
            try {
                doTheClick(withWhat);
                return;
            } catch (junit.framework.AssertionFailedError e) {
                exception = e;
                Log.e("ROBOTIUM", getClass().getName() + " Error: " + e.getMessage());
                tries--;
            }
        }
        ActivityInstrumentationTestCase2.assertTrue(exception.getMessage(), false);
    }

    public abstract void doTheClick(T withWhat);
}
