package com.project.seoulmarket.recruit.model;

/**
 * Created by kh on 2016. 10. 23..
 */
public class RecruitReviewDetailData {
    //data
    public String name; //글쓴이
    public String date; //날짜
    public String content;

    public RecruitReviewDetailData(String name, String date, String content) {
        this.name = name;
        this.date = date;
        this.content = content;
    }

}
