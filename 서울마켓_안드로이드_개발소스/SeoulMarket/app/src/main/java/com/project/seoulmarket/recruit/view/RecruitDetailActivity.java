package com.project.seoulmarket.recruit.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.emilsjolander.components.StickyScrollViewItems.StickyScrollView;
import com.project.seoulmarket.R;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.dialog.DialogImg;
import com.project.seoulmarket.dialog.DialogLogin;
import com.project.seoulmarket.login.LoginActivity;
import com.project.seoulmarket.recruit.model.AddReview;
import com.project.seoulmarket.recruit.model.DetailData;
import com.project.seoulmarket.recruit.model.Review;
import com.project.seoulmarket.recruit.presenter.RecruitDetailPresenter;
import com.project.seoulmarket.recruit.presenter.RecruitDetailPresenterImpl;
import com.project.seoulmarket.recruit.presenter.RecruitReviewAdapter;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecruitDetailActivity extends AppCompatActivity implements RecruitDetailView{

    @BindView(R.id.reviewAddBtn)
    Button reviewAddBtn;
    @BindView(R.id.inputReviewAddEdit)
    EditText inputReviewAddEdit;
    @BindView(R.id.reviewList)
    LinearLayout listview;
    @BindView(R.id.reviewTitle)
    TextView reviewTitle;
    @BindView(R.id.sticky_scroll)
    StickyScrollView sticky_scroll;
    @BindView(R.id.inputWriter)
    TextView inputWriter;
    @BindView(R.id.inputwriteDate)
    TextView inputwriteDate;
    @BindView(R.id.reviewContent)
    TextView reviewContent;

    @BindView(R.id.showImgBtn)
    ImageView showImgBtn;

    DialogImg dialogImg;
    DialogLogin dialog_login;

    InputMethodManager imm;
    RecruitReviewAdapter adapter;
    ArrayList<Review> reviewDatas;

    String recruitId ="";
    String imgUrl ="";
    String currentDate = "";

    RecruitDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_detail);

        /**
         *
         */

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FFA700"));
        }

        ButterKnife.bind(this);


        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        getSupportActionBar().setElevation(0); // 그림자 없애기

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_back_layout, null);

        TextView actionbarTitle = (TextView)mCustomView.findViewById(R.id.mytext);
        actionbarTitle.setText("셀러 모집");
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



        presenter = new RecruitDetailPresenterImpl(this);

        Intent intent = getIntent();
        recruitId = intent.getExtras().getString("recruitId");

        Log.i("myTag",recruitId);

        presenter.getDetailData(recruitId);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        reviewDatas = new ArrayList<Review>();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.showImgBtn)
    public void showDetailImg(){

        WindowManager.LayoutParams loginParams;
        dialogImg = new DialogImg(RecruitDetailActivity.this,closeEvent,imgUrl);

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

    @OnClick(R.id.reviewAddBtn)
    public void addReview(){

        if(inputReviewAddEdit.getText().length() != 0) {



            if(GlobalApplication.loginInfo.getBoolean("Login_check", false)) {

                // 시스템으로부터 현재시간(ms) 가져오기
                long now = System.currentTimeMillis();
                // Data 객체에 시간을 저장한다.
                Date date = new Date(now);
                // 각자 사용할 포맷을 정하고 문자열로 만든다.
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                currentDate = dateFormat.format(date);

                //부모 뷰
//            LinearLayout listview = (LinearLayout) findViewById(R.id.reviewList);


                AddReview reviewData = new AddReview();
                reviewData.reply_contents = inputReviewAddEdit.getText().toString();

                presenter.addReview(recruitId,reviewData);

            }
            else{
                WindowManager.LayoutParams loginParams;
                dialog_login = new DialogLogin(RecruitDetailActivity.this, loginEvent,loginCancelEvent);

                loginParams = dialog_login.getWindow().getAttributes();

                // Dialog 사이즈 조절 하기
                loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog_login.getWindow().setAttributes(loginParams);

                dialog_login.show();

            }



        }
    }


    private View.OnClickListener loginEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_login.dismiss();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    };

    private View.OnClickListener loginCancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_login.dismiss();
        }

    };
    @Override
    public void setRecruitDetailData(DetailData getData) {
        reviewTitle.setText(getData.recruitment_title);
        inputWriter.setText(getData.user_nickname);
        inputwriteDate.setText(getData.recruitment_uploadtime);
        reviewContent.setText(getData.recruitment_contents);


        imgUrl = getData.recruitment_image;

        Log.i("myTag img1",imgUrl);

        if(imgUrl.equals("")){
            Log.i("myTag img","isvisble");
            showImgBtn.setVisibility(View.INVISIBLE);
//            showImgBtn.setClickable(false);
        }
        else{
            Log.i("myTag img","visble");
            showImgBtn.setVisibility(View.VISIBLE);
        }

        reviewDatas.addAll(getData.review);

        /**
         * 미리 정보를 받아와서 객체로 저장해놓고 있어야함!
         */

        //부모 뷰
        LinearLayout listview = (LinearLayout)findViewById(R.id.reviewList);

        LayoutInflater child;
        LinearLayout childLayout;

        TextView reviewNickname;
        TextView reviewContent;
        TextView reviewDate;


        DisplayMetrics dm = this.getResources().getDisplayMetrics();

        for(int i=0; i<reviewDatas.size();i++){

            child = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            childLayout = (LinearLayout) child.inflate(R.layout.review_detail_list_review_item, null);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = convertDpToPx(5, dm);
            params.leftMargin = 0;
            params.rightMargin = 0;
            params.topMargin = convertDpToPx(5, dm);

            childLayout.setLayoutParams(params);


            reviewNickname =  (TextView)childLayout.findViewById(R.id.reviewNickname);
            reviewDate = (TextView)childLayout.findViewById(R.id.reviewDate);
            reviewContent =  (TextView)childLayout.findViewById(R.id.reviewContent);

            reviewNickname.setText(reviewDatas.get(i).user_nickname);
            reviewDate.setText(reviewDatas.get(i).reply_uploadtime);
            reviewContent.setText(reviewDatas.get(i).reply_contents);

            listview.addView(childLayout);

        }
    }

    @Override
    public void addReviewArea() {
        LayoutInflater child;
        LinearLayout childLayout;

        TextView reviewNickname;
        TextView reviewContent;
        TextView reviewDate;

        child = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        childLayout = (LinearLayout) child.inflate(R.layout.review_detail_list_review_item, null);

        DisplayMetrics dm = this.getResources().getDisplayMetrics();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 0;
        params.leftMargin = 0;
        params.rightMargin = 0;
        params.topMargin = convertDpToPx(8, dm);

        childLayout.setLayoutParams(params);

        reviewNickname = (TextView) childLayout.findViewById(R.id.reviewNickname);
        reviewDate = (TextView) childLayout.findViewById(R.id.reviewDate);
        reviewContent = (TextView) childLayout.findViewById(R.id.reviewContent);

        reviewNickname.setText(GlobalApplication.loginInfo.getString("nickname", ""));
        reviewDate.setText(currentDate);
        reviewContent.setText(inputReviewAddEdit.getText().toString());

        listview.addView(childLayout);

        inputReviewAddEdit.setText("");
        imm.hideSoftInputFromWindow(inputReviewAddEdit.getWindowToken(), 0);

        sticky_scroll.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public void NetworkError() {
        Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
    }

    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }
}
