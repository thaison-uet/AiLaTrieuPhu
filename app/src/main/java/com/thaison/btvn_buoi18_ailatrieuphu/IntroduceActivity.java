package com.thaison.btvn_buoi18_ailatrieuphu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Shin on 9/6/2016.
 */
public class IntroduceActivity extends Activity implements Runnable, View.OnClickListener, IReadyDialog {
    private static final String TAG = IntroduceActivity.class.getSimpleName();
    private static final int UPDATE_BACKGROUND = 0;
    private static final int SHOW_DIALOG = 1;

    private RelativeLayout rlBackgroundIntroduce;
    private ImageView ivBackgroundIntroduce;
    private LevelListDrawable levelListDrawable;
    private TextView tvLevel5;
    private TextView tvLevel10;
    private TextView tvLevel15;

    private Animation animation;
    private ManagerMedia managerMedia;
    private boolean isRelease = false;
    private Thread thread;
    private Handler handler;
    private long startTime;
    private boolean isSkipped;
    private Intent intent;
    private ReadyDialog readyDialog;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        initViews();
        initComponents();
        createMedia();
    }

    private void initViews() {
        findViewById(R.id.tv_skip).setOnClickListener(this);
        rlBackgroundIntroduce = (RelativeLayout) findViewById(R.id.rl_background_introduce);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_to_right_background);
        rlBackgroundIntroduce.setAnimation(animation);
        animation.start();
        isSkipped = false;
        thread = new Thread(this);
        thread.start();
        startTime = System.currentTimeMillis();
//        ivBackgroundIntroduce = (ImageView) findViewById(R.id.iv_background_introduce);

        tvLevel5 = (TextView) findViewById(R.id.tv_level_5);
        tvLevel10 = (TextView) findViewById(R.id.tv_level_10);
        tvLevel15 = (TextView) findViewById(R.id.tv_level_15);

//        levelListDrawable = (LevelListDrawable) ivBackgroundIntroduce.getDrawable();
//        levelListDrawable.setLevel(0);
    }

    private void initComponents() {
        readyDialog = new ReadyDialog(this);
        readyDialog.setIReadyDialog(this);
        readyDialog.setCanceledOnTouchOutside(false);

        managerMedia = new ManagerMedia(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_BACKGROUND:
                        int number = msg.arg1;
//                        levelListDrawable.setLevel(number);
                        switch (number) {
                            case 5:
                                tvLevel5.setBackgroundColor(Color.BLUE);
                                break;
                            case 10:
                                tvLevel10.setBackgroundColor(Color.BLUE);
                                tvLevel5.setBackgroundColor(Color.parseColor("#00000000"));
                                break;
                            case 15:
                                tvLevel15.setBackgroundColor(Color.BLUE);
                                tvLevel5.setBackgroundColor(Color.parseColor("#00000000"));
                                tvLevel10.setBackgroundColor(Color.parseColor("#00000000"));
                                break;
                        }
                        break;
                    case SHOW_DIALOG:
                        readyDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        readyDialog.show();
                        random = new Random();
                        int audio = random.nextInt(2);
                        if (audio % 2 == 0) {
                            managerMedia.openMedia(R.raw.ready_b, false);
                        } else {
                            managerMedia.openMedia(R.raw.ready_c, false);
                        }
                        managerMedia.play();
                        break;
                }
            }
        };
    }

    private void createMedia() {
        managerMedia = new ManagerMedia(this);
        managerMedia.openMedia(R.raw.luatchoi_c, false);
        managerMedia.play();
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - startTime <= 6000 && isSkipped == false) {
            if (System.currentTimeMillis() - startTime == 4500) {
                Message message = new Message();
                message.what = UPDATE_BACKGROUND;
                message.arg1 = 5;
                message.setTarget(handler);
                message.sendToTarget();
            }
            if (System.currentTimeMillis() - startTime == 4800) {
                Message message = new Message();
                message.what = UPDATE_BACKGROUND;
                message.arg1 = 10;
                message.setTarget(handler);
                message.sendToTarget();
            }
            if (System.currentTimeMillis() - startTime == 5200) {
                Message message = new Message();
                message.what = UPDATE_BACKGROUND;
                message.arg1 = 15;
                message.setTarget(handler);
                message.sendToTarget();
            }
        }

        Message message = new Message();
        message.what = SHOW_DIALOG;
        message.setTarget(handler);
        message.sendToTarget();
    }

    @Override
    public void onBackPressed() {
        managerMedia.stopMedia();
        managerMedia.release();
        isRelease = true;
        super.onBackPressed();
        managerMedia = new ManagerMedia(this);
        managerMedia.openMedia(R.raw.bgmusic, true);
        managerMedia.play();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
                managerMedia.stopMedia();
                managerMedia.openMedia(R.raw.touch_sound, false);
                managerMedia.play();

                isSkipped = true;

                readyDialog.show();
                managerMedia = new ManagerMedia(this);
                readyDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                readyDialog.show();
                random = new Random();
                int audio = random.nextInt(2);
                if (audio % 2 == 0) {
                    managerMedia.openMedia(R.raw.ready_b, false);
                } else {
                    managerMedia.openMedia(R.raw.ready_c, false);
                }
                managerMedia.play();

                break;
        }
    }

    @Override
    protected void onResume() {
        if (!isRelease) {
            managerMedia.setVolume(1, 1);
            super.onResume();
        } else {
            super.onResume();
        }
    }

    @Override
    protected void onStop() {
        if (!isRelease) {
            managerMedia.setVolume(0, 0);
            super.onStop();
        } else {
            super.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        managerMedia.stopMedia();
        managerMedia.release();
        super.onDestroy();
    }

    @Override
    public void setReady() {
        intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
    }

    @Override
    public void setBackToMenu() {
//        onBackPressed();
        intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
