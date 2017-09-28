package com.project.seoulmarket.main.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.seoulmarket.R;
import com.project.seoulmarket.main.model.MarketFilterData;
import com.project.seoulmarket.main.view.MainView;

import java.util.ArrayList;

/**
 * Created by kh on 2016. 10. 25..
 */
public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewHolder> {
    private ArrayList<MarketFilterData> itemDatas;

    MainView myView;
    private View itemView;
    private ViewGroup parent;

    public FilterViewAdapter(ArrayList<MarketFilterData> itemDatas, MainView myView){
        this.itemDatas = itemDatas;
        this.myView = myView;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        this.itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_cardview_filter, parent,false);

        FilterViewHolder viewHolder = new FilterViewHolder(itemView,myView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {
        holder.mId = itemDatas.get(position).market_idx;
        holder.mName.setText(itemDatas.get(position).market_name);
        holder.mLocation.setText(itemDatas.get(position).market_address);

        String startTemp = itemDatas.get(position).market_startdate.replace("-",".");
        String endTemp = itemDatas.get(position).market_enddate.replace("-",".");

        holder.mDate.setText(startTemp + "~" + endTemp);
        holder.mLike.setText(itemDatas.get(position).market_count);

        // TODO: 2016. 10. 5. 아직 데이터가 없으므로 임시로 넣어주기로.
        /**
         * state > 1 : 남은날자
         * state = 0 : 당일
         * state < 0 만료
         */

        int state = Integer.valueOf(itemDatas.get(position).market_state);

        if( state > 0){
            holder.mProgress.setText("D-" + state);
            holder.mProgress.setBackgroundResource(R.drawable.progress_background);
        }
        else if(state == 0){
            holder.mProgress.setText("D-day");
            holder.mProgress.setBackgroundResource(R.drawable.progress_background);
        }
        else{
            holder.mProgress.setText("만료");
            holder.mProgress.setBackgroundResource(R.drawable.progress_background);
        }

        ImageView imageView = (ImageView)itemView.findViewById(R.id.image);

//        Log.i("myTag",itemDatas.get(position).image);

        Glide.with(parent.getContext())
                .load(itemDatas.get(position).image_url)
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
