package ml.addy.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecastStr;

    public DetailFragment() {
        // Set this flag so that onCreateOptionsMenu() is called
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Reference XML layout resource (fragment_detail.xml)
        // and inflate the fragment into the container in DetailActivity
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The DetailActivity called via Intent
        Intent intent = getActivity().getIntent();

        // Inspect the Intent for forecast data
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            // Get the forecast string sent from the Intent
            mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);

            // Set the detail_text TextView in fragment_detail.xml to the forecast string
            // to display it in DetailActivity
            ((TextView) rootView.findViewById(R.id.detail_text))
                    .setText(mForecastStr);
        }

        // Return the root view of the fragment
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Includes share button fro detailfragment.xml
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    // Create an Intent to share the weather forecast
    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        // Flag to return to this application afterwards
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
}