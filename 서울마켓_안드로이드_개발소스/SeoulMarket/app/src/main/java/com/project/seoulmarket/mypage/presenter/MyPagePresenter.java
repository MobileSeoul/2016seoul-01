package com.project.seoulmarket.mypage.presenter;

/**
 * Created by kh on 2016. 10. 21..
 */
public interface MyPagePresenter {
    void getMyLikeMarketData(String pageNum);
    void getMyReportMarketData(String pageNum);
    void getMyRecruitSellerData(String pageNum);
    void deleteMyReportMarket(String mId);
    void deleteMyRecruitMarket(String mId);
}
