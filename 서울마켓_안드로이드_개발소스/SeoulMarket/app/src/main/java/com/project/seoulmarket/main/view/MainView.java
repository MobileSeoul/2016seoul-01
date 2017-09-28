package com.project.seoulmarket.main.view;

import com.project.seoulmarket.main.model.MarketFilterData;
import com.project.seoulmarket.main.model.MarketFirstData;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 6..
 */
public interface MainView {
    void checkDate();
    void prevousStart();
    void prevousEnd();
    void nextEnd();
    void DataNull();
    void FilterDataNull();
    void moveDetailPage(String id);
    void firstSetData(ArrayList<MarketFirstData> getDatas);
    void filterSetData(String fName, ArrayList<MarketFilterData> getDatas);
    void filterSetData(String address,String startDate, String endDate, ArrayList<MarketFilterData> getDatas);
    void cancelLocationDialog();
    void cancelDateDialog();

}
