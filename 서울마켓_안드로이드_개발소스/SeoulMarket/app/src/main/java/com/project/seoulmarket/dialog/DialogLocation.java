package com.project.seoulmarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.seoulmarket.R;
import com.project.seoulmarket.main.view.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by KyoungHyun on 16. 5. 1..
 */
public class DialogLocation extends Dialog {

    @BindView(R.id.sendLocation)
    Button sendLocationBtn;

    @BindView(R.id.item1)
    TextView item1;
    @BindView(R.id.item2)
    TextView item2;
    @BindView(R.id.item3)
    TextView item3;
    @BindView(R.id.item4)
    TextView item4;
    @BindView(R.id.item5)
    TextView item5;
    @BindView(R.id.item6)
    TextView item6;
    @BindView(R.id.item7)
    TextView item7;
    @BindView(R.id.item8)
    TextView item8;
    @BindView(R.id.item9)
    TextView item9;
    @BindView(R.id.item10)
    TextView item10;

    String address = "null";
    MainView view;

    private View.OnClickListener sendLocationEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_location);

        ButterKnife.bind(this);
        sendLocationBtn.setOnClickListener(sendLocationEvent);

//
        item1.setOnClickListener(getAddress);
        item2.setOnClickListener(getAddress);
        item3.setOnClickListener(getAddress);
        item4.setOnClickListener(getAddress);
        item5.setOnClickListener(getAddress);
        item6.setOnClickListener(getAddress);
        item7.setOnClickListener(getAddress);
        item8.setOnClickListener(getAddress);
        item9.setOnClickListener(getAddress);
//        item10.setOnClickListener(getAddress);

    }

    public void onBackPressed() {
//        Log.i("myTag","cancel");
        view.cancelLocationDialog();
    }

    public DialogLocation(Context context, View.OnClickListener BtnEvent,MainView view) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.sendLocationEvent = BtnEvent;
        this.view = view;

    }

    private View.OnClickListener getAddress = new View.OnClickListener() {
        public void onClick(View v) {

            item1.setBackgroundColor(0);
//            item2.setBackgroundResource(R.color.itemBack);
            item2.setBackgroundColor(0);
            item3.setBackgroundColor(0);
            item4.setBackgroundColor(0);
            item5.setBackgroundColor(0);
            item6.setBackgroundColor(0);
            item7.setBackgroundColor(0);
            item8.setBackgroundColor(0);
            item9.setBackgroundColor(0);
            item10.setBackgroundColor(0);

            v.setBackgroundColor(Color.rgb(179,191,205));

            switch (v.getId()){
                case R.id.item1:
                    address = "강남";
                    break;
                case R.id.item2:
                    address = "광진";
                    break;
                case R.id.item3:
                    address = "송파";
                    break;

                case R.id.item4:
                    address = "노원";
                    break;
                case R.id.item5:
                    address = "강서";
                    break;
                case R.id.item6:
                    address = "종로";
                    break;
                case R.id.item7:
                    address = "영등포";
                    break;
                case R.id.item8:
                    address = "마포";
                    break;
                case R.id.item9:
                    address = "관악";
                    break;
                case R.id.item10:
                    address = "전체";
                    break;
                default:
                    address = "null";
                    break;
            }

        }
    };

    public String giveAddress(){

        return address;
    }

}

