package com.project.seoulmarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.seoulmarket.R;


public class DialogRegister extends Dialog {

    private Button dialog_register;
    private Button dialog_dialog_register_cancel;

    private View.OnClickListener registerEvent;
    private View.OnClickListener registerCancelEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_register);

        dialog_register = (Button) findViewById(R.id.dialog_register);
        dialog_dialog_register_cancel = (Button)findViewById(R.id.dialog_register_cancel);

        dialog_register.setOnClickListener(registerEvent);
        dialog_dialog_register_cancel.setOnClickListener(registerCancelEvent);

    }

    public DialogRegister(Context context, View.OnClickListener CurrentEvent, View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.registerEvent = CurrentEvent;
        this.registerCancelEvent = BtnEvent;
    }
}
