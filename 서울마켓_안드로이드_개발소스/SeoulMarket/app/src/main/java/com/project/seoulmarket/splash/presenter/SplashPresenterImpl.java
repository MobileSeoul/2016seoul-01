package com.project.seoulmarket.splash.presenter;


import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.service.NetworkService;
import com.project.seoulmarket.splash.model.ConnectResult;
import com.project.seoulmarket.splash.view.SplashView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashPresenterImpl implements SplashPresenter {

    SplashView view;

    public SplashPresenterImpl(SplashView view) {
        this.view = view;
    }

    @Override
    public void connectServer() {
        NetworkService networkService = GlobalApplication.getInstance().getNetworkService();

        Call<ConnectResult> loginTest = networkService.getConnection();

        loginTest.enqueue(new Callback<ConnectResult>() {
            @Override
            public void onResponse(Call<ConnectResult> call, Response<ConnectResult> response) {
//                Log.i("myTag", String.valueOf(response.code()));
                view.connectingSucceed(response.code());
            }

            @Override
            public void onFailure(Call<ConnectResult> call, Throwable t) {
                view.networkError();
            }

        });
    }

}
