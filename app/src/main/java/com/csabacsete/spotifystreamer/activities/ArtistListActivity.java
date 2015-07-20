package com.csabacsete.spotifystreamer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.csabacsete.spotifystreamer.R;
import com.csabacsete.spotifystreamer.fragments.SearchArtistFragment;
import com.csabacsete.spotifystreamer.fragments.TopTracksFragment;
import com.csabacsete.spotifystreamer.models.SimpleArtist;
import com.csabacsete.spotifystreamer.utils.Constants;

/**
 * An activity representing a list of Artists. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ArtistDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ArtistListFragment} and the item details
 * (if present) is a {@link ArtistDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link ArtistListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ArtistListActivity extends AppCompatActivity implements SearchArtistFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);

        if (findViewById(R.id.artist_tracks_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((SearchArtistFragment) getFragmentManager()
                    .findFragmentById(R.id.artist_list))
                    .setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link ArtistListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(SimpleArtist artist) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(Constants.INTENT_EXTRA_ARTIST_ID, artist.getId());
            arguments.putString(Constants.INTENT_EXTRA_ARTIST_NAME, artist.getName());
            TopTracksFragment f = new TopTracksFragment();
            f.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.artist_tracks_container, f)
                    .commit();

            ActionBar actionBar = getSupportActionBar();
            if (null != actionBar) {
                actionBar.setSubtitle(artist.getName());
            }
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent i = new Intent(this, DetailsActivity.class);
            i.putExtra(Constants.INTENT_EXTRA_ARTIST_ID, artist.getId());
            i.putExtra(Constants.INTENT_EXTRA_ARTIST_NAME, artist.getName());
            startActivity(i);
        }
    }
}
