package com.project.seoulmarket.mypage.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.project.seoulmarket.R;
import com.project.seoulmarket.mypage.view.MyPageView;

/**
 * Created by kh on 2016. 10. 5..
 */
public class MyPageRecruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private MyPageView myView;

    public int position;
    public String mId;
    public TextView mTitle;
    public TextView mDate;
    public TextView mCount;
    public TextView deleteBtn;

    public MyPageRecruitViewHolder(View itemView, MyPageView myView) {
        super(itemView);

        this.myView = myView;

        mTitle = (TextView)itemView.findViewById(R.id.recruitTitle);
        mDate = (TextView)itemView.findViewById(R.id.recruitDate);
        mCount = (TextView)itemView.findViewById(R.id.recruitCount);
        deleteBtn = (TextView)itemView.findViewById(R.id.deleteRecruit);

        deleteBtn.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deleteRecruit:
                myView.deleteRecruit(position,mId);
                break;
            default:
                myView.moveRecruitPage(mId);
                break;
        }
    }
}