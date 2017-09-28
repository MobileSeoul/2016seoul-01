package com.project.seoulmarket.recruit.view;

import com.project.seoulmarket.mypage.model.RecruitDetailData;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 26..
 */
public interface RecruitView {
    void setRecruitData(ArrayList<RecruitDetailData> getDatas);
    void DataNull();
}
