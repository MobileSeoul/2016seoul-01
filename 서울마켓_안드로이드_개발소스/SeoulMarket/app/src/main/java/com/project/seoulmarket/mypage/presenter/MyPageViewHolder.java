package com.project.seoulmarket.mypage.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.seoulmarket.R;
import com.project.seoulmarket.mypage.view.MyPageView;

/**
 * Created by kh on 2016. 10. 5..
 */
public class MyPageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private MyPageView myView;

    public String mId;
    public TextView mName;
    public TextView mLocation;
    public TextView mProgress;
    public ImageView mImageView;
    public TextView mDate;
    public ImageView kakaoBtn;

    public MyPageViewHolder(View itemView,MyPageView myView) {
        super(itemView);

        this.myView = myView;

        mImageView = (ImageView)itemView.findViewById(R.id.image);
        mName = (TextView)itemView.findViewById(R.id.marketName);
        mLocation = (TextView)itemView.findViewById(R.id.marketLocation);
        mProgress = (TextView)itemView.findViewById(R.id.progressRate);
        mDate = (TextView)itemView.findViewById(R.id.marketDate);
        kakaoBtn = (ImageView)itemView.findViewById(R.id.kakaoBtn);

        kakaoBtn.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kakaoBtn:
                myView.sendKakao(mId);
                break;
            default:
                myView.moveDetailPage(mId);
                break;
        }

    }

    public ImageView getImageView(){
        return mImageView;
    }
}