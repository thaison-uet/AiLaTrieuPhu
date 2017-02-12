package com.thaison.btvn_buoi18_ailatrieuphu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Shin on 9/6/2016.
 */
public class LoadingActivity extends Activity implements Runnable {
    private RelativeLayout rlLoadingActivity;
    private ImageView ivLoading;
    private Animation animation;

    private long startTime;
    private ManagerMedia managerMedia;
    private Thread thread;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initViews();
        startTime = System.currentTimeMillis();
        thread = new Thread(this);
        thread.start();
    }

    private void initViews() {
        rlLoadingActivity = (RelativeLayout) findViewById(R.id.rl_loading_activity);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_to_right_background);
        rlLoadingActivity.setAnimation(animation);
        animation.start();

        ivLoading = (ImageView) findViewById(R.id.iv_loading);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading);
        ivLoading.setAnimation(animation);
        animation.start();
    }

    private void createMedia() {
        managerMedia = new ManagerMedia(this);
        managerMedia.openMedia(R.raw.gofind, false);
        managerMedia.play();
    }

    @Override
    public void run() {
        while (true) {
            if (System.currentTimeMillis() - startTime == 1000) {
                createMedia();
            }
            if (System.currentTimeMillis() - startTime == 5000) {
                intent = new Intent(this, ListLevelActivity.class);
                bundle = new Bundle();
                bundle.putInt("level", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        if (null != managerMedia) {
            managerMedia.setVolume(1, 1);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (managerMedia != null) {
            managerMedia.setVolume(0, 0);
        }
        super.onStop();
    }
//
    @Override
    protected void onDestroy() {
        managerMedia.stopMedia();
        managerMedia.release();
        super.onDestroy();
    }
}
