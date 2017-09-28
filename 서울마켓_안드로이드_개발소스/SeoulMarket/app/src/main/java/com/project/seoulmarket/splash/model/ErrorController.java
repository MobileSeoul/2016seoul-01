package com.project.seoulmarket.splash.model;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Min_Mac on 16. 4. 14..
 */
public class ErrorController {
    Context context;
    String errlog;

    public ErrorController(Context context) {
        this.context = context;
    }
    public ErrorController(Context context, String errlog) {
        this.context = context;
        this.errlog = errlog;
    }

    public void notifyNetworkError() {
        Toast.makeText(context, "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
    }
}
