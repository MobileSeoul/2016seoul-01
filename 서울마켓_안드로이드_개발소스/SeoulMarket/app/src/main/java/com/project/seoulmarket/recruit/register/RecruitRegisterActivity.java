package com.project.seoulmarket.recruit.register;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.seoulmarket.R;
import com.project.seoulmarket.dialog.DialogCancel;
import com.project.seoulmarket.dialog.DialogRegister;
import com.project.seoulmarket.recruit.presenter.RecruitRegisterPresenter;
import com.project.seoulmarket.recruit.presenter.RecruitRegisterPresenterImpl;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecruitRegisterActivity extends AppCompatActivity implements RecruitRegisterView{

    @BindView(R.id.inputTitleEdit)
    EditText inputTitleEdit;
    @BindView(R.id.inputContentEdit)
    EditText inputContentEdit;
    @BindView(R.id.addImageName)
    TextView imgName;

    DialogCancel dialogCancel;
    DialogRegister dialog_Register;
    ProgressDialog asyncDialog;

    Boolean emptyTitleCheck = false;
    Boolean emptyContentCheck = false;

    String currentDate = "";
    final int REQ_CODE_SELECT_IMAGE=100;

    String imgUrl="";

    RecruitRegisterPresenter presenter;

    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit_register);
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


        TextView actionbarTitle = (TextView)mCustomView.findViewById(R.id.mytext);
        actionbarTitle.setText("셀러 모집");
        actionbarTitle.setTypeface(Typeface.createFromAsset(getAssets(),"OTF_B.otf"));

        ImageView closeBtn = (ImageView) mCustomView.findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningOut();
            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        /**
         *
         */

        presenter = new RecruitRegisterPresenterImpl(this);


        // 시스템으로부터 현재시간(ms) 가져오기
        long now = System.currentTimeMillis();
        // Data 객체에 시간을 저장한다.
        Date date = new Date(now);
        // 각자 사용할 포맷을 정하고 문자열로 만든다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        currentDate = dateFormat.format(date);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.getImageBtn)
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
                imgUrl = "";
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

        imgUrl = imgPath;

        return imgName;
    }


    @OnClick(R.id.inputContentArea)
    public void focusContentArea(){
        Log.i("myTag","click");

        inputContentEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @OnClick(R.id.completeRecruit)
    public void compelteRecruit(){
        titleEmptyCheck();
        contentEmptyCheck();

        if(emptyTitleCheck && emptyContentCheck){

            Log.i("myTag",inputTitleEdit.getText().toString());
            Log.i("myTag",inputContentEdit.getText().toString());
            Log.i("myTag",currentDate);

            WindowManager.LayoutParams registerParams;
            dialog_Register = new DialogRegister(RecruitRegisterActivity.this, registerEvent,registerCancelEvent);

            registerParams = dialog_Register.getWindow().getAttributes();

            // Dialog 사이즈 조절 하기
            registerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            registerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog_Register.getWindow().setAttributes(registerParams);

            dialog_Register.show();
        }
    }

    private View.OnClickListener registerEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_Register.dismiss();
//            Toast.makeText(getApplicationContext(),"셀러 모집 등록 완료!",Toast.LENGTH_SHORT).show();

            String title = inputTitleEdit.getText().toString();
            String content = inputContentEdit.getText().toString();

            asyncDialog = new ProgressDialog(RecruitRegisterActivity.this);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("등록 중 입니다..");
            asyncDialog.setCanceledOnTouchOutside(false);
            // show dialog
            asyncDialog.show();

            presenter.addRecruit(imgUrl,title,content,data);
        }

    };

    private View.OnClickListener registerCancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_Register.dismiss();
        }

    };


    @Override
    public void onBackPressed() {
        warningOut();
    }

    public void warningOut(){
        WindowManager.LayoutParams loginParams;
        dialogCancel = new DialogCancel(RecruitRegisterActivity.this, moveRecruitPage, remainPageEvent);

        loginParams = dialogCancel.getWindow().getAttributes();

        // Dialog 사이즈 조절 하기
        loginParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        loginParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogCancel.getWindow().setAttributes(loginParams);
        dialogCancel.show();
    }

    private View.OnClickListener moveRecruitPage = new View.OnClickListener() {
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

    public void titleEmptyCheck(){
        if(inputTitleEdit.length() == 0) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputTitleEdit.getWindowToken(), 0);
            inputTitleEdit.setError(getString(R.string.error_field_required));
            emptyTitleCheck = false;
        }
        else
            emptyTitleCheck = true;
    }

    public void contentEmptyCheck(){
        if(inputContentEdit.length() == 0) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputContentEdit.getWindowToken(), 0);
            inputContentEdit.setError(getString(R.string.error_field_required));
            emptyContentCheck = false;
        }
        else
            emptyContentCheck = true;

    }


    @Override
    public void successMsg() {
        Toast.makeText(getApplicationContext(),"셀러 모집 등록 완료",Toast.LENGTH_SHORT).show();
        asyncDialog.dismiss();
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
