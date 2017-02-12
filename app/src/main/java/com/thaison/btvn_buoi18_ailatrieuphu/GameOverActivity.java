package com.thaison.btvn_buoi18_ailatrieuphu;

import android.app.Activity;
import android.os.Bundle;

import java.util.Random;

/**
 * Created by Shin on 9/9/2016.
 */
public class GameOverActivity extends Activity {
    private ManagerMedia managerMedia;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        initViews();
        initComponents();
    }

    private void initViews() {

    }

    private void initComponents() {
        managerMedia = new ManagerMedia(this);
        random = new Random();
        int choice = random.nextInt(2);
        if (choice % 2 == 0) {
            managerMedia.openMedia(R.raw.lose, false);
        } else {
            managerMedia.openMedia(R.raw.lose2, false);
        }
        managerMedia.play();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        managerMedia.setVolume(1, 1);
        super.onResume();
    }

    @Override
    protected void onStop() {
        managerMedia.setVolume(0, 0);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        managerMedia.stopMedia();
        managerMedia.release();
        super.onDestroy();
    }
}
