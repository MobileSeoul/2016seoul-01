package com.project.seoulmarket.report.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.project.seoulmarket.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportStepTwoActivity extends AppCompatActivity implements com.andexert.calendarlistview.library.DatePickerController {

    @BindView(R.id.startTimeArea)
    LinearLayout startTimeArea;
    @BindView(R.id.endTimeArea)
    LinearLayout endTimeArea;
    @BindView(R.id.startHour)
    TextView startHour;
    @BindView(R.id.startMinute)
    TextView startMinute;
    @BindView(R.id.endHour)
    TextView endHour;
    @BindView(R.id.endMinute)
    TextView endMinute;
    @BindView(R.id.pickerView)
    com.andexert.calendarlistview.library.DayPickerView pickerView;

    Boolean TimeStartCheck = false;
    Boolean TimeEndCheck = false;
    Boolean CalCheck = false;

    String startDate = "";
    String endDate = "";
    String startTime = "";
    String endTime = "";

    int startCount = 0;
    int endCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_step_two);

        ButterKnife.bind(this);
        pickerView.setController(this);

        /**
         * actionbar 설정
         */

        if (Build.VERSION.SDK_INT >= 21) {   //상태바 색
            getWindow().setStatusBarColor(Color.parseColor("#FFA700"));
        }

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        getSupportActionBar().setElevation(0); // 그림자 없애기

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_back_layout, null);

        TextView actionbarTitle = (TextView)mCustomView.findViewById(R.id.mytext);
        actionbarTitle.setText("마켓 제보");
        actionbarTitle.setTypeface(Typeface.createFromAsset(getAssets(),"OTF_B.otf"));

        ImageView backBtn = (ImageView) mCustomView.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        /**
         *
         */

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.startTimeArea)
    public void setStartTime(){
        TimePickerDialog dialog = new TimePickerDialog(this, startListener, 00, 00, false);
        dialog.show();
    }

    @OnClick(R.id.endTimeArea)
    public void setEndTime(){
        TimePickerDialog dialog = new TimePickerDialog(this, endListener, 00, 00, false);
        dialog.show();

    }

    @OnClick(R.id.nextBtn)
    public void nextStopBtn(){
        //다음 단계
        if(TimeStartCheck && TimeEndCheck && CalCheck){


            if(startCount > endCount){
                String temp = startTime;
                startTime = endTime;
                endTime = temp;
            }
            Intent getData = getIntent();
            Intent intent = new Intent(getApplicationContext(),ReportStepThreeActivity.class);
            intent.putExtra("name",getData.getExtras().getString("name"));
            intent.putExtra("host",getData.getExtras().getString("host"));
            intent.putExtra("content",getData.getExtras().getString("content"));
            intent.putExtra("startDate",startDate);
            intent.putExtra("startTime",startTime);
            intent.putExtra("endDate",endDate);
            intent.putExtra("endTime",endTime);

            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"시간 및 날짜를 확인해주세요.",Toast.LENGTH_SHORT).show();
        }

    }

    private TimePickerDialog.OnTimeSetListener startListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // 설정버튼 눌렀을 때
            if(hourOfDay < 10)
                startHour.setText("0"+String.valueOf(hourOfDay));
            else
                startHour.setText(String.valueOf(hourOfDay));

            if(minute < 10)
                startMinute.setText("00");
            else
                startMinute.setText(String.valueOf(Math.round(minute*0.1)*10));

            startCount = Integer.valueOf(startHour.getText().toString()) * 60 + Integer.valueOf(startMinute.getText().toString());
            startTime = startHour.getText().toString() + ":" + startMinute.getText().toString();
            TimeStartCheck = true;
        }
    };


    private TimePickerDialog.OnTimeSetListener endListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // 설정버튼 눌렀을 때
            if(hourOfDay < 10)
                endHour.setText("0"+String.valueOf(hourOfDay));
            else
                endHour.setText(String.valueOf(hourOfDay));

            if(minute < 10)
                endMinute.setText("00");
            else
                endMinute.setText(String.valueOf(Math.round(minute*0.1)*10));

            endCount = Integer.valueOf(endHour.getText().toString()) * 60 + Integer.valueOf(endMinute.getText().toString());
            endTime = endHour.getText().toString() + ":" + endMinute.getText().toString();
            TimeEndCheck = true;

        }
    };

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
//        Log.i("myTag","Date range selected "+ selectedDays.getFirst().toString() + " --> " + selectedDays.getLast().toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        startDate = dateFormat.format(selectedDays.getFirst().getDate());
        endDate = dateFormat.format(selectedDays.getLast().getDate());

        CalCheck = true;
    }

}
