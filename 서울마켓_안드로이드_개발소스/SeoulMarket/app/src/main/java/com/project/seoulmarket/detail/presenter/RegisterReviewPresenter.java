package com.project.seoulmarket.detail.presenter;

import android.content.Intent;

/**
 * Created by kh on 2016. 10. 26..
 */
public interface RegisterReviewPresenter {
    void requestAddReview(String idx, String contents, String imgUrl,Intent data);
}
