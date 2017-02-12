package com.thaison.btvn_buoi18_ailatrieuphu;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Shin on 9/9/2016.
 */
public class GameOverDialog extends Dialog implements View.OnClickListener {
    private TextView tvGameOverTitle;
    private TextView tvGameOverContent;
    private IGameOverDialog iGameOverDialog;

    public GameOverDialog(Context context) {
        super(context);
        init();
    }

    public GameOverDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public TextView getTvGameOverTitle() {
        return tvGameOverTitle;
    }

    public TextView getTvGameOverContent() {
        return tvGameOverContent;
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_game_over);
        initViews();
    }

    private void initViews() {
        tvGameOverTitle = (TextView) findViewById(R.id.tv_game_over_title);
        tvGameOverContent = (TextView) findViewById(R.id.tv_game_over_content);
        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                iGameOverDialog.setFinish();
                break;
        }

        dismiss();
    }

    public void setIGameOverDialog(IGameOverDialog iGameOverDialog) {
        this.iGameOverDialog = iGameOverDialog;
    }
}
