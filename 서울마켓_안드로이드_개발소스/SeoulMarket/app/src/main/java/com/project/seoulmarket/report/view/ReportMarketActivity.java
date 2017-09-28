package com.project.seoulmarket.report.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.seoulmarket.R;
import com.project.seoulmarket.dialog.DialogCancel;
import com.project.seoulmarket.main.view.MainTabActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportMarketActivity extends AppCompatActivity {

    @BindView(R.id.inputMarketName)
    EditText inputMarketName;
    @BindView(R.id.inputMarketGroup)
    EditText inputMarketHost;
    @BindView(R.id.inputMarketContent)
    EditText inputMarketContent;
    @BindView(R.id.inputMarketContentArea)
    LinearLayout inputMarketContentArea;

    Boolean inputNameCheck = false;
    Boolean inputContentCheck = false;

    DialogCancel dialogCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_market);

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
        actionbarTitle.setText("마켓 제보");
        actionbarTitle.setTypeface(Typeface.createFromAsset(getAssets(),"OTF_B.otf"));

        ImageView backBtn = (ImageView) mCustomView.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningOut();
            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        warningOut();
    }

    public void warningOut(){
        //사용자가 아무것도 입력하지 않았을 경우
        if (inputMarketName.length() == 0 && inputMarketHost.length() == 0 & inputMarketContent.length() == 0) {
            super.onBackPressed();
        }
        //사용자가 입력했을 경우는 경고창을 띄워준다.
        else {
            WindowManager.LayoutParams loginParams;
            dialogCancel = new DialogCancel(ReportMarketActivity.this, moveMainPage, remainPageEvent);

            loginParams = dialogCancel.getWindow().getAttributes();

            // Dialog 사이즈 조절 하기
            loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialogCancel.getWindow().setAttributes(loginParams);
            dialogCancel.show();

        }
    }

    private View.OnClickListener moveMainPage = new View.OnClickListener() {
        public void onClick(View v) {
            dialogCancel.dismiss();
            /**
             * 취소시 메인페이지로 이동한다.
             */
            Intent intent = new Intent(ReportMarketActivity.this, MainTabActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

    };

    private View.OnClickListener remainPageEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialogCancel.dismiss();
        }
    };


    @OnClick(R.id.inputMarketContentArea)
    public void focusConentArea(){
        inputMarketContent.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @OnClick(R.id.nextBtn)
    public void nextStopBtn(){
        inputMarketNameEmptyCheck();
        inputMarketContentEmptyCheck();

        if(inputNameCheck && inputContentCheck){
            //다음 단계
            Intent intent = new Intent(getApplicationContext(),ReportStepTwoActivity.class);
            intent.putExtra("name",inputMarketName.getText().toString());
            intent.putExtra("host",inputMarketHost.getText().toString());
            intent.putExtra("content",inputMarketContent.getText().toString());
            startActivity(intent);
        }

    }

    public void inputMarketNameEmptyCheck() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputMarketName.getWindowToken(), 0);
        final String marketName = inputMarketName.getText().toString();


        if (TextUtils.isEmpty(marketName))
            inputMarketName.setError(getString(R.string.error_field_required));
        else {
            inputNameCheck = true;
        }
    }

    public void inputMarketContentEmptyCheck() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputMarketContent.getWindowToken(), 0);
        final String marketContent = inputMarketContent.getText().toString();


        if (TextUtils.isEmpty(marketContent))
            inputMarketContent.setError(getString(R.string.error_field_required));
        else {
            inputContentCheck = true;
        }
    }

}
