package com.project.seoulmarket.application;

/**
 * Created by kh on 2016. 10. 7..
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kakao.auth.KakaoSDK;
import com.project.seoulmarket.login.adapter.KakaoSDKAdapter;
import com.project.seoulmarket.login.cookies.PersistentCookieStore;
import com.project.seoulmarket.service.NetworkService;
import com.tsengvn.typekit.Typekit;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 이미지를 캐시를 앱 수준에서 관리하기 위한 애플리케이션 객체이다.
 * 로그인 기반 샘플앱에서 사용한다.
 *
 * @author MJ
 */
public class GlobalApplication extends Application {
    private static volatile GlobalApplication instance = null;
    private static volatile Activity currentActivity = null;

    public static SharedPreferences loginInfo;
    public static SharedPreferences.Editor editor;

    private static String baseUrl = "ip address";
    private NetworkService networkService;


    PersistentCookieStore cookieStore;
    CookieManager cookieManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;

        /**
         * 폰트 설정
         */
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "OTF_R.otf"))
                .addBold(Typekit.createFromAsset(this, "OTF_B.otf"))
                .addCustom1(Typekit.createFromAsset(this, "OTF_L.otf"));
        /**
         * 쿠키 관리위한 부분
         */
        cookieStore = new PersistentCookieStore(getApplicationContext());
        cookieManager = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);

        /**
         * 로그인 api 설정
         */
        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //kakao
        KakaoSDK.init(new KakaoSDKAdapter());

        /**
         * SharedPreference 설정
         */
        loginInfo = getSharedPreferences("login_info", 0);
        editor= loginInfo.edit();

        /**
         * build service
         */

        GlobalApplication.instance = this;
        this.buildService();

    }

    public void buildService() {


        OkHttpClient client =  new OkHttpClient().newBuilder()
                .connectTimeout(7676, TimeUnit.SECONDS)
                .writeTimeout(7676, TimeUnit.SECONDS)
                .readTimeout(7676, TimeUnit.SECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();

        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        networkService = retrofit.create(NetworkService.class);
    }

    public static GlobalApplication getInstance() {
        return instance;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static GlobalApplication getGlobalApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//    /**
//     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
//     */
//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//        instance = null;
//    }
}

