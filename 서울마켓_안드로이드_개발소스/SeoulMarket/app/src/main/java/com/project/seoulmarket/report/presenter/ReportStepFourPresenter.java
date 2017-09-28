package com.project.seoulmarket.report.presenter;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 27..
 */
public interface ReportStepFourPresenter {
    void addReportMarket(
            String market_name,
            String market_address,
            String market_host,
            String market_contents,
            String market_tag,
            String market_longitude,
            String market_latitude,
            String market_tell,
            String market_startdate,
            String market_enddate,
            String market_url,
            ArrayList images,
            Intent dataList1,
            Intent dataList2,
            Intent dataList3,
            Intent dataList4,
            Intent dataList5,
            Intent dataList6
    );
}
