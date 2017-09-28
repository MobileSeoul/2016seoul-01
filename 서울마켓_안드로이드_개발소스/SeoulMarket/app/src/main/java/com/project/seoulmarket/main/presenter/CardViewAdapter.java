package com.project.seoulmarket.main.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.seoulmarket.R;
import com.project.seoulmarket.main.model.MarketFirstData;
import com.project.seoulmarket.main.view.MainView;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 5..
 */

public class CardViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<MarketFirstData> itemDatas;
    MainView myView;
    private View itemView;
    private ViewGroup parent;

    public CardViewAdapter(ArrayList<MarketFirstData> itemDatas, MainView myView){
        this.itemDatas = itemDatas;
        this.myView = myView;
    }

    //ViewHolder 생성
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.parent = parent;
        this.itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_cardview_basic, parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView,myView);

        return viewHolder;

    }

    //ListView의 getView()랑 동일
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.mId = itemDatas.get(position).idx;
        holder.mName.setText(itemDatas.get(position).marketname);
        holder.mLocation.setText(itemDatas.get(position).address);

//        Log.i("myTag",String.valueOf(position));
        /**
         * state > 1 : 남은날자
         * state = 0 : 진행중
         * state < 0 만료
         */

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

        ImageView imageView = (ImageView)itemView.findViewById(R.id.image);
//        Log.i("myTag",itemDatas.get(position).image);

        Glide.with(parent.getContext())
                .load(itemDatas.get(position).image)
                .thumbnail(0.3f)
                .error(R.drawable.ic_default)
                .into(holder.getImageView());


    }


    @Override
    public int getItemCount() {
        return (itemDatas != null) ? itemDatas.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
