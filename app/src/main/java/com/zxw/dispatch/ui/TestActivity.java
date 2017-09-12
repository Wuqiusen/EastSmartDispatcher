package com.zxw.dispatch.ui;

import android.os.Bundle;
import android.view.View;

import com.zxw.data.bean.DriverWorkloadItem;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.commucation.ConnectUtil;
import com.zxw.dispatch.module.OperatorModule;
import com.zxw.dispatch.module.ScheduleModule;
import com.zxw.dispatch.ui.base.BaseHeadActivity;
import com.zxw.dispatch.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.imlib.model.Conversation;
import rx.Subscriber;

public class TestActivity extends BaseHeadActivity implements ScheduleModule.OnLoadingListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//{"code":200,"userId":"002","token":"0tbBlcs4EiUmacu03i+uUFqTWmUx1yakeXAwiP7BXEXjt568leBi8Pu6NiNEve+lAEf6BVorIv3uyNGYA1TCMg=="}
        String token = "0tbBlcs4EiUmacu03i+uUFqTWmUx1yakeXAwiP7BXEXjt568leBi8Pu6NiNEve+lAEf6BVorIv3uyNGYA1TCMg==";
        ConnectUtil.connect(MyApplication.mContext.getApplicationInfo(), mContext, token);
    }

    public void call(View view){
        String rongId = "001";
        ArrayList<String> userList = new ArrayList<>();
        userList.add(rongId);
        String callId = RongCallClient.getInstance().startCall(Conversation.ConversationType.PRIVATE, rongId, userList, RongCallCommon.CallMediaType.AUDIO, null);
        DebugLog.w("callId " + callId);
    }
    public void hangUp(View view){
        String callId = RongCallClient.getInstance().getCallSession().getCallId();
        DebugLog.w("hangUp callId " + callId);
        RongCallClient.getInstance().hangUpCall(callId);

    }
    public void accept(View view){

        String callId = RongCallClient.getInstance().getCallSession().getCallId();
        DebugLog.w("accept callId " + callId);
        RongCallClient.getInstance().acceptCall(callId);
    }
}
