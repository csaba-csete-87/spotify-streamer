package com.csabacsete.spotifystreamer.fragments;


import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.csabacsete.spotifystreamer.R;
import com.csabacsete.spotifystreamer.models.SimpleTrack;
import com.csabacsete.spotifystreamer.utils.Constants;
import com.csabacsete.spotifystreamer.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaybackFragment extends DialogFragment {

    @InjectView(R.id.artist_name)
    TextView mArtistName;

    @InjectView(R.id.album_name)
    TextView mAlbumName;

    @InjectView(R.id.album_cover)
    ImageView mAlbumCover;

    @InjectView(R.id.track_name)
    TextView mTrackName;

    @InjectView(R.id.track_total_duration)
    TextView mTrackDuration;

    @InjectView(R.id.track_current_position)
    TextView mTrackPosition;

    @OnClick(R.id.play_pause)
    void onPlayPauseClicked(ImageButton b) {
        if (mMediaPlayer.isPlaying()) {
            pausePlayback();
            b.setImageResource(R.mipmap.ic_av_play_circle_fill);
        } else {
            startPlayback();
            b.setImageResource(R.mipmap.ic_av_pause_circle_fill);
        }
    }

    @OnClick(R.id.previous_track)
    void onPreviousClicked(ImageButton b) {
        previousTrack();
    }

    @OnClick(R.id.next_track)
    void onNextClicked(ImageButton b) {
        nextTrack();
    }

    private SimpleTrack mTrack;

    private MediaPlayer mMediaPlayer;

    public PlaybackFragment() {
        // Required empty public constructor
    }

    public static PlaybackFragment newInstance(SimpleTrack t, String artistName) {
        PlaybackFragment f = new PlaybackFragment();
        Bundle b = new Bundle();
        b.putParcelable(Constants.INTENT_EXTRA_TRACK, t);
        b.putString(Constants.INTENT_EXTRA_ARTIST_NAME, artistName);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_playback, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTrack = getArguments().getParcelable(Constants.INTENT_EXTRA_TRACK);

        getDialog().setTitle(null);
        mArtistName.setText(getArguments().getString(Constants.INTENT_EXTRA_ARTIST_NAME));
        mAlbumName.setText(mTrack.getAlbumName());
        mTrackName.setText(mTrack.getName());

        if (mTrack.getAlbumArt() != null) {
            Picasso.with(getActivity())
                    .load(mTrack.getAlbumArt())
                    .placeholder(R.mipmap.ic_spotify)
                    .error(R.mipmap.ic_spotify)
                    .into(mAlbumCover);
        } else {
            Picasso.with(getActivity())
                    .load(R.mipmap.ic_spotify)
                    .into(mAlbumCover);
        }

        String url = mTrack.getPreviewUrl();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp.getCurrentPosition() == 0) {
                    mTrackDuration.setText(Utils.getTimeStringFromMs(mp.getDuration()));
                }
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextTrack();
            }
        });

        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        super.onDismiss(dialog);
    }

    private void startPlayback() {
        mMediaPlayer.start();

    }

    private void pausePlayback() {
        mMediaPlayer.pause();
    }

    private void nextTrack() {

    }

    private void previousTrack() {

    }
}
