package com.csabacsete.spotifystreamer.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.csabacsete.spotifystreamer.models.SimpleTrack;
import com.csabacsete.spotifystreamer.utils.Constants;

import java.io.IOException;

import kaaes.spotify.webapi.android.models.Track;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer mMediaPlayer = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION_PLAY)) {
            SimpleTrack t = intent.getParcelableExtra(Constants.INTENT_EXTRA_TRACK);

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(t.getPreviewUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    /**
     * Called when MediaPlayer is ready
     */
    public void onPrepared(MediaPlayer player) {
        player.start();
    }
}