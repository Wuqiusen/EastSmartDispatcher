package com.zxw.dispatch.commucation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;


import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.utils.DebugLog;

import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.RongIMClient;

/**
 * Created by 李振强 on 2017/7/19.
 */

public class ConnectUtil {

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    public static void connect(ApplicationInfo info, Context context, String token) {

        if (info.packageName.equals(MyApplication.getCurProcessName(context))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("ConnectUtil", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.d("ConnectUtil", "--onSuccess---" + userid);
                    setReceivedCallListener();

                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("ConnectUtil", "--onError" + errorCode);
                }
            });
        }
    }

    private static void setReceivedCallListener() {
        RongCallClient.setReceivedCallListener(new IRongReceivedCallListener() {
            /**
             * 来电回调
             * @param callSession 通话实体
             */
            @Override
            public void onReceivedCall(RongCallSession callSession) {
                String callId = callSession.getCallId();
                DebugLog.w("callId " + callId);
                startCallActivity(callId, true);
            }
            @Override
            public void onCheckPermission(RongCallSession callSession) {
            }
        });
    }

    private static void startCallActivity(String currentCallId, boolean isCalled){
        Intent intent = new Intent(MyApplication.mContext, RealCallActivity.class);
        intent.putExtra("callId", currentCallId);
        intent.putExtra("isCalled", isCalled);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.mContext.startActivity(intent);
    }
}
