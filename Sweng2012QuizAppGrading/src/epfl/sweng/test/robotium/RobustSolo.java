package epfl.sweng.test.robotium;

import java.lang.reflect.InvocationTargetException;

import junit.framework.AssertionFailedError;
import android.app.Activity;
import android.app.Instrumentation;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;

/** A version of Robotium that is more robust on slow machines */
public class RobustSolo {

    private static final int N_TRIES = 2;
    private static final int RETRY_DELAY = 1000;

    private Instrumentation instrumentation;
    private Solo solo;

    public RobustSolo(Instrumentation instrumentation) {
        this.solo = new Solo(instrumentation);
        this.instrumentation = instrumentation;
    }

    public void clickOnText(String text) {
        executeRobustly("clickOnText", text);
    }
    
    public void clickOnCheckBox(int id) {
    	executeRobustly("clickOnCheckBox", id);
    }

    private Object executeRobustly(String method, Object... args) {
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; ++i) {
        	Class<?> type = args[i].getClass();
        	if (type.equals(Integer.class)) {
        			type = Integer.TYPE;
        	}
            argTypes[i] = type;
        }

        System.out.println("argTypes[0]  = " + argTypes[0]);
        instrumentation.waitForIdleSync();

        int tries = N_TRIES;
        while (tries > 0) {
            try {
                // execute the actual command
                return solo.getClass().getMethod(method, argTypes).invoke(solo, args);
            } catch (IllegalArgumentException e) {
                AssertionFailedError err = new AssertionFailedError("Could not call method " + method);
                err.initCause(e);
                throw err;
            } catch (IllegalAccessException e) {
                AssertionFailedError err = new AssertionFailedError("Could not call method " + method);
                err.initCause(e);
                throw err;
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof AssertionFailedError) {
                    // If Robotium doesn't perform its work, retry
                    tries--;
                    if (tries == 0) {
                        throw (AssertionFailedError) e.getCause();
                    }
                    solo.sleep(RETRY_DELAY);
                } else {
                    AssertionFailedError err = new AssertionFailedError("Unexpected Robotium exception on call to "
                            + method);
                    err.initCause(e.getCause());
                    throw err;
                }
            } catch (NoSuchMethodException e) {
                AssertionFailedError err = new AssertionFailedError("Could not call method " + method);
                err.initCause(e);
                throw err;
            }
        }

        throw new AssertionFailedError("Unreachable");
    }

    public boolean searchText(String text) {
        return (Boolean) executeRobustly("searchText", text);
    }

    public Button getButton(String label) {
        return (Button) executeRobustly("getButton", label);
    }

    public boolean waitForActivity(String name) {
        return (Boolean) executeRobustly("waitForActivity", name);
    }

    public void finishOpenedActivities() {
        solo.finishOpenedActivities();
    }

    public void sleep(int millis) {
        solo.sleep(millis);
    }

    public Activity getCurrentActivity() {
        return solo.getCurrentActivity();
    }

    public Solo solo() {
        return solo;
    }
}
