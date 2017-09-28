package com.project.seoulmarket.recruit.register;

import android.content.Context;

/**
 * Created by kh on 2016. 10. 26..
 */
public interface RecruitRegisterView {
    void successMsg();
    void errorMsg();
    Context getNowContext();
}
