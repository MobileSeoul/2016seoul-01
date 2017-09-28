package com.project.seoulmarket.mypage.presenter;

import android.util.Log;

import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.mypage.model.LikeDetailData;
import com.project.seoulmarket.mypage.model.LikeResult;
import com.project.seoulmarket.mypage.model.RecruitDetailData;
import com.project.seoulmarket.mypage.model.RecruitResult;
import com.project.seoulmarket.mypage.model.ReportDetailData;
import com.project.seoulmarket.mypage.model.ReportResult;
import com.project.seoulmarket.mypage.view.MyPageView;
import com.project.seoulmarket.service.NetworkService;
import com.project.seoulmarket.splash.model.ConnectResult;
import com.project.seoulmarket.splash.model.MessageResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kh on 2016. 10. 21..
 */
public class MyPagePresenterImpl implements MyPagePresenter{
    MyPageView view;
    NetworkService networkService;

    public MyPagePresenterImpl(MyPageView view) {
        this.view = view;
        networkService = GlobalApplication.getInstance().getNetworkService();
    }


    @Override
    public void getMyLikeMarketData(final String pageNum) {

        Call<LikeResult> getLikeData = networkService.getMyLikeMarketData(pageNum);

        getLikeData.enqueue(new Callback<LikeResult>() {
            @Override
            public void onResponse(Call<LikeResult> call, Response<LikeResult> response) {

                Log.i("myTag","like");

                if (response.isSuccessful()){
                    ArrayList<LikeDetailData> getDatas = response.body().result;

//                    Log.i("myTag",String.valueOf(getDatas));

                    if(getDatas.size() > 0 )
                        view.setLikeData(getDatas);
                    else{
                        if (Integer.valueOf(pageNum) > 0)
                            ;
                        else
                            view.dataNull();
                    }
                }
            }

            @Override
            public void onFailure(Call<LikeResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void getMyReportMarketData(final String pageNum) {
        Call<ReportResult> getReportData = networkService.getMyReportMarketData(pageNum);
        getReportData.enqueue(new Callback<ReportResult>() {
            @Override
            public void onResponse(Call<ReportResult> call, Response<ReportResult> response) {
                Log.i("myTag","report");

                if (response.isSuccessful()){
                    ArrayList<ReportDetailData> getDatas = response.body().result;

//                    Log.i("myTag",String.valueOf(getDatas));

                    if(getDatas.size() > 0 )
                        view.setReportData(getDatas);
                    else{
                        if (Integer.valueOf(pageNum) > 0)
                            ;
                        else
                            view.dataNull();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void getMyRecruitSellerData(final String pageNum) {
        Call<RecruitResult> getRecruitData = networkService.getMyRecruitSellerData(pageNum);
        getRecruitData.enqueue(new Callback<RecruitResult>() {
            @Override
            public void onResponse(Call<RecruitResult> call, Response<RecruitResult> response) {
                Log.i("myTag","recruit");

                if (response.isSuccessful()){
                    ArrayList<RecruitDetailData> getDatas = response.body().result;

//                    Log.i("myTag idx",String.valueOf(getDatas.get(0).recruitment_idx));

                    if(getDatas.size() > 0 )
                        view.setRecruitData(getDatas);
                    else{
                        if (Integer.valueOf(pageNum) > 0)
                            ;
                        else
                            view.dataNull();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecruitResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void deleteMyReportMarket(String mId) {
        Call<ConnectResult> requestDelete = networkService.requestDeleteMarket(mId);
        requestDelete.enqueue(new Callback<ConnectResult>() {
            @Override
            public void onResponse(Call<ConnectResult> call, Response<ConnectResult> response) {
                if (response.isSuccessful()){
                    MessageResult result = response.body().result;
                    if(result.message.equals("Success"))
                        view.successDeleteReport();
                    else
                        view.NetworkError();
                }
                else
                    view.NetworkError();
            }

            @Override
            public void onFailure(Call<ConnectResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void deleteMyRecruitMarket(String mId) {
        Call<ConnectResult> requestDelete = networkService.requestDeleteSeller(mId);
        requestDelete.enqueue(new Callback<ConnectResult>() {
            @Override
            public void onResponse(Call<ConnectResult> call, Response<ConnectResult> response) {
                if (response.isSuccessful()){
                    MessageResult result = response.body().result;
                    Log.i("myTag", String.valueOf(result));
                    if(result.message.equals("Success"))
                        view.successDeleteRecruit();
                    else
                        view.NetworkError();
                }
                else
                    view.NetworkError();
            }

            @Override
            public void onFailure(Call<ConnectResult> call, Throwable t) {
                Log.i("myTag", String.valueOf(t));
            }
        });
    }
}
