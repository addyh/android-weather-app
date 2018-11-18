package ml.addy.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


// Main Activity, loads when app starts
public class MainActivity extends AppCompatActivity implements ForecastFragment.Callback {

    private final String LOG_TAG = "TEST/" + MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    // Are we in two-pane (wide-view) mode?
    private boolean mTwoPane;
    // Used to check if the location setting changes
    private String mLocation;

    public MainActivity() {
        Log.v(LOG_TAG, "MainActivity()");
    }

    // When app is created for the first time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mLocation = Utility.getPreferredLocation(this);

        // Set the view to the layout in activity_main.xml
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.weather_detail_container) != null) {
            Log.v(LOG_TAG, "- onCreate: two panes");
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                Log.v(LOG_TAG, " - onCreate: loading DetailFragment");
                // Load the ForecastFragment into activity_main.xml's container, FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
                Log.v(LOG_TAG, "- onCreate: DetailFragment created");
            }
        } else {
            Log.v(LOG_TAG, "- onCreate: one pane");
            mTwoPane = false;
        }
    }

    // The activity is about to become visible
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart");
    }

    // The activity has become visible (it is now "resumed")
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume");

        // Location setting was changed while the app is still running:
        String location = Utility.getPreferredLocation( this );
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
            Log.v(LOG_TAG, " - onResume: location is different");
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentById(
                    R.id.fragment_forecast);
            if ( null != ff ) {
                Log.v(LOG_TAG, " - onResume: updating location");
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(
                    DETAILFRAGMENT_TAG);
            if ( null != df ) {
                df.onLocationChanged(location);
            }
            mLocation = location;
        }
    }

    // Another activity is taking focus (this activity is about to be "paused")
    @Override
    protected void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "onPause");
    }

    // The activity is no longer visible (it is now "stopped")
    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop");
    }

    // The activity is about to be destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy");
    }

    // When the options menu list is expanded, inflate it
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(LOG_TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // When an option menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(LOG_TAG, "onOptionsItemSelected");
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

        String location = Utility.getPreferredLocation(this);

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

    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            // Create a DetaiFragment and put the URI inside the arguments bundle
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            // Replace the existing DetailFragment in weather_detail_container using tag
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            // Create a new intent to launch the DetailActivity, and set Data URI
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }
}