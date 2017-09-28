package com.project.seoulmarket.detail.review;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.seoulmarket.R;
import com.project.seoulmarket.detail.presenter.RegisterReviewPresenter;
import com.project.seoulmarket.detail.presenter.RegisterReviewPresenterImpl;
import com.project.seoulmarket.dialog.DialogCancel;
import com.project.seoulmarket.dialog.DialogRegister;
import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterReviewActivity extends AppCompatActivity implements RegisterReviewView{

    @BindView(R.id.marketName)
    TextView marketName;
    @BindView(R.id.addImgBtn)
    Button addImgBtn;
    @BindView(R.id.imgName)
    TextView imgName;
    @BindView(R.id.reviewEdit)
    EditText reviewEdit;


    DialogCancel dialogCancel;
    DialogRegister dialog_Register;
    ProgressDialog asyncDialog;

    final int REQ_CODE_SELECT_IMAGE=100;
    String marketId;

    String imgURL="";
    RegisterReviewPresenter presenter;

    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_review);

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
        View mCustomView = mInflater.inflate(R.layout.actionbar_close_layout, null);

        TextView title = (TextView)mCustomView.findViewById(R.id.mytext);
        title.setText("후기 등록");
        title.setTypeface(Typeface.createFromAsset(getAssets(),"OTF_B.otf"));

        ImageView closeBtn = (ImageView) mCustomView.findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningOut();
            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        presenter = new RegisterReviewPresenterImpl(this);


        Intent intent = getIntent();
        marketId = intent.getExtras().getString("market_id");
        marketName.setText(intent.getExtras().getString("market_name"));

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
        WindowManager.LayoutParams loginParams;
        dialogCancel = new DialogCancel(RegisterReviewActivity.this, moveDetailPage, remainPageEvent);

        loginParams = dialogCancel.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogCancel.getWindow().setAttributes(loginParams);
        dialogCancel.show();
    }

    private View.OnClickListener moveDetailPage = new View.OnClickListener() {
        public void onClick(View v) {
            dialogCancel.dismiss();
            finish();
        }

    };

    private View.OnClickListener remainPageEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialogCancel.dismiss();
        }

    };

    @OnClick(R.id.addImgBtn)
    public void getImg(){
        // 사진 갤러리 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    // 선택된 이미지 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    String name_Str = getImageNameToUri(data.getData());

                    this.data = data;

                    Log.i("myTag",name_Str);

                    imgName.setText(name_Str);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else{
                imgURL = "";
                imgName.setText("");
            }
        }
    }

    // 선택된 이미지 파일명 가져오기
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        imgURL = imgPath;

        return imgName;
    }

    @OnClick(R.id.registerReviewBtn)
    public void completeReview(){

        if(reviewEdit.getText().toString().length() != 0 ){
            WindowManager.LayoutParams registerParams;
            dialog_Register = new DialogRegister(RegisterReviewActivity.this, registerEvent,registerCancelEvent);

            registerParams = dialog_Register.getWindow().getAttributes();

            // Dialog 사이즈 조절 하기
            registerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            registerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog_Register.getWindow().setAttributes(registerParams);

            dialog_Register.show();
        }
        else{
            Toast.makeText(getApplicationContext(),"내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
        }

    }

    private View.OnClickListener registerEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_Register.dismiss();

            asyncDialog = new ProgressDialog(RegisterReviewActivity.this);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("등록 중 입니다..");
            asyncDialog.setCanceledOnTouchOutside(false);
            // show dialog
            asyncDialog.show();

            String contents = reviewEdit.getText().toString();

            presenter.requestAddReview(marketId,contents,imgURL,data);
        }

    };

    private View.OnClickListener registerCancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_Register.dismiss();
        }

    };


    @OnClick(R.id.contentArea)
    public void giveFocus(){

        reviewEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void successMsg() {
        Toast.makeText(getApplicationContext(),"마켓 후기 등록 완료",Toast.LENGTH_SHORT).show();
        asyncDialog.dismiss();
        /**
         * 성공시  이동한다.
         */
        finish();
    }

    @Override
    public void errorMsg() {
        Toast.makeText(getApplicationContext(),R.string.error_network,Toast.LENGTH_SHORT).show();
        asyncDialog.dismiss();
    }

    @Override
    public Context getNowContext() {
        return getApplicationContext();
    }
}
