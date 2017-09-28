package com.project.seoulmarket.mypage.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.seoulmarket.R;
import com.project.seoulmarket.mypage.model.RecruitDetailData;
import com.project.seoulmarket.mypage.view.MyPageView;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 5..
 */

public class MyPageRecruitAdapter extends RecyclerView.Adapter<MyPageRecruitViewHolder> {

    private ArrayList<RecruitDetailData> itemDatas;
    private View itemView;
    private ViewGroup parent;

    private MyPageView myView;

    public MyPageRecruitAdapter(ArrayList<RecruitDetailData> itemDatas, MyPageView myView){
        this.itemDatas = itemDatas;
        this.myView = myView;
    }

    //ViewHolder 생성
    @Override
    public MyPageRecruitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.parent = parent;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_cardview_recruit, parent,false);
        MyPageRecruitViewHolder viewHolder = new MyPageRecruitViewHolder(itemView, myView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyPageRecruitViewHolder holder, int position) {
        holder.position = position;
        holder.mId = itemDatas.get(position).recruitment_idx;
        holder.mTitle.setText(itemDatas.get(position).recruitment_title);
        holder.mDate.setText(itemDatas.get(position).recruitment_uploadtime);
        holder.mCount.setText(itemDatas.get(position).count);

    }


    @Override
    public int getItemCount() {
        return (itemDatas != null) ? itemDatas.size() : 0;
    }
}
