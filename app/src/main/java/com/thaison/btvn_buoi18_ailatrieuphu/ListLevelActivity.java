package com.thaison.btvn_buoi18_ailatrieuphu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * Created by Shin on 9/7/2016.
 */
public class ListLevelActivity extends Activity implements Runnable {
    private RelativeLayout rlActivityListLevel;
    private Animation animation;
    private ManagerMedia managerMedia;
    private Random random;
    private long startTime;
    private Thread thread;
    private Intent intent;
    private int level;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_level);
        initViews();
        initComponents();
    }

    private void initViews() {
        rlActivityListLevel = (RelativeLayout) findViewById(R.id.rl_activity_list_level);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_to_right_background);
        rlActivityListLevel.setAnimation(animation);
        animation.start();
    }

    private void initComponents() {
        bundle = new Bundle();
        intent = getIntent();
        bundle = intent.getExtras();
        level = bundle.getInt("level");

        startTime = System.currentTimeMillis();
        thread = new Thread(this);
        thread.start();
    }

    private void createMedia() {
        managerMedia = new ManagerMedia(this);
        random = new Random();
        int randomNumber = random.nextInt(2);
        switch (level) {
            case 1:
                if (randomNumber % 2 == 0) {
                    managerMedia.openMedia(R.raw.ques1, false);
                } else {
                    managerMedia.openMedia(R.raw.ques1_b, false);
                }
                break;

            case 2:
                if (randomNumber % 2 == 0) {
                    managerMedia.openMedia(R.raw.ques2, false);
                } else {
                    managerMedia.openMedia(R.raw.ques2_b, false);
                }
                break;

            case 3:
                if (randomNumber % 2 == 0) {
                    managerMedia.openMedia(R.raw.ques3, false);
                } else {
                    managerMedia.openMedia(R.raw.ques3_b, false);
                }
                break;

            case 4:
                if (randomNumber % 2 == 0) {
                    managerMedia.openMedia(R.raw.ques4, false);
                } else {
                    managerMedia.openMedia(R.raw.ques4_b, false);
                }
                break;

            case 5:
                if (randomNumber % 2 == 0) {
                    managerMedia.openMedia(R.raw.ques5, false);
                } else {
                    managerMedia.openMedia(R.raw.ques5_b, false);
                }
                break;

            case 6:
                managerMedia.openMedia(R.raw.ques6, false);
                break;

            case 7:
                if (randomNumber % 2 == 0) {
                    managerMedia.openMedia(R.raw.ques7, false);
                } else {
                    managerMedia.openMedia(R.raw.ques7_b, false);
                }
                break;

            case 8:
                if (randomNumber % 2 == 0) {
                    managerMedia.openMedia(R.raw.ques8, false);
                } else {
                    managerMedia.openMedia(R.raw.ques8_b, false);
                }
                break;

            case 9:
                if (randomNumber % 2 == 0) {
                    managerMedia.openMedia(R.raw.ques9, false);
                } else {
                    managerMedia.openMedia(R.raw.ques9_b, false);
                }
                break;

            case 10:
                managerMedia.openMedia(R.raw.ques10, false);
                break;

            case 11:
                managerMedia.openMedia(R.raw.ques11, false);
                break;

            case 12:
                managerMedia.openMedia(R.raw.ques12, false);
                break;

            case 13:
                managerMedia.openMedia(R.raw.ques13, false);
                break;

            case 14:
                managerMedia.openMedia(R.raw.ques14, false);
                break;

            case 15:
                managerMedia.openMedia(R.raw.ques15, false);
                break;
        }
        managerMedia.play();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - startTime <= 1900) {
            if (System.currentTimeMillis() - startTime == 800) {
                createMedia();
            }
        }

        intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }
}
