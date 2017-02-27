package com.zxw.dispatch.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zxw.data.bean.DepartCar;
import com.zxw.data.source.DepartSource;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SendCarUtils;
import com.zxw.dispatch.utils.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

import static com.zxw.dispatch.MyApplication.mContext;

/**
 * 当前类注析：
 * Created by huson on 2017/2/9.
 * 940762301@qq.com
 */

public class CarPlanService extends Service {
    private int lineId;
    private List<Integer> lineIds = new ArrayList<>();
    private CarPlanReceiver carPlanReceiver;
    private Map<Integer,SendCarUtils> autoData = new HashMap<>();

    private DepartSource mSource = new DepartSource();
    private String code = SpUtils.getCache(mContext, SpUtils.USER_ID);
    private String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DebugLog.e("CarPlanService:onStartCommand");
        //动态注册广播接收器
        if (carPlanReceiver == null) {
            carPlanReceiver = new CarPlanReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.zxw.dispatch.service.RECEIVER");
            registerReceiver(carPlanReceiver, intentFilter);
        }
        if (intent != null) {
            lineId = intent.getIntExtra("lineId", 0);
            lineIds.add(lineId);
            loadCarDataTimer(lineId);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 获取待发车数据
     * @param lineId
     */
    public void loadCarDataTimer(final int lineId) {
        mSource.departList(new Subscriber<List<DepartCar>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(final List<DepartCar> waitVehicles) {
                DebugLog.e("loadCarDataTimer" + lineId);
                //如果获取数据为空则停止该线路自动发车功能
                if (waitVehicles == null && waitVehicles.isEmpty()){
                    if (autoData.get(lineId) != null){
                        autoData.remove(lineId);
                        //通知main设置为手动发车
                        Intent megIntent = new Intent("com.zxw.dispatch.MSG_RECEIVER");
                        megIntent.putExtra("isAuto", false);
                        megIntent.putExtra("type", "getData");
                        sendBroadcast(megIntent);
                    }
                }else {
                    createSendCarUtil(lineId, waitVehicles);
                }

            }
        }, code,  keyCode,lineId);
    }

    /**
     * 创建自动发车工具
     * @param lineId
     * @param waitVehicles
     */
    private void createSendCarUtil(int lineId, List<DepartCar> waitVehicles){
        SendCarUtils sendCarUtils = new SendCarUtils(lineId, waitVehicles);
        sendCarUtils.setOnSendCarResult(new SendCarUtils.SendCarResult() {
            @Override
            public void onSendCarSuccess(int lineId) {
                //发车成功，发送广播更新列表数据
                Intent intent = new Intent("com.zxw.dispatch.MSG_RECEIVER");
                intent.putExtra("lineId", lineId);
                sendBroadcast(intent);
            }

            @Override
            public void onSendCarFail(int lineId) {
                //发送失败，重新获取发车列表
                autoData.remove(lineId);
                loadCarDataTimer(lineId);

            }
        });
        autoData.put(lineId, sendCarUtils);
    }





    /**
     * 广播接收器
     */
    public class CarPlanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getStringExtra("type"), "getData")) {
                //获取自动发车列表，发送广播给Main判断该线路是否设置自动发车
                boolean isAuto = false;
                if (lineIds != null && !lineIds.isEmpty()) {
                    for (Integer lineId : lineIds) {
                        if (lineId == intent.getIntExtra("lineKey", 0)) {
                            isAuto = true;
                        }
                    }
                }
                Intent megIntent = new Intent("com.zxw.dispatch.MSG_RECEIVER");
                megIntent.putExtra("isAuto", isAuto);
                megIntent.putExtra("type", "getData");
                sendBroadcast(megIntent);

            } else {
                //取消自动发车
                lineId = intent.getIntExtra("lineId", 0);
                if (lineIds != null && !lineIds.isEmpty()) {
                    for (int i = 0; i < lineIds.size(); i++) {
                        if (lineIds.get(i) == lineId) {
                            lineIds.remove(i);
                            autoData.remove(lineId);//移除自动发车
                        }
                    }
                }
            }

        }

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(carPlanReceiver);
        super.onDestroy();
    }
}


