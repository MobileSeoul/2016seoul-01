package com.project.seoulmarket.recruit.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.seoulmarket.R;
import com.project.seoulmarket.mypage.model.RecruitDetailData;

import java.util.ArrayList;

/**
 * Created by woody on 2016-04-23.
 */
public class RecruitAdapter extends BaseAdapter {

    // 아무것도 없을 때 빨간 줄
    // 기본적으로 무조건 적어줘야 하는 Override 메소드가 있기 때문 !!

    private ArrayList<RecruitDetailData> itemDatas = null;
    private LayoutInflater layoutInflater = null;

    //생성자
    public RecruitAdapter(ArrayList<RecruitDetailData> itemDatas, Context ctx){
        this.itemDatas = itemDatas;
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItemDatas(ArrayList<RecruitDetailData> itemDatas){
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



    /**
     * ViewHolder Pattern을 이용할 경우
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.detail_list_review_item,parent,false);

            viewHolder.Textview_nickname = (TextView)convertView.findViewById(R.id.reviewNickname);
            viewHolder.Textview_content = (TextView)convertView.findViewById(R.id.reviewContent);
            viewHolder.Textview_date = (TextView)convertView.findViewById(R.id.reviewDate);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RecruitDetailData itemData = itemDatas.get(position);

        viewHolder.Textview_nickname.setText(itemData.user_nickname);
        viewHolder.Textview_content.setText(itemData.recruitment_title);
        viewHolder.Textview_date.setText(itemData.recruitment_uploadtime);

        return convertView;
    }

    public class ViewHolder {
        TextView Textview_nickname;
        TextView Textview_content;
        TextView Textview_date;
    }

}
