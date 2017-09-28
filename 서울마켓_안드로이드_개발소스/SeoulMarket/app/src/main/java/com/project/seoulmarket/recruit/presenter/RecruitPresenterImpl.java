package com.project.seoulmarket.recruit.presenter;

import com.project.seoulmarket.application.GlobalApplication;
import com.project.seoulmarket.mypage.model.RecruitDetailData;
import com.project.seoulmarket.mypage.model.RecruitResult;
import com.project.seoulmarket.recruit.view.RecruitView;
import com.project.seoulmarket.service.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kh on 2016. 10. 26..
 */
public class RecruitPresenterImpl implements RecruitPresenter {
    RecruitView view;
    NetworkService networkService;

    public RecruitPresenterImpl(RecruitView view) {
        this.view = view;
        this.networkService = GlobalApplication.getInstance().getNetworkService();
    }

    @Override
    public void getRecruitListDatas(final String pageNum) {
        Call<RecruitResult> getListData = networkService.getRecruitSellerListData(pageNum);
        getListData.enqueue(new Callback<RecruitResult>() {
            @Override
            public void onResponse(Call<RecruitResult> call, Response<RecruitResult> response) {
                if (response.isSuccessful()){
                    //result[0],[1].....
                    ArrayList<RecruitDetailData> getDatas = response.body().result;

                    if(getDatas.size() > 0 )
                        view.setRecruitData(getDatas);
                    else {
                        if (Integer.valueOf(pageNum) > 0)
                            ;
                        else
                            view.DataNull();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecruitResult> call, Throwable t) {

            }
        });
    }
}
