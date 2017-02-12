package com.thaison.btvn_buoi18_ailatrieuphu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Shin on 9/7/2016.
 */
public class QuestionActivity extends Activity implements Runnable, IGameOverDialog, View.OnClickListener {
    private static final String TAG = QuestionActivity.class.getSimpleName();
    private static final int UPDATE_TIME = 0;
    private static final int SHOW_DIALOG_OVER_TIME = 1;
    private static final int SHOW_DIALOG_WRONG_ANSWER = 2;
    private static final int UPDATE_CHOOSE_BUTTON = 3;
    private static final int UPDATE_TRUE_BUTTON = 4;
    private static final int ANSWER_THE_QUESTION = 5;
    private static final int SHOW_LIST_LEVEL = 6;
    private static final String A = "a";
    private static final String B = "b";
    private static final String C = "c";
    private static final String D = "d";

    private RelativeLayout rlBackgroundQuestion;
    private Button btnTime;
    private TextView tvLevel;
    private TextView tvQuestion;
    private Button btnCaseA;
    private Button btnCaseB;
    private Button btnCaseC;
    private Button btnCaseD;

    private int level;
    private String question;
    private String caseA;
    private String caseB;
    private String caseC;
    private String caseD;
    private int trueCase;

    private ManagerMedia managerMedia;
    private Animation animation;
    private List<Question> questions;
    private QuestionManager questionManager;
    private boolean isPaused;
    private Thread thread;
    private Handler handler;
    private long startTime;
    private int time;

    private GameOverDialog gameOverDialog;
    private Intent intent;
    private Bundle bundle;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initViews();
        level = Integer.parseInt(tvLevel.getText().toString()
                .substring(tvLevel.getText().toString().length() - 1));
        level++;
        initComponents(level);
    }

    private void initViews() {
        rlBackgroundQuestion = (RelativeLayout) findViewById(R.id.rl_background_question);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate_to_left_background);
        rlBackgroundQuestion.setAnimation(animation);
        animation.start();

        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        btnCaseA = (Button) findViewById(R.id.btn_case_a);
        btnCaseB = (Button) findViewById(R.id.btn_case_b);
        btnCaseC = (Button) findViewById(R.id.btn_case_c);
        btnCaseD = (Button) findViewById(R.id.btn_case_d);
        btnTime = (Button) findViewById(R.id.btn_time);

        btnCaseA.setOnClickListener(this);
        btnCaseB.setOnClickListener(this);
        btnCaseC.setOnClickListener(this);
        btnCaseD.setOnClickListener(this);
    }

    private void initComponents(final int level) {
        managerMedia = new ManagerMedia(this);
        managerMedia.openMedia(R.raw.background_music, false);
        managerMedia.play();

        questions = new ArrayList<>();
        questionManager = new QuestionManager(this);
        questions = questionManager.get15Questions();

        tvLevel.setText("Câu " + questions.get(level - 1).getLevel() + "");
        tvQuestion.setText(questions.get(level - 1).getQuestion());
        btnCaseA.setText("A. " + questions.get(level - 1).getCaseA());
        btnCaseB.setText("B. " + questions.get(level - 1).getCaseB());
        btnCaseC.setText("C. " + questions.get(level - 1).getCaseC());
        btnCaseD.setText("D. " + questions.get(level - 1).getCaseD());

        gameOverDialog = new GameOverDialog(this);
        gameOverDialog.setCanceledOnTouchOutside(false);
        gameOverDialog.setIGameOverDialog(this);

        intent = new Intent(this, ListLevelActivity.class);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TIME:
                        btnTime.setText(msg.arg1 + "");
                        break;

                    case SHOW_DIALOG_OVER_TIME:
                        managerMedia.stopMedia();
                        gameOverDialog.getTvGameOverTitle().setText("Hết giờ");
                        gameOverDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        gameOverDialog.show();
                        managerMedia.openMedia(R.raw.out_of_time, false);
                        managerMedia.play();
                        break;

                    case SHOW_DIALOG_WRONG_ANSWER:
                        managerMedia.stopMedia();
//                        managerMedia.release();
                        gameOverDialog.getTvGameOverTitle().setText("Trả lời sai");
                        gameOverDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        gameOverDialog.show();
                        break;

                    case UPDATE_CHOOSE_BUTTON:
                        String answer = msg.obj + "";
                        if (answer.equals(A)) {
                            if (msg.arg1 == 1) {
                                btnCaseA.setBackgroundResource(R.drawable.bg_choose1);
                            } else {
                                btnCaseA.setBackgroundResource(R.drawable.bg_choose2);
                            }
                        } else {
                            if (answer.equals(B)) {
                                if (msg.arg1 == 1) {
                                    btnCaseB.setBackgroundResource(R.drawable.bg_choose1);
                                } else {
                                    btnCaseB.setBackgroundResource(R.drawable.bg_choose2);
                                }
                            } else {
                                if (answer.equals(C)) {
                                    if (msg.arg1 == 1) {
                                        btnCaseC.setBackgroundResource(R.drawable.bg_choose1);
                                    } else {
                                        btnCaseC.setBackgroundResource(R.drawable.bg_choose2);
                                    }
                                } else {
                                    if (msg.arg1 == 1) {
                                        btnCaseD.setBackgroundResource(R.drawable.bg_choose1);
                                    } else {
                                        btnCaseD.setBackgroundResource(R.drawable.bg_choose2);
                                    }
                                }
                            }
                        }
                        break;

                    case UPDATE_TRUE_BUTTON:
                        answer = msg.obj + "";
                        if (answer.equals(A)) {
                            if (msg.arg1 == 1) {
                                btnCaseA.setBackgroundResource(R.drawable.bg_true1);
                            } else {
                                btnCaseA.setBackgroundResource(R.drawable.bg_true2);
                            }
                        } else {
                            if (answer.equals(B)) {
                                if (msg.arg1 == 1) {
                                    btnCaseB.setBackgroundResource(R.drawable.bg_true1);
                                } else {
                                    btnCaseB.setBackgroundResource(R.drawable.bg_true2);
                                }
                            } else {
                                if (answer.equals(C)) {
                                    if (msg.arg1 == 1) {
                                        btnCaseC.setBackgroundResource(R.drawable.bg_true1);
                                    } else {
                                        btnCaseC.setBackgroundResource(R.drawable.bg_true2);
                                    }
                                } else {
                                    if (answer.equals(D)) {
                                        if (msg.arg1 == 1) {
                                            btnCaseD.setBackgroundResource(R.drawable.bg_true1);
                                        } else {
                                            btnCaseD.setBackgroundResource(R.drawable.bg_true2);
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    case ANSWER_THE_QUESTION:
                        answerTheQuestion(msg.arg1);
                        final long beginTime = System.currentTimeMillis();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (System.currentTimeMillis() - beginTime <= 5000) {
                                }
                                Message message = new Message();
                                message.what = SHOW_DIALOG_WRONG_ANSWER;
                                message.setTarget(handler);
                                message.sendToTarget();
                            }
                        });
                        thread.start();
                        break;

                    case SHOW_LIST_LEVEL:
                        bundle = new Bundle();
                        bundle.putInt("level", level + 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        };
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        time = 10;
        isPaused = false;

        while (time > 0 && !isPaused) {
            SystemClock.sleep(1000);
            time--;
            Message message = new Message();
            message.what = UPDATE_TIME;
            message.arg1 = time;
            message.setTarget(handler);
            message.sendToTarget();
        }

        if (!isPaused) {
            Message message = new Message();
            message.what = SHOW_DIALOG_OVER_TIME;
            message.setTarget(handler);
            message.sendToTarget();
        }
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

    @Override
    public void setFinish() {
        intent = new Intent(this, GameOverActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        final long beginTime;

        switch (view.getId()) {
            case R.id.btn_case_a:
                isPaused = true;

                createMedia(A);

                beginTime = System.currentTimeMillis();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() - beginTime <= 5000) {
                            Message message = new Message();
                            message.what = UPDATE_CHOOSE_BUTTON;
                            message.obj = A;
                            message.arg1 = 1;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                            message = new Message();
                            message.what = UPDATE_CHOOSE_BUTTON;
                            message.obj = A;
                            message.arg1 = 2;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);

//                            Log.i(TAG, "time " + (System.currentTimeMillis() - beginTime));

//                            if (System.currentTimeMillis() - beginTime == 2000) {
//                                answerNow();
//                            }
                        }

                        Message message = new Message();
                        message.what = ANSWER_THE_QUESTION;
                        message.arg1 = 1;
                        message.setTarget(handler);
                        message.sendToTarget();
                    }
                });
                thread.start();
                break;

            case R.id.btn_case_b:
                isPaused = true;

                createMedia(B);

                beginTime = System.currentTimeMillis();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() - beginTime <= 5000) {
                            Message message = new Message();
                            message.what = UPDATE_CHOOSE_BUTTON;
                            message.obj = B;
                            message.arg1 = 1;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                            message = new Message();
                            message.what = UPDATE_CHOOSE_BUTTON;
                            message.obj = B;
                            message.arg1 = 2;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                        }

                        Message message = new Message();
                        message.what = ANSWER_THE_QUESTION;
                        message.arg1 = 2;
                        message.setTarget(handler);
                        message.sendToTarget();
                    }
                });
                thread.start();
                break;

            case R.id.btn_case_c:
                isPaused = true;

                createMedia(C);

                beginTime = System.currentTimeMillis();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() - beginTime <= 5000) {
                            Message message = new Message();
                            message.what = UPDATE_CHOOSE_BUTTON;
                            message.obj = C;
                            message.arg1 = 1;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                            message = new Message();
                            message.what = UPDATE_CHOOSE_BUTTON;
                            message.obj = C;
                            message.arg1 = 2;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                        }

                        Message message = new Message();
                        message.what = ANSWER_THE_QUESTION;
                        message.arg1 = 3;
                        message.setTarget(handler);
                        message.sendToTarget();
                    }
                });
                thread.start();
                break;

            case R.id.btn_case_d:
                isPaused = true;

                createMedia(D);

                beginTime = System.currentTimeMillis();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() - beginTime <= 5000) {
                            Message message = new Message();
                            message.what = UPDATE_CHOOSE_BUTTON;
                            message.obj = D;
                            message.arg1 = 1;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                            message = new Message();
                            message.what = UPDATE_CHOOSE_BUTTON;
                            message.obj = D;
                            message.arg1 = 2;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                        }

                        Message message = new Message();
                        message.what = ANSWER_THE_QUESTION;
                        message.arg1 = 4;
                        message.setTarget(handler);
                        message.sendToTarget();
                    }
                });
                thread.start();
                break;
        }
    }

    private void answerNow() {
        Log.i(TAG, "answer now..");
        random = new Random();
        int i = random.nextInt(3);
        if (i == 0) {
            managerMedia.openMedia(R.raw.ans_now1, false);
        } else {
            if (i == 1) {
                managerMedia.openMedia(R.raw.ans_now2, false);
            } else {
                managerMedia.openMedia(R.raw.ans_now3, false);
            }
        }
    }

    private void answerTheQuestion(int answer) {
        int i;
        trueCase = questions.get(level - 1).getTrueCase();
        random = new Random();
        final long beginTime;
        if (answer == trueCase) {
            switch (trueCase) {
                case 1:
                    i = random.nextInt(3);
                    if (i == 0) {
                        managerMedia.openMedia(R.raw.true_a, false);
                    } else {
                        if (i == 1) {
                            managerMedia.openMedia(R.raw.true_a2, false);
                        } else {
                            managerMedia.openMedia(R.raw.true_a3, false);
                        }
                    }
                    managerMedia.play();

                    beginTime = System.currentTimeMillis();
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (System.currentTimeMillis() - beginTime <= 4000) {

                            }

                            Message message = new Message();
                            message.what = SHOW_LIST_LEVEL;
                            message.setTarget(handler);
                            message.sendToTarget();
                        }
                    });
                    thread.start();

                    break;

                case 2:
                    i = random.nextInt(3);
                    if (i == 0) {
                        managerMedia.openMedia(R.raw.true_b, false);
                    } else {
                        if (i == 1) {
                            managerMedia.openMedia(R.raw.true_b2, false);
                        } else {
                            managerMedia.openMedia(R.raw.true_b3, false);
                        }
                    }
                    managerMedia.play();

                    beginTime = System.currentTimeMillis();
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (System.currentTimeMillis() - beginTime <= 4000) {

                            }

                            Message message = new Message();
                            message.what = SHOW_LIST_LEVEL;
                            message.setTarget(handler);
                            message.sendToTarget();
                        }
                    });
                    thread.start();

                    break;

                case 3:
                    i = random.nextInt(3);
                    if (i == 0) {
                        managerMedia.openMedia(R.raw.true_c, false);
                    } else {
                        if (i == 1) {
                            managerMedia.openMedia(R.raw.true_c2, false);
                        } else {
                            managerMedia.openMedia(R.raw.true_c3, false);
                        }
                    }
                    managerMedia.play();

                    beginTime = System.currentTimeMillis();
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (System.currentTimeMillis() - beginTime <= 4000) {

                            }

                            Message message = new Message();
                            message.what = SHOW_LIST_LEVEL;
                            message.setTarget(handler);
                            message.sendToTarget();
                        }
                    });
                    thread.start();

                    break;

                case 4:
                    i = random.nextInt(3);
                    if (i == 0) {
                        managerMedia.openMedia(R.raw.true_d2, false);
                    } else {
                        managerMedia.openMedia(R.raw.true_d3, false);
                    }
                    managerMedia.play();

                    beginTime = System.currentTimeMillis();
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (System.currentTimeMillis() - beginTime <= 4000) {

                            }

                            Message message = new Message();
                            message.what = SHOW_LIST_LEVEL;
                            message.setTarget(handler);
                            message.sendToTarget();
                        }
                    });
                    thread.start();

                    break;
            }
        } else {
            switch (trueCase) {
                case 1:
                    i = random.nextInt(2);
                    if (i % 2 == 0) {
                        managerMedia.openMedia(R.raw.lose_a, false);
                    } else {
                        managerMedia.openMedia(R.raw.lose_a2, false);
                    }
                    managerMedia.play();

                    switch (answer) {
                        case 1:
                            btnCaseA.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 2:
                            btnCaseB.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 3:
                            btnCaseC.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 4:
                            btnCaseD.setBackgroundResource(R.drawable.bg_faile1);
                            break;
                    }

                    break;

                case 2:
                    i = random.nextInt(2);
                    if (i % 2 == 0) {
                        managerMedia.openMedia(R.raw.lose_b, false);
                    } else {
                        managerMedia.openMedia(R.raw.lose_b2, false);
                    }
                    managerMedia.play();

                    switch (answer) {
                        case 1:
                            btnCaseA.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 2:
                            btnCaseB.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 3:
                            btnCaseC.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 4:
                            btnCaseD.setBackgroundResource(R.drawable.bg_faile1);
                            break;
                    }

                    break;

                case 3:
                    i = random.nextInt(2);
                    if (i % 2 == 0) {
                        managerMedia.openMedia(R.raw.lose_c, false);
                    } else {
                        managerMedia.openMedia(R.raw.lose_c2, false);
                    }
                    managerMedia.play();

                    switch (answer) {
                        case 1:
                            btnCaseA.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 2:
                            btnCaseB.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 3:
                            btnCaseC.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 4:
                            btnCaseD.setBackgroundResource(R.drawable.bg_faile1);
                            break;
                    }

                    break;

                case 4:
                    i = random.nextInt(2);
                    if (i % 2 == 0) {
                        managerMedia.openMedia(R.raw.lose_d, false);
                    } else {
                        managerMedia.openMedia(R.raw.lose_d2, false);
                    }
                    managerMedia.play();

                    switch (answer) {
                        case 1:
                            btnCaseA.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 2:
                            btnCaseB.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 3:
                            btnCaseC.setBackgroundResource(R.drawable.bg_faile1);
                            break;

                        case 4:
                            btnCaseD.setBackgroundResource(R.drawable.bg_faile1);
                            break;
                    }

                    break;
            }
        }

        final long beginTime1;
        switch (trueCase) {
            case 1:
                beginTime1 = System.currentTimeMillis();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() - beginTime1 <= 4000) {
                            Message message = new Message();
                            message.what = UPDATE_TRUE_BUTTON;
                            message.obj = A;
                            message.arg1 = 1;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                            message = new Message();
                            message.what = UPDATE_TRUE_BUTTON;
                            message.obj = A;
                            message.arg1 = 2;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);

                        }
                    }
                });
                thread.start();
                break;

            case 2:
                beginTime1 = System.currentTimeMillis();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() - beginTime1 <= 4000) {
                            Message message = new Message();
                            message.what = UPDATE_TRUE_BUTTON;
                            message.obj = B;
                            message.arg1 = 1;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                            message = new Message();
                            message.what = UPDATE_TRUE_BUTTON;
                            message.obj = B;
                            message.arg1 = 2;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);

                        }
                    }
                });
                thread.start();
                break;

            case 3:
                beginTime1 = System.currentTimeMillis();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() - beginTime1 <= 4000) {
                            Message message = new Message();
                            message.what = UPDATE_TRUE_BUTTON;
                            message.obj = C;
                            message.arg1 = 1;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                            message = new Message();
                            message.what = UPDATE_TRUE_BUTTON;
                            message.obj = C;
                            message.arg1 = 2;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);

                        }
                    }
                });
                thread.start();
                break;

            case 4:
                beginTime1 = System.currentTimeMillis();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (System.currentTimeMillis() - beginTime1 <= 4000) {
                            Message message = new Message();
                            message.what = UPDATE_TRUE_BUTTON;
                            message.obj = D;
                            message.arg1 = 1;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);
                            message = new Message();
                            message.what = UPDATE_TRUE_BUTTON;
                            message.obj = D;
                            message.arg1 = 2;
                            message.setTarget(handler);
                            message.sendToTarget();
                            SystemClock.sleep(200);

                        }
                    }
                });
                thread.start();
                break;
        }
    }

    private void createMedia(String answer) {
        managerMedia = new ManagerMedia(this);
        switch (answer) {
            case A:
                random = new Random();
                if (random.nextInt(2) % 2 == 0) {
                    managerMedia.openMedia(R.raw.ans_a, false);
                } else {
                    managerMedia.openMedia(R.raw.ans_a2, false);
                }
                managerMedia.play();
                break;

            case B:
                random = new Random();
                if (random.nextInt(2) % 2 == 0) {
                    managerMedia.openMedia(R.raw.ans_b, false);
                } else {
                    managerMedia.openMedia(R.raw.ans_b2, false);
                }
                managerMedia.play();
                break;

            case C:
                random = new Random();
                if (random.nextInt(2) % 2 == 0) {
                    managerMedia.openMedia(R.raw.ans_c, false);
                } else {
                    managerMedia.openMedia(R.raw.ans_c2, false);
                }
                managerMedia.play();
                break;

            case D:
                random = new Random();
                if (random.nextInt(2) % 2 == 0) {
                    managerMedia.openMedia(R.raw.ans_d, false);
                } else {
                    managerMedia.openMedia(R.raw.ans_d2, false);
                }
                managerMedia.play();
                break;
        }
    }
}
