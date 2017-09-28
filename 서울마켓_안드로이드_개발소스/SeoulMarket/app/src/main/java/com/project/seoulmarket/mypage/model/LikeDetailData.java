package com.project.seoulmarket.mypage.model;

/**
 * Created by kh on 2016. 10. 26..
 */
public class LikeDetailData {
    public String user_nickname;
    public String idx;
    public String address;
    public String state;
    public String image;
    public String marketname;
    public String market_state;
    public String market_count;
    public String market_startdate;
    public String market_enddate;
    public String favorite;

    public LikeDetailData(String user_nickname, String idx, String address, String state, String image, String marketname, String market_state, String market_count, String market_startdate, String market_enddate, String favorite) {
        this.user_nickname = user_nickname;
        this.idx = idx;
        this.address = address;
        this.state = state;
        this.image = image;
        this.marketname = marketname;
        this.market_state = market_state;
        this.market_count = market_count;
        this.market_startdate = market_startdate;
        this.market_enddate = market_enddate;
        this.favorite = favorite;
    }
}
