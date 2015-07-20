package com.csabacsete.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleTrack implements Parcelable {

    private String name;

    private String albumName;

    private String albumArt;

    private String previewUrl;

    public SimpleTrack(String name, String albumName, String albumArt, String previewUrl) {
        this.name = name;
        this.albumName = albumName;
        this.albumArt = albumArt;
        this.previewUrl = previewUrl;
    }

    public String getName() {
        return name;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    protected SimpleTrack(Parcel in) {
        name = in.readString();
        albumName = in.readString();
        albumArt = in.readString();
        previewUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(albumName);
        dest.writeString(albumArt);
        dest.writeString(previewUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SimpleTrack> CREATOR = new Parcelable.Creator<SimpleTrack>() {
        @Override
        public SimpleTrack createFromParcel(Parcel in) {
            return new SimpleTrack(in);
        }

        @Override
        public SimpleTrack[] newArray(int size) {
            return new SimpleTrack[size];
        }
    };
}
