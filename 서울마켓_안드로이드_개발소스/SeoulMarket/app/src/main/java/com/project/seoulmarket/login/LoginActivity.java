package com.project.seoulmarket.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.project.seoulmarket.R;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.join.JoinActivity;
import com.project.seoulmarket.join.model.UserNickCheckResult;
import com.project.seoulmarket.login.model.Token;
import com.project.seoulmarket.main.view.MainTabActivity;
import com.project.seoulmarket.service.NetworkService;
import com.project.seoulmarket.splash.model.ConnectResult;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback callback;      //콜백 선언
    CallbackManager mFacebookCallbackManager;

    @BindView(R.id.login_with_facebook)
    LoginButton facebookBtn;

    String token;
    NetworkService networkService;;

    String name;
    String thumnailImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        /**
         *
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
        actionbarTitle.setText("로그인");
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
        networkService = GlobalApplication.getInstance().getNetworkService();



        LoginManager.getInstance().logOut();

        //kakao login
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
//        Session.getCurrentSession().checkAndImplicitOpen();


        //Facebook
        facebookBtn.setReadPermissions("public_profile","email");
        // If using in a fragment
//        facebookBtn.setFragment(this);
        // Other app specific specialization

        mFacebookCallbackManager = CallbackManager.Factory.create();

        // Callback registration
        facebookBtn.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                Log.i("myTag","facebook");

//                String token = String.valueOf(loginResult.getAccessToken().getToken());

                Token token = new Token();
                token.access_token =loginResult.getAccessToken().getToken();

                Log.i("myTag", String.valueOf(token.access_token));

                Call<ConnectResult> requestFacebookLogin = networkService.requestFacebookLogin(token);

                requestFacebookLogin.enqueue(new Callback<ConnectResult>() {
                    @Override
                    public void onResponse(Call<ConnectResult> call, Response<ConnectResult> response) {

//                        final String cookies = response.headers().get("set-cookie");
//                        Log.i("myTag cookies2", cookies);

                        if(response.isSuccessful()){
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(response.body());
//                            Log.i("myTag",jsonString);

                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONObject resultValue = new JSONObject(jsonObject.getString("result"));

                                String result = resultValue.getString("message");
                                Log.i("myTag >> ",result );

                                if(result.equals("Success")){
                                    GraphRequest request = GraphRequest.newMeRequest(
                                            loginResult.getAccessToken(),
                                            new GraphRequest.GraphJSONObjectCallback() {
                                                @Override
                                                public void onCompleted(
                                                        JSONObject object,
                                                        GraphResponse response) {
                                                    // Application code
                                                    try {

                                                        String id = (String) response.getJSONObject().get("id");//페이스북 아이디값
                                                        name = (String) response.getJSONObject().get("name");//페이스북 이름

                                                        thumnailImg = "http://graph.facebook.com/"+ id +"/picture?type=large";

                                                        /**
                                                         * 페이스북 로그인 성공에 따른 정보 업데이트
                                                         */
//                                                        GlobalApplication.editor.putBoolean("Login_check", true);
//                                                        Log.i("myTag cookies", cookies);
//                                                        GlobalApplication.editor.putString("cookie", cookies);

                                                        GlobalApplication.editor.putString("method", "facebook");
//                                                        GlobalApplication.editor.putString("nickname", name);
                                                        GlobalApplication.editor.putString("thumbnail", thumnailImg);
                                                        GlobalApplication.editor.commit();


                                                    } catch (JSONException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                    }

                                                    // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다

                                                }
                                            });
                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "id,name,email,gender, birthday");
                                    request.setParameters(parameters);
                                    request.executeAsync();

                                    checkUser();
//                                    redirectJoinActivity();
                                }
                                else{
                                    LoginManager.getInstance().logOut();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("myTag", String.valueOf(e));
                            }



                        }
                        else{
                            Toast.makeText(getApplicationContext(),"접근이 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ConnectResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"접근이 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                });


            }

            @Override
            public void onCancel() {
                // App code
                Log.i("myTag","face-cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("myTag","face-error");
            }
        });


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출         }

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
            Log.i("myTag", String.valueOf(exception));

            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }

    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, KakaoSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void redirectJoinActivity() {
        /**
         * 연동을 성공하는 순간, 회원가입은 된 것.
         * 앱에서 사용할 닉네임만 따로 수정할 수 있도록 이동하는 것
         */

        Intent intent = new Intent(this, JoinActivity.class);
        intent.putExtra("login","facebook");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void redirectMainActivity() {
        /**
         * 연동을 성공하는 순간, 회원가입은 된 것.
         * 앱에서 사용할 닉네임만 따로 수정할 수 있도록 이동하는 것
         */

        Intent intent = new Intent(this, MainTabActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void checkUser(){
        Call<UserNickCheckResult> call = networkService.userCheck("nickname");
        call.enqueue(new Callback<UserNickCheckResult>() {
            @Override
            public void onResponse(Call<UserNickCheckResult> call, Response<UserNickCheckResult> response) {
                if (response.isSuccessful()){

                    UserNickCheckResult.Result result = response.body().result;

                    if(result.message.user_nickname == null)
                        redirectJoinActivity();
                    else{
                        if (result.message.user_nickname.equals("")){
                            redirectJoinActivity();
                        }
                        else{
                            Log.i("myTag",result.message.user_nickname);
                            GlobalApplication.editor.putBoolean("Login_check", true);
                            GlobalApplication.editor.putString("method", "facebook");
                            GlobalApplication.editor.putString("nickname", result.message.user_nickname);
                            GlobalApplication.editor.putString("thumbnail", thumnailImg);
                            GlobalApplication.editor.commit();
                            redirectMainActivity();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<UserNickCheckResult> call, Throwable t) {

            }
        });
    }
}
