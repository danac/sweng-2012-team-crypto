package epfl.sweng.test.robotium;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

public class SoloInteraction {

    private final Interaction<View> clickOnView;
    private final Interaction<String> clickOnButton;
    private final Interaction<String> clickOnText;
    private final ActivityInstrumentationTestCase2<?> mTest;
    private final Solo mSolo;

    public SoloInteraction(final Solo solo, ActivityInstrumentationTestCase2<?> test) {
        clickOnView = new Interaction<View>(test) {
            @Override
            public void doTheClick(View withWhat) {
                solo.clickOnView(withWhat);
            }
        };

        clickOnButton = new Interaction<String>(test) {

            @Override
            public void doTheClick(String withWhat) {
                solo.clickOnButton(withWhat);
            }
        };

        clickOnText = new Interaction<String>(test) {

            @Override
            public void doTheClick(String withWhat) {
                solo.clickOnText(withWhat);
            }
        };

        mTest = test;
        mSolo = solo;
    }

    /**
     * Returns the n-th button (counting starts from 0) containing the given
     * text
     */
    public Button getNthButton(String text, int n) {
        Pattern pattern = Pattern.compile(text);
        int toFind = n + 1;

        for (Button b : mSolo.getCurrentButtons()) {
            Matcher matcher = pattern.matcher(b.getText());
            if (matcher.find()) {
                toFind -= 1;
                if (toFind == 0) {
                    return b;
                }
            }
        }

        ActivityInstrumentationTestCase2.fail("Could not find " + (n + 1) + " buttons with text \"" + text
                + "\" (only " + (n + 1 - toFind) + " found)");
        return null;
    }

    /**
     * Also works for invisible buttons
     */
    public Button getNthButtonSafe(String text, int n) {
        Pattern pattern = Pattern.compile(text);
        int toFind = n + 1;

        ArrayList<View> views = mSolo.getViews();
        for (View v : views) {
            if (v instanceof Button) {
                Button b = (Button) v;
                // Log.i("VIEW", b.getText().toString());
                Matcher matcher = pattern.matcher(b.getText());
                if (matcher.find()) {
                    toFind -= 1;
                    if (toFind == 0) {
                        return b;
                    }
                }
            }
        }

        ActivityInstrumentationTestCase2.fail("Could not find " + (n + 1) + " buttons with text \"" + text
                + "\" (only " + (n + 1 - toFind) + " found)");
        return null;
    }

    /**
     * Prints a list of buttons in the view. Useful for debugging.
     */
    public void printButtonsInView() {
        ArrayList<View> views = mSolo.getViews();
        for (View v : views) {
            if (v instanceof Button) {
                Button b = (Button) v;
                Log.i("BUTTON", b.getText().toString());
            }
        }
    }

    public void clickOnText(String text) {
        clickOnText.interact(text);
    }

    public void clickOnButton(String text) {
        clickOnButton.interact(text);
    }

    public void clickOnView(View view) {
        clickOnView.interact(view);
    }

    public EditText getHintedField(String hint) {
        clickOnText(Pattern.quote(hint));
        return (EditText) mSolo.getCurrentActivity().getCurrentFocus();
    }

    public void enterTextIntoHintedField(String hint, String text) {
        EditText field = getHintedField(hint);
        mSolo.enterText(field, text);
    }

    public void setHint(final EditText edit, final String text) {
        try {
            mTest.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    edit.setHint(text);
                }
            });
        } catch (Throwable e) {

            e.printStackTrace();
        }
        mTest.getInstrumentation().waitForIdleSync();
    }

    public void setVisibility(final View view, final int visibility) {
        try {
            mTest.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(visibility);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mTest.getInstrumentation().waitForIdleSync();
    }

    public void setButtonText(final Button b, final String text) {
        try {
            mTest.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    b.setText(text);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mTest.getInstrumentation().waitForIdleSync();
    }

    public void setButtonEnabled(final Button b, final boolean enabled) {
        try {
            mTest.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    b.setEnabled(enabled);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mTest.getInstrumentation().waitForIdleSync();
    }

}
