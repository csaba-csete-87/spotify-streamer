package com.csabacsete.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csabacsete.spotifystreamer.R;
import com.csabacsete.spotifystreamer.models.SimpleArtist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArtistsAdapter extends ArrayAdapter<SimpleArtist> {

    private final int mResource;
    private final Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<SimpleArtist> mArtists;

    public ArtistsAdapter(Context context, int resource, ArrayList<SimpleArtist> artists) {
        super(context, resource, artists);
        mResource = resource;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mArtists = artists;
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

        SimpleArtist a = mArtists.get(position);
        holder.title.setText(a.getName());

        if (a.getUrl() != null) {
            Picasso.with(mContext)
                    .load(a.getUrl())
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
        if (mArtists != null) {
            return mArtists.size();
        }
        return 0;
    }

    static class ViewHolder {
        @InjectView(R.id.icon)
        ImageView icon;

        @InjectView(R.id.title)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
