package ml.addy.sunshine;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ml.addy.sunshine.data.WeatherContract;


/**
 * A ForecastFragment fragment containing a FrameLayout
 * Gets loaded into activity_main.xml's FrameLayout with id "container"
 *
 * i.e. The first thing the user sees when the app is started
 */

public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    private final String LOG_TAG = "TEST/" + ForecastFragment.class.getSimpleName();

    // ArrayAdapter holding all the forecast data
    private ForecastAdapter mForecastAdapter;

    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private boolean mUseTodayLayout;

    private static final String SELECTED_KEY = "selected_position";

    private static final int FORECAST_LOADER = 0;

    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public ForecastFragment() {
        Log.v(LOG_TAG, "ForecastFragment()");
    }

    // When the fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Use this only for non UI fragments
        // setRetainInstance(true);

        // Add this line in order for this fragment to handle menu events.
        // to indicate that we want callbacks for the options menu methods
        setHasOptionsMenu(true);
    }

    // The activity is about to become visible
    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart");
        // Removed to save network resources:
        // updateWeather();
    }

    // The activity has become visible (it is now "resumed")
    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume");
    }

    // Another activity is taking focus (this activity is about to be "paused")
    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG, "onPause");
    }

    // The activity is no longer visible (it is now "stopped")
    @Override
    public void onStop() {
        super.onStop();
        Log.v(LOG_TAG, "onStop");
    }

    // The activity is about to be destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy");
    }

    // When the options menu list is expanded, inflate it
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v(LOG_TAG, "onCreateOptionsMenu");
        // Inflate the menu defined in forecastfragment.xml
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    // When an option menu item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(LOG_TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            // Update the weather data
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // When the UI gets initialized
    // Inflate and return the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");

        // The ForecastAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);

        // Reference XML layout resource (fragment_main.xml)
        // and inflate the fragment into the container in MainActivity
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView in fragment_main.xml
        mListView = (ListView) rootView.findViewById(R.id.listview_forecast);

        // Attach this adapter (mForecastAdapter, the weather data ForecastAdapter) to it
        mListView.setAdapter(mForecastAdapter);

        // Setup an OnClick Listener for when a specific forecast item is pressed
        // We'll call our MainActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String locationSetting = Utility.getPreferredLocation(getActivity());
                    // Notify the callback with the Uri for the location and date
                    ((Callback) getActivity())
                            .onItemSelected(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                    locationSetting, cursor.getLong(COL_WEATHER_DATE)
                            ));
                }
                mPosition = position;
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);

        // Return the root view of the fragment
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onActivityCreated");

        // This will make sure that the location chosen actually exists in the database
        // and if it doesn't exist, it will be added
        // - Such as when:
        //    * Starting the app for the very first time
        //    * Changing location setting when the app's onDestroy() gets called
        // Basically whenever the app first loads (or the activity is first created),
        // if the location doesn't exist, we need to add it
        String locationSetting = Utility.getPreferredLocation(getActivity());
        Cursor locationCursor = getActivity().getContentResolver().query(
                WeatherContract.LocationEntry.CONTENT_URI,
                new String[]{WeatherContract.LocationEntry._ID},
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null);

        if (locationCursor.moveToFirst()) {
            // Location exists. Good; do nothing.
            Log.v(LOG_TAG, " - onActivityCreated: LOCATION EXISTS");
        } else {
            // Need to updateWeather() to query with the new location
            Log.v(LOG_TAG, " - onActivityCreated: LOATION DOES NOT EXIST, updating location");
            updateWeather();
        }
        locationCursor.close();

        // Create the Loader
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    // Since we read the location when we create the loader, all we need to do is restart things
    void onLocationChanged( ) {
        Log.v(LOG_TAG, "onLocationChanged");
        // Update the weather with the new location
        updateWeather();
        // Restart the Loader so it loads with the new location
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }

    // Update the weather data by calling FetchWeatherTask with the location from preferences
    private void updateWeather() {
        Log.v(LOG_TAG, "updateWeather");
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPreferredLocation(getActivity());

        weatherTask.execute(location);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader: id=" + Integer.toString(i));

        // This is called when a new Loader needs to be created.  This
        // fragment only uses one loader, so we don't care about checking the id.

        // To only show current and future dates, filter the query to return weather only for
        // dates after or including today.

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        String locationSetting = Utility.getPreferredLocation(getActivity());

        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Log.v(LOG_TAG, " - onCreateLoader: cursor: " + weatherForLocationUri.toString());

        // ========================================================================
        // Only necessary for logging
        Cursor locationCursor = getActivity().getContentResolver().query(
                WeatherContract.LocationEntry.CONTENT_URI,
                new String[]{WeatherContract.LocationEntry._ID},
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null);

        if (locationCursor.moveToFirst()) {
            Log.v(LOG_TAG, " - onCreateLoader: LOCATION EXISTS");
        } else {
            Log.v(LOG_TAG, " - onCreateLoader: LOATION DOES NOT EXIST");
        }
        locationCursor.close();
        // ========================================================================

        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "onLoadFinished");
        mForecastAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(LOG_TAG, "onLoaderReset");
        mForecastAdapter.swapCursor(null);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }
}