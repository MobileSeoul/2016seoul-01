package com.project.seoulmarket.detail.presenter;

/**
 * Created by kh on 2016. 10. 24..
 */
public interface DetailPresenter {
    void getDetail(String id,String pageNum);
    void requestLikeFavorite(String id);
    void requestDeleteFavorite(String id);
}
