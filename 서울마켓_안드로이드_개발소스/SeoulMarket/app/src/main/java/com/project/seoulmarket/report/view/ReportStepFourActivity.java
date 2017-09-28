package com.project.seoulmarket.report.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.seoulmarket.R;
import com.project.seoulmarket.dialog.DialogRegister;
import com.project.seoulmarket.main.view.MainTabActivity;
import com.project.seoulmarket.report.presenter.ReportStepFourPresenter;
import com.project.seoulmarket.report.presenter.ReportStepFourPresenterImpl;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportStepFourActivity extends AppCompatActivity implements ReportStepFourView{

    @BindView(R.id.itemImg1)
    ImageView itemImg1;
    @BindView(R.id.itemImg2)
    ImageView itemImg2;
    @BindView(R.id.itemImg3)
    ImageView itemImg3;
    @BindView(R.id.itemImg4)
    ImageView itemImg4;
    @BindView(R.id.itemImg5)
    ImageView itemImg5;
    @BindView(R.id.itemImg6)
    ImageView itemImg6;
    @BindView(R.id.imgArea)
    LinearLayout imgArea;
    @BindView(R.id.inputMarketTag)
    EditText inputMarketTag;
    @BindView(R.id.addMarketTagArea)
    LinearLayout inflatedLayout;
    @BindView(R.id.inputMarketURL)
    EditText inputMarketURL;
    @BindView(R.id.inputPhoneNum)
    EditText inputPhoneNum;

    ArrayList tagList;
    ProgressDialog asyncDialog;
    Intent dataList1 = null;
    Intent dataList2 = null;
    Intent dataList3 = null;
    Intent dataList4 = null;
    Intent dataList5 = null;
    Intent dataList6 = null;


    final int REQ_CODE_SELECT_IMAGE=100;
    ArrayList imgURL;
    int imgCount = 0;

    String TagString = "";

    private DialogRegister dialog_Register;

    ReportStepFourPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_step_four);
        ButterKnife.bind(this);

        /**
         * actionbar 설정
         */

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FFA700"));
        }

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
                finish();
            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        /**
         *
         */
        tagList = new ArrayList();
        imgURL = new ArrayList();
        presenter = new ReportStepFourPresenterImpl(this);

        asyncDialog = new ProgressDialog(ReportStepFourActivity.this);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("등록 중 입니다..");
        asyncDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.addMarketTagBtn)
    public void addMartketTagToArea(){

        if(inputMarketTag.getText().toString().length() == 0){
            Toast.makeText(getApplicationContext(),"태그를 입력해주세요.",Toast.LENGTH_SHORT).show();
        }
        else{
            String tempTag = "#"+inputMarketTag.getText().toString();

            /**
             * 사용자가 입력한 태그 중복체크 후 등록해야함.
             */
            //중복 아님
            if(tagDoubleCheck()){


                inputMarketTag.setText("");
                tagList.add(tempTag.trim());

                LayoutInflater child;
                final LinearLayout childLayout;

                child = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                childLayout = (LinearLayout) child.inflate(R.layout.recurit_tag_item, null);

                DisplayMetrics dm = this.getResources().getDisplayMetrics();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 0;
                params.leftMargin = 0;
                params.rightMargin = convertDpToPx(3, dm);
                params.topMargin = 0;

                childLayout.setLayoutParams(params);

                TextView tagview = (TextView)childLayout.findViewById(R.id.tagText);
                ImageView close = (ImageView)childLayout.findViewById(R.id.deleteTag);

                tagview.setText(tempTag);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ViewParent child = v.getParent();
                        //hashCode 가 같은 뷰를 삭제함
                        for(int a=0; a<inflatedLayout.getChildCount(); a++){
                            if(inflatedLayout.getChildAt(a).hashCode() == child.hashCode()){
                                tagList.remove(a);
                                inflatedLayout.removeViewAt(a);
                                break;
                            }
                        }

                    }
                });

                inflatedLayout.addView(childLayout);


            }
        }


    }

    public Boolean tagDoubleCheck(){
        String tempTag = "#"+inputMarketTag.getText().toString();

        if(tagList.size() < 4){

            for(int i = 0 ; i < tagList.size(); i++){
                if(tempTag.equals(tagList.get(i))){
                    Toast.makeText(getApplicationContext(),"중복된 태그입니다.",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"태크는 최대 4개입니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.registerServer)
    public void registerServer(){

        if (tagList.size() > 0 && imgCount > 0){


            String tagTemp = String.valueOf(tagList.get(0));
            for(int i = 1; i<tagList.size();i++)
                tagTemp+=","+tagList.get(i);

            Log.i("myTag tag",tagTemp);
            TagString = tagTemp;

            WindowManager.LayoutParams registerParams;
            dialog_Register = new DialogRegister(ReportStepFourActivity.this, registerEvent,registerCancelEvent);

            registerParams = dialog_Register.getWindow().getAttributes();

            // Dialog 사이즈 조절 하기
            registerParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            registerParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog_Register.getWindow().setAttributes(registerParams);

            dialog_Register.show();
        }
        else{
            Toast.makeText(getApplicationContext(),"필수 항목을 채워주세요.",Toast.LENGTH_SHORT).show();
        }

    }

    private View.OnClickListener registerEvent = new View.OnClickListener() {
        public void onClick(View v) {

            asyncDialog.show();
            dialog_Register.dismiss();

            // show dialog

            /**
             *
             */

            Intent getData = getIntent();
//            Log.i("myTag name",);
//            Log.i("myTag host",);
//            Log.i("myTag content",);
//            Log.i("myTag startDate",);
//            Log.i("myTag startTime",getData.getExtras().getString("startTime"));
//            Log.i("myTag endDate",
//            Log.i("myTag endTime",getData.getExtras().getString("endTime"));
//            Log.i("myTag address",);
//            Log.i("myTag lat",);
//            Log.i("myTag log",);

            String market_name = getData.getExtras().getString("name");
            String market_address = getData.getExtras().getString("address");
            String market_host = getData.getExtras().getString("host");
            String market_contents = getData.getExtras().getString("content");
            String market_tag = TagString;
            String market_longitude =getData.getExtras().getString("log");
            String market_latitude = getData.getExtras().getString("lat");
            String market_tell = inputPhoneNum.getText().toString();
            String market_startdate = getData.getExtras().getString("startDate") +" "+getData.getExtras().getString("startTime");
            String market_enddate = getData.getExtras().getString("endDate") + " " + getData.getExtras().getString("endTime");
            String market_url = inputMarketURL.getText().toString();
//          imgURL

            presenter.addReportMarket(market_name,market_address,market_host,market_contents,market_tag,market_longitude,market_latitude,
                    market_tell,market_startdate,market_enddate,market_url,imgURL,dataList1,dataList2,dataList3,dataList4,dataList5,dataList6);

        }

    };

    private View.OnClickListener registerCancelEvent = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_Register.dismiss();
        }

    };

    @OnClick(R.id.imgArea)
    public void getImg(){
        // 사진 갤러리 호출
        if(imgCount < 6){

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
        }
        else{
            Toast.makeText(getApplicationContext(),"최대 이미지 6장입니다.",Toast.LENGTH_SHORT).show();
        }
    }

    // 선택된 이미지 가져오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    String name_Str = getImageNameToUri(data.getData());



                    Log.i("myTag",String.valueOf(imgCount));

                    if(imgCount == 0)
                        dataList1 = data;
                    else if(imgCount == 1)
                        dataList2 = data;
                    else if(imgCount == 2)
                        dataList3 = data;
                    else if(imgCount == 3)
                        dataList4 = data;
                    else if(imgCount == 4)
                        dataList5 = data;
                    else if(imgCount == 5)
                        dataList6 = data;

                    //이미지 데이터를 비트맵으로 받아온다.
//                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    AssetFileDescriptor afd = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inSampleSize = 4;
                    Bitmap image_bitmap = BitmapFactory.decodeFileDescriptor(afd.getFileDescriptor(), null, opt);


                    //배치해놓은 ImageView에 set
                    if(imgCount == 0)
                        itemImg1.setImageBitmap(image_bitmap);
                    else if(imgCount == 1)
                        itemImg2.setImageBitmap(image_bitmap);
                    else if(imgCount == 2)
                        itemImg3.setImageBitmap(image_bitmap);
                    else if(imgCount == 3)
                        itemImg4.setImageBitmap(image_bitmap);
                    else if(imgCount == 4)
                        itemImg5.setImageBitmap(image_bitmap);
                    else if(imgCount == 5)
                        itemImg6.setImageBitmap(image_bitmap);

                    imgCount++;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
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

        imgURL.add(imgPath);

        return imgName;
    }

    @Override
    public void successMsg() {
        Toast.makeText(getApplicationContext(),"마켓 제보 완료",Toast.LENGTH_SHORT).show();
        asyncDialog.dismiss();
        /**
         * 성공시 메인페이지로 이동한다.
         */
        Intent intent = new Intent(ReportStepFourActivity.this, MainTabActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
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

    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }
}
