package com.project.seoulmarket.detail.presenter;

import android.util.Log;

import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.detail.DetailView;
import com.project.seoulmarket.detail.model.DetailResultData;
import com.project.seoulmarket.detail.model.FavoriteResult;
import com.project.seoulmarket.detail.model.Result;
import com.project.seoulmarket.service.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kh on 2016. 10. 24..
 */
public class DetailPresenterImpl implements DetailPresenter {

    DetailView view;
    NetworkService networkService;

    public DetailPresenterImpl(DetailView view) {
        this.view = view;
        networkService = GlobalApplication.getInstance().getNetworkService();
    }

    @Override
    public void getDetail(String id, final String pageNum) {
        Call<DetailResultData> detailData = networkService.getDetailData(id,pageNum);
        detailData.enqueue(new Callback<DetailResultData>() {
            @Override
            public void onResponse(Call<DetailResultData> call, Response<DetailResultData> response) {
                if(response.isSuccessful()){
                    Result getDatas = response.body().result;
//                    Log.i("myTag", String.valueOf(getDatas.market_name));
                    if(Integer.valueOf(pageNum) == 0)
                        view.setDetailData(getDatas);
                    else
                        view.addReviewData(getDatas.review);
                }
            }

            @Override
            public void onFailure(Call<DetailResultData> call, Throwable t) {
                Log.i("myTag fail",t.toString());
            }
        });

    }

    @Override
    public void requestLikeFavorite(String id) {

        Call<FavoriteResult> requestLike = networkService.requestLikeFavorite(id);
        requestLike.enqueue(new Callback<FavoriteResult>() {
            @Override
            public void onResponse(Call<FavoriteResult> call, Response<FavoriteResult> response) {


                if(response.isSuccessful()){

//                    Log.i("myTag",String.valueOf(response.body().result));

                    FavoriteResult.FavoriteDetailData getDatas = response.body().result;

                    Log.i("myTag",String.valueOf(getDatas.message));

                    if (getDatas.message != null){
                        if(getDatas.message.equals("Success"))
                            view.setLikeHeart();
                    }
                }
                else
                    Log.i("myTag",response.body() + " " + response.code());

            }

            @Override
            public void onFailure(Call<FavoriteResult> call, Throwable t) {
                Log.i("myTag",String.valueOf(t));
            }
        });
    }

    @Override
    public void requestDeleteFavorite(String id) {
        Call<FavoriteResult> requestLike = networkService.requestDeleteFavorite(id);
        requestLike.enqueue(new Callback<FavoriteResult>() {
            @Override
            public void onResponse(Call<FavoriteResult> call, Response<FavoriteResult> response) {


                if(response.isSuccessful()){

//                    Log.i("myTag",String.valueOf(response.body().result));

                    FavoriteResult.FavoriteDetailData getDatas = response.body().result;

                    Log.i("myTag",String.valueOf(getDatas.message));

                    if (getDatas.message != null){
                        if(getDatas.message.equals("Success"))
                            view.setDeleteHeart();
                    }
                }
                else
                    Log.i("myTag",response.body() + " " + response.code());

            }

            @Override
            public void onFailure(Call<FavoriteResult> call, Throwable t) {
                Log.i("myTag",String.valueOf(t));
            }
        });
    }


}
