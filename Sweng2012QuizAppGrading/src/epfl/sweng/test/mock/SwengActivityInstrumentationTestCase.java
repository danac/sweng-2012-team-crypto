package epfl.sweng.test.mock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.robotium.RobustSolo;
import epfl.sweng.test.robotium.SoloInteraction;

/**
 * A base class for SwEng tests. Provides a default setup with robotium and a
 * FixtureHttpClient.
 * 
 * @param <T>
 *            The type of the activity to test
 */
public class SwengActivityInstrumentationTestCase<T extends Activity> extends ActivityInstrumentationTestCase2<T> {

    // The name of preferences, as stated in the homework assignment.
    // Duplicated here so that we don't depend on constants in the students'
    // programs.
    protected static final String USER_SESSION_PREFERENCES = "user_session";

    protected RobustSolo solo;
    protected MockHttpClient httpClient;

    protected SharedPreferences preferences;
    protected SoloInteraction interaction;

    public SwengActivityInstrumentationTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context app = getInstrumentation().getTargetContext();
        preferences = app.getSharedPreferences(USER_SESSION_PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();

        httpClient = new FixtureHttpClient(getInstrumentation().getContext());
        SwengHttpClientFactory.setInstance(httpClient);

        solo = new RobustSolo(getInstrumentation());

        interaction = new SoloInteraction(solo.solo(), this);
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
