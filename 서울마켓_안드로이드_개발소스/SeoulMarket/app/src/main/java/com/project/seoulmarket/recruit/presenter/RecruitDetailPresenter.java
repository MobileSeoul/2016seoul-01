package com.project.seoulmarket.recruit.presenter;

import com.project.seoulmarket.recruit.model.AddReview;

/**
 * Created by kh on 2016. 10. 26..
 */
public interface RecruitDetailPresenter {
    void getDetailData(String id);
    void addReview(String id, AddReview addReview);
}
