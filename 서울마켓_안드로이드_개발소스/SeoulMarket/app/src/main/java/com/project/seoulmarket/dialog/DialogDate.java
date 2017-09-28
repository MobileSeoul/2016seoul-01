package com.project.seoulmarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.project.seoulmarket.R;
import com.project.seoulmarket.main.view.MainView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by KyoungHyun on 16. 5. 1..
 */
public class DialogDate extends Dialog implements com.andexert.calendarlistview.library.DatePickerController{

    @BindView(R.id.sendPerson)
    Button sendBtn;
    @BindView(R.id.pickerView)
    com.andexert.calendarlistview.library.DayPickerView pickerView;

    MainView mainView;

    String startDate;
    String endDate;
    SimpleDateFormat dateFormat;

    private View.OnClickListener sendPersonEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date);

        ButterKnife.bind(this);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        pickerView.setController(this);
        sendBtn.setOnClickListener(sendPersonEvent);
    }

    public DialogDate(Context context, MainView view, View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.mainView = view;
        this.sendPersonEvent = BtnEvent;
    }

    public void onBackPressed() {
//        Log.i("myTag","cancel");
        mainView.cancelDateDialog();
    }

    @Override
    public int getMaxYear()
    {
        return 2017;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day)
    {
        Log.i("myTag","Day Selected "+day + " / " + month + " / " + year);
    }

    @Override
    public void onDateRangeSelected(com.andexert.calendarlistview.library.SimpleMonthAdapter.SelectedDays<com.andexert.calendarlistview.library.SimpleMonthAdapter.CalendarDay> selectedDays) {
        Log.i("myTag","Date range selected "+ selectedDays.getFirst().toString() + " --> " + selectedDays.getLast().toString());

        startDate = dateFormat.format(selectedDays.getFirst().getDate());
        endDate = dateFormat.format(selectedDays.getLast().getDate());

    }

    public String getStartDate(){
        return startDate;
    }
    public String getEndDate(){
        return endDate;
    }


}
