package com.project.seoulmarket.recruit.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.seoulmarket.R;
import com.project.seoulmarket.recruit.model.RecruitReviewDetailData;

import java.util.ArrayList;

/**
 * Created by lee on 2016-10-23.
 */

public class RecruitReviewAdapter extends BaseAdapter{

    private ArrayList<RecruitReviewDetailData> itemDatas = null;
    private LayoutInflater layoutInflater = null;

    //생성자
    public RecruitReviewAdapter(ArrayList<RecruitReviewDetailData> itemDatas, Context ctx){
        this.itemDatas = itemDatas;
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItemDatas(ArrayList<RecruitReviewDetailData> itemDatas){
        this.itemDatas = itemDatas;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return itemDatas != null ? itemDatas.size():0;
    }

    @Override
    public Object getItem(int position) {
        return (itemDatas != null && ( position>=0 && position < itemDatas.size()) ? itemDatas.get(position):null);
    }

    @Override
    public long getItemId(int position) {
        return (itemDatas != null && ( position>=0 && position < itemDatas.size()) ? position:0);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.review_detail_list_review_item,parent,false);

            viewHolder.Textview_nickname = (TextView)convertView.findViewById(R.id.reviewNickname);
            viewHolder.Textview_content = (TextView)convertView.findViewById(R.id.reviewContent);
            viewHolder.Textview_date = (TextView)convertView.findViewById(R.id.reviewDate);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RecruitReviewDetailData itemData = itemDatas.get(position);

        viewHolder.Textview_nickname.setText(itemData.name);
        viewHolder.Textview_content.setText(itemData.content);
        viewHolder.Textview_date.setText(itemData.date);

        return convertView;

    }

    public class ViewHolder {
        TextView Textview_nickname;
        TextView Textview_content;
        TextView Textview_date;
    }
}
