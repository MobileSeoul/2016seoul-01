package com.project.seoulmarket.join;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;
import com.project.seoulmarket.R;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.join.model.JoinData;
import com.project.seoulmarket.main.view.MainTabActivity;
import com.project.seoulmarket.service.NetworkService;
import com.project.seoulmarket.splash.model.ConnectResult;
import com.project.seoulmarket.splash.model.MessageResult;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {

    @BindView(R.id.userNickname)
    EditText nickNameArea;
    @BindView(R.id.doubleCheck)
    TextView doubleCheckBtn;

    private String usrToken;
    String loginMethod;
    Boolean nickNameCheck = false;


    InputMethodManager imm;
    NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        ButterKnife.bind(this);

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
        actionbarTitle.setText("회원 가입");

        actionbarTitle.setTypeface(Typeface.createFromAsset(getAssets(),"OTF_B.otf"));

        ImageView backBtn = (ImageView) mCustomView.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);     // 여기서 this는 Activity의 this

                // 여기서 부터는 알림창의 속성 설정
                builder.setTitle("회원 가입을 취소하시겠습니까?")        // 메세지 설정
                        //  .setMessage("취소 시 연동된 앱의 닉네임으로 설정됩니다.")
                        .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){

                                if(GlobalApplication.loginInfo.getString("method", "").equals("kakao")){
                                    //kakao 로그아웃
                                    UserManagement.requestLogout(new LogoutResponseCallback() {
                                        @Override
                                        public void onCompleteLogout() {
                                        }
                                    });
                                }
                                else{
                                    LoginManager.getInstance().logOut();
                                }

                                GlobalApplication.editor.putBoolean("Login_check", false);
                                GlobalApplication.editor.commit();

                                finish();

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        networkService = GlobalApplication.getInstance().getNetworkService();

        Log.i("myTag","In Join");

        Intent intent = getIntent();
        loginMethod =  intent.getExtras().getString("login");


        Log.i("myTag",loginMethod);

        /**
         * kakao를 통해 로그인했을 경우
         */
        if(loginMethod.equals("kakao")){
            kakaoRequestMe();
        }
        else if(loginMethod.equals("facebook")) {
            facebookRequestMe();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

        // 여기서 부터는 알림창의 속성 설정
        builder.setTitle("회원 가입을 취소하시겠습니까?")        // 메세지 설정
                //  .setMessage("취소 시 연동된 앱의 닉네임으로 설정됩니다.")
                .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){

                        if(GlobalApplication.loginInfo.getString("method", "").equals("kakao")){
                            //kakao 로그아웃
                            UserManagement.requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                }
                            });
                        }
                        else{
                            LoginManager.getInstance().logOut();
                        }

                        GlobalApplication.editor.putBoolean("Login_check", false);
                        GlobalApplication.editor.commit();

                        finish();

                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

    /**
     * facebook 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void facebookRequestMe(){

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        try {

                            String id = (String) response.getJSONObject().get("id");//페이스북 아이디값
                            String name = (String) response.getJSONObject().get("name");//페이스북 이름
//                            String email = (String) response.getJSONObject().get("email");//이메일

//                            Log.i("myTag",id);
//                            Log.i("myTag",name);
//                            Log.i("myTag", String.valueOf(response));


//                            nickNameArea.setText(name);
//
//                            String thumnailImg = "http://graph.facebook.com/"+ id +"/picture?type=large";
//                            Glide.with(JoinActivity.this)
//                                    .load(thumnailImg)
//                                    .into(profile);


                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


    /**
     * kakao 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void kakaoRequestMe() { //유저의 정보를 받아오는 함수
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                Log.i("myTag","error2");

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.i("myTag","error3");
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환

//                Log.i("myTag", String.valueOf(userProfile.getId()));
//                Log.i("myTag", String.valueOf(userProfile.getNickname()));
//                Logger.d("UserProfile : " + userProfile);

                usrToken = String.valueOf(userProfile.getId());
                String thumnailImg = userProfile.getThumbnailImagePath();

//                Log.i("myTag", thumnailImg);

//                nickNameArea.setText(userProfile.getNickname());

                /**
                 * 받아온 User 데이터의 profileUrl을 Glide 라이브러리를 이용하여 이미지뷰에 삽입하는 코드입니다.
                 * Glide의 가장 기본적인 사용방법이니 익혀두시면 편합니다.
                 */
//                Glide.with(JoinActivity.this)
//                        .load(thumnailImg)
//                        .into(profile);

            }
        });
    }

    @OnClick(R.id.doubleCheck)
    public void nickNameCheck(){
//        Toast.makeText(getApplicationContext(),nickNameArea.getText(),Toast.LENGTH_SHORT).show();


        //빈칸 체크
        if(nickNameArea.length() == 0){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(nickNameArea.getWindowToken(), 0);
            final String marketContent = nickNameArea.getText().toString();

            nickNameArea.setError(getString(R.string.error_field_required));
        }
        else{
            final Call<ConnectResult> doubleCheck = networkService.nickNameDoubleCheck(String.valueOf(nickNameArea.getText()));

            doubleCheck.enqueue(new Callback<ConnectResult>() {
                @Override
                public void onResponse(Call<ConnectResult> call, Response<ConnectResult> response) {


                    if(response.isSuccessful()){
                        Log.i("myTag","double check" );

                        Gson gson = new Gson();
                        String jsonString = gson.toJson(response.body());
//                            Log.i("myTag",jsonString);

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(jsonString);
                            JSONObject resultValue = new JSONObject(jsonObject.getString("result"));
                            String result = resultValue.getString("message");

                            Log.i("myTag",result );
                            if(result.equals("Success")){
                                doubleCheckBtn.setText("사용 가능");
                                nickNameCheck = true;

                                imm.hideSoftInputFromWindow(nickNameArea.getWindowToken(), 0);
                            }
                            else{
                                doubleCheckBtn.setText("사용 불가");
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(nickNameArea.getWindowToken(), 0);
                                nickNameArea.setError(getString(R.string.error_field_doubled));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ConnectResult> call, Throwable t) {

                }
            });
        }


    }


    public boolean nickNameCheckError() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nickNameArea.getWindowToken(), 0);
        final String marketContent = nickNameArea.getText().toString();
        nickNameArea.setError(getString(R.string.error_field_doubled));

        if (TextUtils.isEmpty(marketContent)) {

            return false;
        }
        else {
            return true;
        }
    }


    @OnClick(R.id.completeNickname)
    public void complete(){
        // TODO: 2016. 10. 18. 서버로 닉네임 업데이트 해줘야 함.

        if(nickNameCheck){

            JoinData data = new JoinData();
            data.nickname = nickNameArea.getText().toString();

            Call<ConnectResult> requestJoin = networkService.joinPutNickName(data);
            requestJoin.enqueue(new Callback<ConnectResult>() {
                @Override
                public void onResponse(Call<ConnectResult> call, Response<ConnectResult> response) {
                    if(response.isSuccessful()){

                        Log.i("myTag","nickname put");

                        MessageResult data = response.body().result;

                        if(data.message.equals("Success")){
                            GlobalApplication.editor.putBoolean("Login_check", true);
                            GlobalApplication.editor.putString("nickname", String.valueOf(nickNameArea.getText()));
                            GlobalApplication.editor.commit();

                            Toast.makeText(getApplicationContext(),"회원가입 성공!",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(JoinActivity.this, MainTabActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"인터넷 상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ConnectResult> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"인터넷 상태를 확인해주세요.",Toast.LENGTH_SHORT).show();
                    Log.i("myTag",t.toString());

                }
            });



        }
        else{
            Toast.makeText(getApplicationContext(),"닉네임 중복체크를 먼저 해주세요.",Toast.LENGTH_SHORT).show();
        }

    }

}
