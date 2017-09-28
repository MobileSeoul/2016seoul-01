package com.project.seoulmarket.recruit.view;

import com.project.seoulmarket.recruit.model.DetailData;

/**
 * Created by kh on 2016. 10. 26..
 */
public interface RecruitDetailView {
    void setRecruitDetailData(DetailData getData);
    void addReviewArea();
    void NetworkError();
}
