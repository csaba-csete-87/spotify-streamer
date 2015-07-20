package com.csabacsete.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csabacsete.spotifystreamer.R;
import com.csabacsete.spotifystreamer.models.SimpleTrack;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TracksAdapter extends ArrayAdapter<SimpleTrack> {

    private final int mResource;
    private final Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SimpleTrack> mTracks;

    public TracksAdapter(Context context, int resource, ArrayList<SimpleTrack> tracks) {
        super(context, resource, tracks);
        mResource = resource;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTracks = tracks;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(mResource, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        SimpleTrack t = mTracks.get(position);

        holder.title.setText(t.getName());
        holder.description.setText(t.getAlbumName());

        if (t.getAlbumArt() != null) {
            Picasso.with(mContext)
                    .load(t.getAlbumArt())
                    .placeholder(R.mipmap.ic_spotify)
                    .error(R.mipmap.ic_spotify)
                    .into(holder.icon);
        } else {
            Picasso.with(mContext)
                    .load(R.mipmap.ic_spotify)
                    .into(holder.icon);
        }

        return view;
    }

    @Override
    public int getCount() {
        if (mTracks != null) {
            return mTracks.size();
        }
        return 0;
    }

    static class ViewHolder {
        @InjectView(R.id.icon)
        ImageView icon;

        @InjectView(R.id.title)
        TextView title;

        @InjectView(R.id.description)
        TextView description;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
