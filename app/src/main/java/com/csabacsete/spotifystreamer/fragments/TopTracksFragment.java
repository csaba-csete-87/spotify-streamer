package com.csabacsete.spotifystreamer.fragments;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.csabacsete.spotifystreamer.R;
import com.csabacsete.spotifystreamer.adapters.TracksAdapter;
import com.csabacsete.spotifystreamer.models.SimpleTrack;
import com.csabacsete.spotifystreamer.utils.Constants;
import com.csabacsete.spotifystreamer.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

public class TopTracksFragment extends ListFragment {

    private ArrayList<SimpleTrack> mContent;
    private TracksAdapter mAdapter;

    public TopTracksFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setDivider(null);

        if (savedInstanceState != null) {
            mContent = savedInstanceState.getParcelableArrayList(Constants.BUNDLE_KEY_TRACKS);
        } else {
            mContent = new ArrayList<>();
            String artistId = getArguments().getString(Constants.INTENT_EXTRA_ARTIST_ID);

            if (!TextUtils.isEmpty(artistId)) {
                if (Utils.isDataConnectionAvailable(getActivity())) {
                    new TopTracksAsyncTask().execute(artistId);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
                }
            } else {
                //In case the artistId is empty. Normally this should not happen.
                Toast.makeText(getActivity(), getString(R.string.unexpected_error_top_tracks), Toast.LENGTH_LONG).show();
            }
        }

        mAdapter = new TracksAdapter(getActivity(), R.layout.list_item_icon_title_and_description, mContent);
        setListAdapter(mAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.BUNDLE_KEY_TRACKS, mContent);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        SimpleTrack t = mAdapter.getItem(position);

        PlaybackFragment f = PlaybackFragment.newInstance(t, getArguments().getString(Constants.INTENT_EXTRA_ARTIST_NAME));
        f.show(getFragmentManager(), "test");
    }

    private class TopTracksAsyncTask extends AsyncTask<String, Void, ArrayList<SimpleTrack>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setListShownNoAnimation(false);
        }

        @Override
        protected ArrayList<SimpleTrack> doInBackground(String... params) {
            ArrayList<SimpleTrack> tracks;
            try {
                Map<String, Object> localeQueryParam = new HashMap<>();
                localeQueryParam.put(Constants.QUERY_PARAM_COUNTRY, Locale.getDefault().getCountry());

                Tracks tracksResponse = Utils.getSpotifyApi().getArtistTopTrack(params[0], localeQueryParam);
                tracks = Utils.buildSimpleTrackArrayList(tracksResponse.tracks);
            } catch (RetrofitError error) {
                return null;
            }
            return tracks;
        }

        @Override
        protected void onPostExecute(ArrayList<SimpleTrack> tracks) {
            setListShown(true);
            if (tracks != null) {
                mContent = tracks;
                mAdapter.clear();
                if (!tracks.isEmpty()) {
                    mAdapter.addAll(tracks);
                } else {
                    setEmptyText(getString(R.string.this_artist_has_no_top_tracks));
                }
            } else {
                Toast.makeText(getActivity(), getString(R.string.unexpected_network_error), Toast.LENGTH_LONG).show();
            }
        }
    }
}
