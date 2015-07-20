package com.csabacsete.spotifystreamer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.csabacsete.spotifystreamer.models.SimpleArtist;
import com.csabacsete.spotifystreamer.models.SimpleTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class Utils {

    public static SpotifyService getSpotifyApi() {
        SpotifyApi api = new SpotifyApi();
        return api.getService();
    }

    public static boolean isDataConnectionAvailable(Context context) {
        boolean connectedToWifi = false;
        boolean connectedToNetwork = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo networkInfo : allNetworkInfo) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    if (networkInfo.isConnected()) {
                        connectedToWifi = true;
                    }
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    if (networkInfo.isConnected()) {
                        connectedToNetwork = true;
                    }
                    break;
            }
        }
        return connectedToWifi || connectedToNetwork;
    }

    public static ArrayList<SimpleArtist> buildSimpleArtistArrayList(List<Artist> artistList) {
        ArrayList<SimpleArtist> simpleArtistArrayList = new ArrayList<>();
        for (Artist artist : artistList) {
            List<Image> images = artist.images;

            String url = null;
            if (images != null && images.size() > 0) {
                Image i = images.get(0);
                if (!TextUtils.isEmpty(i.url)) {
                    url = i.url;
                }
            }
            SimpleArtist s = new SimpleArtist(artist.id, artist.name, url);
            simpleArtistArrayList.add(s);
        }
        return simpleArtistArrayList;
    }

    public static ArrayList<SimpleTrack> buildSimpleTrackArrayList(List<Track> trackList) {
        ArrayList<SimpleTrack> simpleTrackArrayList = new ArrayList<>();
        for (Track track : trackList) {
            List<Image> images = track.album.images;

            String url = null;
            if (images != null && images.size() > 0) {
                Image i = images.get(0);
                if (!TextUtils.isEmpty(i.url)) {
                    url = i.url;
                }
            }
            SimpleTrack t = new SimpleTrack(track.name, track.album.name, url, track.preview_url);
            simpleTrackArrayList.add(t);
        }
        return simpleTrackArrayList;
    }

    public static String getTimeStringFromMs(int milliseconds) {
        return String.format(Constants.STRING_TIME_FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }
}
