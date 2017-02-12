package com.thaison.btvn_buoi18_ailatrieuphu;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by Shin on 9/6/2016.
 */
public class ReadyDialog extends Dialog implements View.OnClickListener {
    private IReadyDialog iReadyDialog;

    public ReadyDialog(Context context) {
        super(context);
        init();
    }

    public ReadyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ready);
        findViewById(R.id.btn_back_to_menu).setOnClickListener(this);
        findViewById(R.id.btn_ready).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_to_menu:
                iReadyDialog.setBackToMenu();
                break;

            case R.id.btn_ready:
                iReadyDialog.setReady();
                break;
        }

        dismiss();
    }

    public void setIReadyDialog(IReadyDialog iReadyDialog) {
        this.iReadyDialog = iReadyDialog;
    }
}
