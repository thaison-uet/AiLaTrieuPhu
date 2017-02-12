package com.thaison.btvn_buoi18_ailatrieuphu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Animation animationSunRayRotate;
    private ImageView ivSunRay;
    private Intent intent;
    private ManagerMedia managerMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initViews();
        createMedia();
    }

    private void initViews() {
        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_high_score).setOnClickListener(this);
        ivSunRay = (ImageView) findViewById(R.id.iv_sun_ray);
        animationSunRayRotate = AnimationUtils.loadAnimation(this, R.anim.rotate_sun_ray);
        ivSunRay.setAnimation(animationSunRayRotate);
        animationSunRayRotate.start();
    }

    private void createMedia() {
        managerMedia = new ManagerMedia(this);
        managerMedia.openMedia(R.raw.bgmusic, true);
        managerMedia.play();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                managerMedia.stopMedia();
                managerMedia.release();
                managerMedia.openMedia(R.raw.touch_sound, false);
                managerMedia.play();
                intent = new Intent(this, IntroduceActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_high_score:
                managerMedia.stopMedia();
                managerMedia.release();
                managerMedia.openMedia(R.raw.touch_sound, false);
                managerMedia.play();
                break;
        }
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
