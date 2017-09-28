package com.project.seoulmarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.seoulmarket.R;

public class DialogLogin extends Dialog {

    private Button dialog_login;
    private Button dialog_login_cancel;

    private View.OnClickListener loginEvent;
    private View.OnClickListener loginCancelEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        dialog_login = (Button) findViewById(R.id.dialog_login);
        dialog_login_cancel = (Button)findViewById(R.id.dialog_login_cancel);

        dialog_login.setOnClickListener(loginEvent);
        dialog_login_cancel.setOnClickListener(loginCancelEvent);

    }

    public DialogLogin(Context context, View.OnClickListener CurrentEvent, View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.loginEvent = CurrentEvent;
        this.loginCancelEvent = BtnEvent;
    }
}
