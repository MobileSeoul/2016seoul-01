package com.project.seoulmarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.seoulmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by KyoungHyun on 16. 5. 1..
 */
public class DialogChoose extends Dialog {

    @BindView(R.id.completeBtn)
    Button completeBtn;
    @BindView(R.id.cancelBtn)
    Button cancelBtn;

    private View.OnClickListener completeBtnEvent;
    private View.OnClickListener cancelBtnEvnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose);

        ButterKnife.bind(this);
        completeBtn.setOnClickListener(completeBtnEvent);
        cancelBtn.setOnClickListener(cancelBtnEvnet);


    }

    public DialogChoose(Context context, View.OnClickListener complete, View.OnClickListener cancel) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.completeBtnEvent = complete;
        this.cancelBtnEvnet = cancel;

    }

}

