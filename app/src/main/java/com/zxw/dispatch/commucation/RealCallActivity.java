package com.zxw.dispatch.commucation;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.zxw.dispatch.R;
import com.zxw.dispatch.ui.base.BaseHeadActivity;

import java.util.concurrent.TimeUnit;

import io.rong.calllib.IRongCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RealCallActivity extends BaseHeadActivity {
    private static final String TAG = "CallActivity";
    private String callId;
    private Button accept;
    private Button hangup;
    private TextView status;
    private Subscription subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_call);

        accept = (Button) findViewById(R.id.accept);
        hangup = (Button) findViewById(R.id.hangup);
        status = (TextView) findViewById(R.id.textView);
        callId = getIntent().getStringExtra("callId");
        boolean isCalled = getIntent().getBooleanExtra("isCalled", true);
        if (!isCalled) {
            accept.setVisibility(View.INVISIBLE);
        }else{

        }
        RongCallClient.getInstance().setVoIPCallListener(new IRongCallListener() {
            /**
             * 电话已拨出。
             * 主叫端拨出电话后，通过回调 onCallOutgoing 通知当前 call 的详细信息。
             *
             * @param callSession 通话实体。
             * @param localVideo  本地 camera 信息。
             */
            @Override
            public void onCallOutgoing(RongCallSession callSession, SurfaceView localVideo) {
                Log.w(TAG, "onCallOutgoing: ");
                status.setText("正在拨号");
            }

            /**
             * 已建立通话。
             * 通话接通时，通过回调 onCallConnected 通知当前 call 的详细信息。
             *
             * @param callSession 通话实体。
             * @param localVideo  本地 camera 信息。
             */
            @Override
            public void onCallConnected(RongCallSession callSession, SurfaceView localVideo) {
                Log.w(TAG, "onCallConnected: ");
                RealCallActivity.this.onCallConnected();
            }

            /**
             * 通话结束。
             * 通话中，对方挂断，己方挂断，或者通话过程网络异常造成的通话中断，都会回调 onCallDisconnected。
             *
             * @param callSession 通话实体。
             * @param reason      通话中断原因。
             */
            @Override
            public void onCallDisconnected(RongCallSession callSession, RongCallCommon.CallDisconnectedReason reason) {
                Log.w(TAG, "onCallDisconnected: ");
                Toast.makeText(RealCallActivity.this, "通话结束", Toast.LENGTH_SHORT).show();
                status.setText("通话结束");
                finish();
            }

            /**
             * 被叫端正在振铃。
             * 主叫端拨出电话，被叫端收到请求，发出振铃响应时，回调 onRemoteUserRinging。
             *
             * @param userId 振铃端用户 id。
             */
            @Override
            public void onRemoteUserRinging(String userId) {
                Log.w(TAG, "onRemoteUserRinging: ");
                status.setText("对方正在响铃");
            }

            /**
             * 被叫端加入通话。
             * 主叫端拨出电话，被叫端收到请求后，加入通话，回调 onRemoteUserJoined。
             *
             * @param userId      加入用户的 id。
             * @param mediaType   加入用户的媒体类型，audio or video。
             * @param remoteVideo 加入用户者的 camera 信息。
             */
            @Override
            public void onRemoteUserJoined(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView remoteVideo) {
                Log.w(TAG, "onRemoteUserJoined: ");
            }

            /**
             * 通话中的某一个参与者，邀请好友加入通话，发出邀请请求后，回调 onRemoteUserInvited。
             *
             * @param userId    被邀请者的 id。
             * @param mediaType 被邀请者的 id。
             */
            @Override
            public void onRemoteUserInvited(String userId, RongCallCommon.CallMediaType mediaType) {

            }

            /**
             * 通话中的远端参与者离开。
             * 回调 onRemoteUserLeft 通知状态更新。
             *
             * @param userId 远端参与者的 id。
             * @param reason 远端参与者离开原因。
             */
            @Override
            public void onRemoteUserLeft(String userId, RongCallCommon.CallDisconnectedReason reason) {
                Log.w(TAG, "onRemoteUserLeft: ");
            }

            /**
             * 当通话中的某一个参与者切换通话类型，例如由 audio 切换至 video，回调 onMediaTypeChanged。
             *
             * @param userId    切换者的 userId。
             * @param mediaType 切换者，切换后的媒体类型。
             * @param video     切换着，切换后的 camera 信息，如果由 video 切换至 audio，则为 null。
             */
            @Override
            public void onMediaTypeChanged(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView video) {

            }

            /**
             * 通话过程中，发生异常。
             *
             * @param errorCode 异常原因。
             */
            @Override
            public void onError(RongCallCommon.CallErrorCode errorCode) {

            }

            /**
             * 远端参与者 camera 状态发生变化时，回调 onRemoteCameraDisabled 通知状态变化。
             *
             * @param userId   远端参与者 id。
             * @param disabled 远端参与者 camera 是否可用。
             */
            @Override
            public void onRemoteCameraDisabled(String userId, boolean disabled) {

            }
        });

    }

    private void onCallConnected() {
        if (subscribe != null)
            subscribe.unsubscribe();
        subscribe = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        calculateCallTime(aLong);
                    }
                });
        accept.setVisibility(View.INVISIBLE);
    }

    private void calculateCallTime(Long between) {
        long iHour = between / 3600;
        long iMin = (between - iHour * 3600) / 60;
        long iSen = between - iHour * 3600 - iMin * 60;
        status.setText(getResources().getString(R.string.call_time, String.valueOf(iHour), String.valueOf(iMin), String.valueOf(iSen)));
    }

    public void hangup(View view) {
        RongCallClient.getInstance().hangUpCall(callId);
        finish();
    }

    public void accept(View view) {
        RongCallClient.getInstance().acceptCall(callId);
    }
}
