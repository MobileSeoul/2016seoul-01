package com.project.seoulmarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.project.seoulmarket.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by KyoungHyun on 16. 5. 1..
 */
public class DialogName extends Dialog {

    @BindView(R.id.nameEdit)
    EditText nameEditArea;

    @BindView(R.id.sendName)
    Button sendNameBtn;


    private View.OnClickListener sendLocationEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_name);

        ButterKnife.bind(this);
        sendNameBtn.setOnClickListener(sendLocationEvent);


    }

    public DialogName(Context context, View.OnClickListener BtnEvent) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.sendLocationEvent = BtnEvent;

    }



    public String getName(){

        String name = nameEditArea.getText().toString();
        return name;
    }

}

