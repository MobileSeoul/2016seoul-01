package com.project.seoulmarket.detail;

import com.project.seoulmarket.detail.model.Result;
import com.project.seoulmarket.detail.model.ReviewData;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 24..
 */
public interface DetailView {
    void setDetailData(Result itemDatas);
    void addReviewData(ArrayList<ReviewData> reviewDatas);
    void setLikeHeart();
    void setDeleteHeart();
}
