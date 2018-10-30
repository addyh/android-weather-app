package ml.addy.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {

    public DetailFragment() {
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
            String forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);

            // Set the detail_text TextView in fragment_detail.xml to the forecast string
            // to display it in DetailActivity
            ((TextView) rootView.findViewById(R.id.detail_text))
                    .setText(forecastStr);
        }

        // Return the root view of the fragment
        return rootView;
    }
}