package com.project.seoulmarket.mypage.view;

import android.widget.LinearLayout;

import com.project.seoulmarket.mypage.model.LikeDetailData;
import com.project.seoulmarket.mypage.model.RecruitDetailData;
import com.project.seoulmarket.mypage.model.ReportDetailData;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 21..
 */
public interface MyPageView {

    void makeLikeView(LinearLayout view);
    void makeReportView(LinearLayout view);
    void makeRecruitView(LinearLayout view);
    void makeInfoView(LinearLayout view);
    void dataNull();
    void setLikeData(ArrayList<LikeDetailData> getDatas);
    void setReportData(ArrayList<ReportDetailData> getDatas);
    void setRecruitData(ArrayList<RecruitDetailData> getDatas);

    void moveDetailPage(String mId);
    void moveRecruitPage(String mId);
    void deleteReport(int position, String mId);
    void deleteRecruit(int position, String mId);
    void sendKakao(String mId);

    void successDeleteRecruit();
    void successDeleteReport();
    void NetworkError();


    void requestDeleteReport(int position, String mId);
    void cancelDialog();
}
