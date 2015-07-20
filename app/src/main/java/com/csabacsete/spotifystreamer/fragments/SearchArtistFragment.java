package com.csabacsete.spotifystreamer.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.csabacsete.spotifystreamer.R;
import com.csabacsete.spotifystreamer.adapters.ArtistsAdapter;
import com.csabacsete.spotifystreamer.models.SimpleArtist;
import com.csabacsete.spotifystreamer.utils.Constants;
import com.csabacsete.spotifystreamer.utils.Utils;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;

public class SearchArtistFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private ArrayList<SimpleArtist> mContent = new ArrayList<>();
    private ArtistsAdapter mAdapter;
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sArtistClickCallback;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(SimpleArtist a);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sArtistClickCallback = new Callbacks() {
        @Override
        public void onItemSelected(SimpleArtist a) {
        }
    };

    public SearchArtistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sArtistClickCallback;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.BUNDLE_KEY_ARTISTS, mContent);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mContent = savedInstanceState.getParcelableArrayList(Constants.BUNDLE_KEY_ARTISTS);
        }

        setEmptyText(getString(R.string.search_spotify));

//        mAdapter = new ArtistsAdapter(getActivity(), android.R.layout.simple_list_item_activated_1, mContent);
        mAdapter = new ArtistsAdapter(getActivity(), R.layout.list_item_icon_and_title, mContent);
        setListAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (Utils.isDataConnectionAvailable(getActivity())) {
                    new SearchArtistsAsyncTask().execute(query);
                    searchMenuItem.collapseActionView();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(mAdapter.getItem(position));
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private class SearchArtistsAsyncTask extends AsyncTask<String, Void, ArrayList<SimpleArtist>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setListShownNoAnimation(false);
        }

        @Override
        protected ArrayList<SimpleArtist> doInBackground(String... params) {
            ArrayList<SimpleArtist> artists;
            try {
                ArtistsPager artistsPager = Utils.getSpotifyApi().searchArtists(params[0]);
                artists = Utils.buildSimpleArtistArrayList(artistsPager.artists.items);
            } catch (RetrofitError error) {
                return null;
            }
            return artists;
        }

        protected void onPostExecute(ArrayList<SimpleArtist> artistList) {
            setListShown(true);
            if (artistList != null) {
                mContent = artistList;
                mAdapter.clear();
                if (!artistList.isEmpty()) {
                    mAdapter.addAll(artistList);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.artist_not_found_check_typo), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.unexpected_network_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
