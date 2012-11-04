package epfl.sweng.test.shared;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

import android.util.Log;
import epfl.sweng.editquestions.EditQuestionActivity;

/**
 * This class allows to invoke the audit method on a specified thread. This
 * allows the audit method to manipulate the GUI.
 * 
 * The class also deals with cases where students didn't bother naming their
 * audit methods as indicated in the exam. In this case, it we have a list of
 * method names that we try to invoke.
 */
public class AuditInvoker implements Runnable {
    EditQuestionActivity mActivity;
    int mResult;
    Semaphore mLock;

    /* Indicate whether the activity has the required audit method */
    boolean hasCorrectAuditMethod;

    /* Indicate whether it was possible to invoke some audit method */
    boolean couldInvokeAuditMethod;

    public AuditInvoker(EditQuestionActivity activity) {
        mActivity = activity;
        mLock = new Semaphore(1);
        mLock.acquireUninterruptibly();
        hasCorrectAuditMethod = false;
        couldInvokeAuditMethod = false;
    }

    /**
     * Find the audit method with a similar signature as the one required by the
     * assignment.
     * 
     * @return The method object
     */
    public static Method getNormalAuditMethod(String name) {
        Class<?> c = EditQuestionActivity.class;

        Method m = null;

        try {
            m = c.getMethod(name);
        } catch (NoSuchMethodException e) {
            return null;
        }

        if (!m.getReturnType().isAssignableFrom(int.class)) {
            return null;
        }

        if (m.getParameterTypes().length != 0) {
            return null;
        }

        return m;
    }

    /**
     * Some put a depth argument
     * 
     * @param name
     * @return
     */
    public Method getAuditMethodWithDepth(String name) {
        Class<?> c = mActivity.getClass();

        Method m = null;

        try {
            m = c.getMethod(name, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        if (!m.getReturnType().isAssignableFrom(int.class)) {
            Log.e("AUDITINVOCATION", "Invalid return type " + m.getReturnType().getSimpleName());
            return null;
        }

        return m;
    }

    private Object invokeAuditMethod(Method m, Object... params) {
        try {
            return m.invoke(mActivity, params);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Audit method threw an exception", e);
        }
    }

    boolean invokeNormalAuditMethod(String name) {

        Method m = getNormalAuditMethod(name);
        if (m == null) {
            return false;
        }

        Object result = invokeAuditMethod(m);
        if (result == null) {
            return false;
        }

        mResult = (Integer) result;
        return true;
    }

    boolean invokeAuditMethodWithDepth(String name, int depth) {
        Method m = getAuditMethodWithDepth(name);
        if (m == null) {
            Log.e("AUDITINVOCATION", "Could not find int auditErrors(int depth)");
            return false;
        }

        Object result = invokeAuditMethod(m, depth);
        if (result == null) {
            return false;
        }

        mResult = (Integer) result;
        return true;
    }

    @Override
    public void run() {
        if (invokeNormalAuditMethod("auditErrors")) {
            hasCorrectAuditMethod = true;
            couldInvokeAuditMethod = true;
        } else if (invokeAuditMethodWithDepth("auditErrors", 1)) {
            couldInvokeAuditMethod = true;
        } else if (invokeNormalAuditMethod("auditError")) {
            couldInvokeAuditMethod = true;
        }

        mLock.release();
        return;
    }

    int getErrors() {
        if (!couldInvokeAuditMethod) {
            throw new RuntimeException("There is no audit method in " + EditQuestionActivity.class.getName()
                    + " that we can call.");
        }

        return mResult;
    }

    void waitForCompletion() {
        mLock.acquireUninterruptibly();
    }
}