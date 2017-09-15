package com.zxw.data.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by wuqiusen on 2017/9/14.
 */

public class PollingUtil {
    private int poi = 1;
    private boolean isSuccess = false;

    public PollingUtil(Context context, final PollingCallBack pollingCallBack, final int eachTime, final int overTime) {
        poi = 1;

        new Thread() {
            @Override
            public void run() {
                super.run();
                while (!isSuccess) {
                    if (poi * eachTime < overTime){
                        pollingCallBack.callBack();
                        try {
                            Thread.sleep(eachTime * poi * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e("polling:", poi + "-------------");
                        poi++;
                    }else {
                        isSuccess = true;
                    }
                }
            }
        }.start();
    }

    public void setSuccess() {
        isSuccess = true;
        poi = 1;
    }

    public interface PollingCallBack {
        void callBack();
    }
}
