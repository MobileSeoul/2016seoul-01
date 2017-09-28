package com.project.seoulmarket.mypage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.project.seoulmarket.R;

/**
 * Created by kh on 2016. 10. 21..
 */
public class MyPageInfoFragment extends Fragment {
    private MyPageView myView;

    public MyPageInfoFragment(MyPageView myView) {
        this.myView = myView;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout view = (LinearLayout)inflater.inflate(R.layout.content_my_page_info, container, false);
        myView.makeInfoView(view);

        return view;
    }
}

