package epfl.sweng.entry;

import epfl.sweng.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * Main Activity of the Application
 *
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
