package com.project.seoulmarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.project.seoulmarket.R;

public class DialogImg extends Dialog {

    private ImageView imgview;
    private LinearLayout closeBtn;

    private View.OnClickListener closeEvent;

    String imgUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_img);

        imgview = (ImageView) findViewById(R.id.detailImgView);
//        closeBtn =(LinearLayout)findViewById(R.id.closeBtn);
//        closeBtn.setOnClickListener(closeEvent);

        Glide.with(getContext())
                .load(imgUrl)
                .placeholder(R.drawable.ic_default)
                .error(R.drawable.ic_default)
                .into(imgview);

    }

    public DialogImg(Context context,View.OnClickListener closeEvent ,String imgUrl) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.closeEvent = closeEvent;
        this.imgUrl = imgUrl;

    }
}
