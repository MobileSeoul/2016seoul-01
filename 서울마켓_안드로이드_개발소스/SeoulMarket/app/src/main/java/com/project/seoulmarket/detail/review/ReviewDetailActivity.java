package com.project.seoulmarket.detail.review;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.seoulmarket.R;
import com.project.seoulmarket.dialog.DialogImg;
import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewDetailActivity extends AppCompatActivity {

    @BindView(R.id.inputWriter)
    TextView inputWriter;
    @BindView(R.id.inputwriteDate)
    TextView inputwriteDate;
    @BindView(R.id.reviewContent)
    TextView reviewContent;
    @BindView(R.id.showImgBtn)
    ImageView showImgBtn;

    DialogImg dialogImg;
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FFA700"));
        }

        ButterKnife.bind(this);


        /**
         * actionbar 설정
         */

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        getSupportActionBar().setElevation(0); // 그림자 없애기

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_back_layout, null);

        TextView actionbarTitle = (TextView)mCustomView.findViewById(R.id.mytext);
        actionbarTitle.setText("후기 보기");
        actionbarTitle.setTypeface(Typeface.createFromAsset(getAssets(),"OTF_B.otf"));
        ImageView backBtn = (ImageView) mCustomView.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        /**
         *
         */

        Intent intent = getIntent();
        String nickName = intent.getExtras().getString("review_nick");
        String uploadTime = intent.getExtras().getString("review_uploadtime");
        String contents = intent.getExtras().getString("review_content");
        imgUrl = intent.getExtras().getString("review_img");

        if(imgUrl.equals("")){
            showImgBtn.setVisibility(View.INVISIBLE);
//            showImgBtn.setClickable(false);
        }
        else{
            showImgBtn.setVisibility(View.VISIBLE);
        }

        inputWriter.setText(nickName);
        inputwriteDate.setText(uploadTime);
        reviewContent.setText(contents);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.showImgBtn)
    public void showDetailImg(){

        WindowManager.LayoutParams loginParams;
        dialogImg = new DialogImg(ReviewDetailActivity.this,closeEvent,imgUrl);

        loginParams = dialogImg.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogImg.getWindow().setAttributes(loginParams);

        dialogImg.show();

    }


    private View.OnClickListener closeEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialogImg.dismiss();
        }

    };


}
