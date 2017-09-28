package com.project.seoulmarket.recruit.presenter;

import android.util.Log;

import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.recruit.model.AddReview;
import com.project.seoulmarket.recruit.model.DetailData;
import com.project.seoulmarket.recruit.model.ResultRecruitDetail;
import com.project.seoulmarket.recruit.view.RecruitDetailView;
import com.project.seoulmarket.service.NetworkService;
import com.project.seoulmarket.splash.model.ConnectResult;
import com.project.seoulmarket.splash.model.MessageResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kh on 2016. 10. 26..
 */
public class RecruitDetailPresenterImpl implements RecruitDetailPresenter {
    RecruitDetailView view;
    NetworkService networkService;

    public RecruitDetailPresenterImpl(RecruitDetailView view) {
        this.view = view;
        this.networkService = GlobalApplication.getInstance().getNetworkService();
    }

    @Override
    public void getDetailData(String id) {
        Call<ResultRecruitDetail> getRecruitData = networkService.getRecrutitDetailData(id);
        getRecruitData.enqueue(new Callback<ResultRecruitDetail>() {
            @Override
            public void onResponse(Call<ResultRecruitDetail> call, Response<ResultRecruitDetail> response) {
                if(response.isSuccessful()){

                    DetailData getDatas = response.body().result;

                    view.setRecruitDetailData(getDatas);
//                    Log.i("myTag", String.valueOf(getDatas.image.get(i).img_url));

                }
            }

            @Override
            public void onFailure(Call<ResultRecruitDetail> call, Throwable t) {

            }
        });

    }

    @Override
    public void addReview(String id, AddReview addReview) {
        Call<ConnectResult> addSellerReview = networkService.addSellerReview(id,addReview);
        addSellerReview.enqueue(new Callback<ConnectResult>() {
            @Override
            public void onResponse(Call<ConnectResult> call, Response<ConnectResult> response) {
                Log.i("myTag","123");
                if(response.isSuccessful()){
                    MessageResult result = response.body().result;
                    if(result.message.equals("Success"))
                        view.addReviewArea();
                    else
                        view.NetworkError();
                }
                else
                    view.NetworkError();
            }

            @Override
            public void onFailure(Call<ConnectResult> call, Throwable t) {
                view.NetworkError();
                Log.i("myTag",t.toString());
            }
        });
    }
}
