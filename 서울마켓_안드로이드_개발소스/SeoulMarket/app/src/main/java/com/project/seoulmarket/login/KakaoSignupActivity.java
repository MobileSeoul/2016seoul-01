package com.project.seoulmarket.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.Session;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;
import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.join.JoinActivity;
import com.project.seoulmarket.join.model.UserNickCheckResult;
import com.project.seoulmarket.main.view.MainTabActivity;
import com.project.seoulmarket.service.NetworkService;
import com.project.seoulmarket.splash.model.ConnectResult;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KakaoSignupActivity extends AppCompatActivity {

    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    NetworkService networkService;

    String thumnailImg = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        networkService = GlobalApplication.getInstance().getNetworkService();

        /**
         * kakao 로그인 요청 -- 우리서버로 토큰의 유효성 체크
         */
//        ;


        Call<ConnectResult> requestKakaoLogin = networkService.requestKakaoLogin(Session.getCurrentSession().getAccessToken());

        requestKakaoLogin.enqueue(new Callback<ConnectResult>() {
            @Override
            public void onResponse(Call<ConnectResult> call, Response<ConnectResult> response) {
                if(response.isSuccessful()){
//                            Log.i("myTag","reponse");

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(response.body());
//                            Log.i("myTag",jsonString);

                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        JSONObject resultValue = new JSONObject(jsonObject.getString("result"));

                        String result = resultValue.getString("message");
                        Log.i("myTag",result );

                        if(result.equals("Success")){

                            requestMe();
//                            requestAccessTokenInfo();
                        }
                        else{
                            //kakao 로그아웃
                            UserManagement.requestLogout(new LogoutResponseCallback() {
                                @Override
                                public void onCompleteLogout() {
                                }
                            });
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

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() { //유저의 정보를 받아오는 함수
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
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.i("myTag","error3");
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환

                Log.i("myTag", String.valueOf(userProfile.getId()));
                Log.i("myTag", String.valueOf(userProfile.getNickname()));
                Logger.d("UserProfile : " + userProfile);

                thumnailImg = userProfile.getProfileImagePath();
//                GlobalApplication.editor.putBoolean("Login_check", true);
                GlobalApplication.editor.putString("method", "kakao");
//                GlobalApplication.editor.putString("nickname", userProfile.getNickname());
                GlobalApplication.editor.putString("thumbnail", userProfile.getProfileImagePath());
                Log.i("myTag", String.valueOf(userProfile.getProfileImagePath()));
                GlobalApplication.editor.commit();


                /**
                 * 로그인 성공 시
                 * 닉네임 설정 페이지로 이동한다.
                 * 프로필 이미지, 카카오 로그인 토큰 전달
                 */
                checkUser();
//                redirectJoinActivity();
//                redirectMainActivity(); // 로그인 성공시 MainActivity로
            }
        });
    }

    private void requestAccessTokenInfo() {
        AuthService.requestAccessTokenInfo(new ApiResponseCallback<AccessTokenInfoResponse>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity(self);
            }

            @Override
            public void onNotSignedUp() {
                // not happened
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e("failed to get access token info. msg=" + errorResult);
            }

            @Override
            public void onSuccess(AccessTokenInfoResponse accessTokenInfoResponse) {
                long userId = accessTokenInfoResponse.getUserId();
                Logger.d("this access token is for userId=" + userId);
                Log.i("myTag kakao Token", String.valueOf(accessTokenInfoResponse));

                long expiresInMilis = accessTokenInfoResponse.getExpiresInMillis();
//                Logger.d("this access token expires after " + expiresInMilis + " milliseconds.");

//                Log.i("myTag", String.valueOf(Session.getCurrentSession().getAccessToken()));
            }
        });
    }


    private void redirectMainActivity() {
        Intent intent = new Intent(this, MainTabActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void redirectJoinActivity() {
        Intent intent = new Intent(this, JoinActivity.class);
        intent.putExtra("login","kakao");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
                            GlobalApplication.editor.putString("method", "kakao");
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
