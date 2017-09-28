package com.project.seoulmarket.mypage.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.seoulmarket.R;
import com.project.seoulmarket.mypage.model.LikeDetailData;
import com.project.seoulmarket.mypage.view.MyPageView;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 5..
 */

public class MyPageAdapter extends RecyclerView.Adapter<MyPageViewHolder> {

    private ArrayList<LikeDetailData> itemDatas;
    private View itemView;
    private ViewGroup parent;

    private MyPageView myView;

    public MyPageAdapter(ArrayList<LikeDetailData> itemDatas, MyPageView myView){
        this.itemDatas = itemDatas;
        this.myView = myView;
    }

    //ViewHolder 생성
    @Override
    public MyPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.parent = parent;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_cardview_like, parent,false);
        MyPageViewHolder viewHolder = new MyPageViewHolder(itemView, myView);

        return viewHolder;

    }

    //ListView의 getView()랑 동일
    @Override
    public void onBindViewHolder(MyPageViewHolder holder, int position) {

        holder.mId = itemDatas.get(position).idx;
        holder.mName.setText(itemDatas.get(position).marketname);
        holder.mLocation.setText(itemDatas.get(position).address);

        int state = Integer.valueOf(itemDatas.get(position).state);

        if( state > 0){
            holder.mProgress.setText("D-" + state);
            holder.mProgress.setBackgroundResource(R.drawable.progress_background);
        }
        else if(state == 0){
            holder.mProgress.setText("진행중");
            holder.mProgress.setBackgroundResource(R.drawable.progress_background);
        }
        else{
            holder.mProgress.setText("만료");
            holder.mProgress.setBackgroundResource(R.drawable.progress_background);
        }

        String startTemp = itemDatas.get(position).market_startdate.replace("-",".");
        String endTemp = itemDatas.get(position).market_enddate.replace("-",".");

        holder.mDate.setText(startTemp +"~" + endTemp);

        ImageView imageView = (ImageView)itemView.findViewById(R.id.image);

        Glide.with(parent.getContext())
                .load(itemDatas.get(position).image)
                .thumbnail(0.3f)
                .error(R.drawable.ic_default)
                .into(holder.getImageView());

//        Glide.clear(imageView);

    }


    @Override
    public int getItemCount() {
        return (itemDatas != null) ? itemDatas.size() : 0;
    }


}
