package ml.addy.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


// Main Activity, loads when app starts
public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    // When app is created for the first time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("TEST", "onCreate");
        super.onCreate(savedInstanceState);
        // Set the view to the layout in activity_main.xml
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            // Load the ForecastFragment into activity_main.xml's container, FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    // The activity is about to become visible
    @Override
    protected void onStart() {
        super.onStart();
        Log.v("TEST", "onStart");
    }

    // The activity has become visible (it is now "resumed")
    @Override
    protected void onResume() {
        super.onResume();
        Log.v("TEST", "onResume");
    }

    // Another activity is taking focus (this activity is about to be "paused")
    @Override
    protected void onPause() {
        super.onPause();
        Log.v("TEST", "onPause");
    }

    // The activity is no longer visible (it is now "stopped")
    @Override
    protected void onStop() {
        super.onStop();
        Log.v("TEST", "onStop");
    }

    // The activity is about to be destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("TEST", "onDestroy");
    }

    // When the options menu list is expanded, inflate it
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // When an option menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // If the settings menu item is selected
        if (id == R.id.action_settings) {
            // Create an Intent to launch SettingsActivity
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        // If map menu has been selected
        else if (id == R.id.action_map) {
            // Open maps app using intent
            openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        String location = sharedPrefs.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));

        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + location + ", no receiving apps installed!");
        }
    }
}