package com.project.seoulmarket.main.model;

/**
 * Created by kh on 2016. 10. 25..
 */
public class MarketFilterData {
    public String market_idx;
    public String market_address;
    public String market_state;
    public String market_name;
    public String image_url;
    public String market_count;
    public String market_startdate;
    public String market_enddate;

    public MarketFilterData(String market_idx, String market_address, String market_state, String market_name, String image, String market_count, String market_startdate, String market_enddate) {
        this.market_idx = market_idx;
        this.market_address = market_address;
        this.market_state = market_state;
        this.market_name = market_name;
        this.image_url = image;
        this.market_count = market_count;
        this.market_startdate = market_startdate;
        this.market_enddate = market_enddate;
    }
}
