package com.project.seoulmarket.mypage.model;

/**
 * Created by kh on 2016. 10. 26..
 */
public class RecruitDetailData {
    public String recruitment_idx;
    public String recruitment_title;
    public String recruitment_image;
    public String user_nickname;
    public String recruitment_uploadtime;
    public String count;

    public RecruitDetailData(String recruitment_idx, String recruitment_title, String recruitment_image, String user_nickname, String recruitment_uploadtime, String count) {
        this.recruitment_idx = recruitment_idx;
        this.recruitment_title = recruitment_title;
        this.recruitment_image = recruitment_image;
        this.user_nickname = user_nickname;
        this.recruitment_uploadtime = recruitment_uploadtime;
        this.count = count;
    }
}
