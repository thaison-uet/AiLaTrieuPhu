package com.thaison.btvn_buoi18_ailatrieuphu;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Shin on 9/6/2016.
 */
public class ManagerMedia {
    private MediaPlayer mediaPlayer;
    private Context context;

    public ManagerMedia(Context context) {
        this.context = context;
    }

    public void openMedia(int id, boolean loop) {
        release();
        mediaPlayer = MediaPlayer.create(context, id);
        if (loop) {
            mediaPlayer.setLooping(true);
        }
    }

    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stopMedia() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }


}
