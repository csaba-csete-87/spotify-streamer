package com.csabacsete.spotifystreamer.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleArtist implements Parcelable {

    private String id;

    private String name;

    private String url;

    public SimpleArtist(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    protected SimpleArtist(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SimpleArtist> CREATOR = new Parcelable.Creator<SimpleArtist>() {
        @Override
        public SimpleArtist createFromParcel(Parcel in) {
            return new SimpleArtist(in);
        }

        @Override
        public SimpleArtist[] newArray(int size) {
            return new SimpleArtist[size];
        }
    };
}