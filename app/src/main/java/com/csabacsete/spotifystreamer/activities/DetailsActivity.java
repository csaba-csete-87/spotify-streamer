package com.csabacsete.spotifystreamer.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.csabacsete.spotifystreamer.R;
import com.csabacsete.spotifystreamer.utils.Constants;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setSubtitle(getIntent().getStringExtra(Constants.INTENT_EXTRA_ARTIST_NAME));
        }
    }

}
