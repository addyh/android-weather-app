package ml.addy.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

// The DetailActivity is shown when a forecast day is pressed to show more detail on it
public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "TEST/" + DetailActivity.class.getSimpleName();

    public DetailActivity() {
        Log.v(LOG_TAG, "DetailActivity()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        // Set the view to the layout in activity_detail.xml
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction
            Bundle arguments = new Bundle();
            // Read the data URI from the incoming intent
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());
            // Use the URI as the arguments in the DetailFragment
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            // Dynamically add the fragment to the container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.weather_detail_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(LOG_TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(LOG_TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Create an Intent to launch SettingsActivity
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}