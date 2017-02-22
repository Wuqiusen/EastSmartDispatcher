package com.zxw.dispatch.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.WaitVehicle;
import com.zxw.data.source.DepartSource;
import com.zxw.dispatch.Constants;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;

import static com.zxw.dispatch.MyApplication.mContext;

/**
 * 当前类注析：
 * Created by huson on 2017/2/9.
 * 940762301@qq.com
 */

public class CarPlanService extends Service {
    private int lineId;
    private int stationId;
    private List<Integer> lineIds = new ArrayList<>();
    private List<Integer> stationIds = new ArrayList<>();
    private Map<Integer, Timer> timerMap = new HashMap<>();
    private Map<Map<Integer, Integer>, WaitVehicle> dataMap = new HashMap<>();
    private CarPlanReceiver carPlanReceiver;

    DepartSource mSource = new DepartSource();
    String code = SpUtils.getCache(mContext, SpUtils.USER_ID);
    String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);

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
            stationId = intent.getIntExtra("stationId", 0);
            lineIds.add(lineId);
            stationIds.add(stationId);

            loadCarDataTimer(lineId, stationId);
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void sendCar(final int lineId, final int stationId, int opId) {
        mSource.sendVehicle(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                loadCarDataTimer(lineId, stationId);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseBean baseBean) {
                DebugLog.e("sendCar" + lineId);
                //发送广播更新列表数据
                Intent intent = new Intent("com.zxw.dispatch.MSG_RECEIVER");
                intent.putExtra("lineId", lineId);
                intent.putExtra("stationId", stationId);
                sendBroadcast(intent);
            }
        }, code, keyCode, opId, Constants.AUTO_TYPE);

    }

    public void loadCarDataTimer(final int lineId, final int stationId) {
        final Map<Integer, Integer> map = new HashMap<>();
        map.put(lineId, stationId);
        mSource.loadWaitVehicle(new Subscriber<List<WaitVehicle>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                loadCarDataTimer(lineId, stationId);
            }

            @Override
            public void onNext(final List<WaitVehicle> waitVehicles) {
                DebugLog.e("loadCarDataTimer" + lineId);
                if (waitVehicles != null && !waitVehicles.isEmpty()) {
                    dataMap.put(map, waitVehicles.get(0));
                    //定时器
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            checkTime(lineId, stationId, waitVehicles.get(0));
                        }
                    }, 0, 1000 * 30);
                    timerMap.put(lineId, t);
                } else {
                    if (timerMap.get(lineId) != null) {
                        DebugLog.e("loadCarDataTimer cancel" +lineId);
                        timerMap.get(lineId).cancel();
                    }
                }


            }
        }, code, lineId, stationId, keyCode, 1, 1);
    }

    public void checkTime(final int lineId, final int stationId, final WaitVehicle w) {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        if (checkLine(lineId)) {
            DebugLog.e("lineId:" + lineId);
            if (w.projectTime != null && !TextUtils.isEmpty(w.projectTime)) {
                if (Integer.valueOf(w.projectTime) <= Integer.valueOf(str)) {
                    timerMap.get(lineId).cancel();
                    sendCar(lineId, stationId, w.id);
                }
            }
        } else {
            DebugLog.w("del:" + lineId);
            timerMap.get(lineId).cancel();//如果没有该线路id则取消定时器
            timerMap.remove(lineId);
        }
    }

    private boolean checkLine(int lineId) {
        for (Integer id : lineIds) {
            if (id == lineId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 广播接收器
     */
    public class CarPlanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getStringExtra("type"), "getData")){
                //获取自动发车列表，发送广播给Main判断该线路是否设置自动发车
                boolean isAuto = false;
                if (lineIds != null && !lineIds.isEmpty()) {
                    for (Integer lineId: lineIds){
                        if (lineId == intent.getIntExtra("lineKey", 0)) {
                            isAuto = true;
                        }
                    }
                }
                Intent megIntent = new Intent("com.zxw.dispatch.MSG_RECEIVER");
                megIntent.putExtra("isAuto", isAuto);
                megIntent.putExtra("type", "getData");
                sendBroadcast(megIntent);

            }else {
                //取消自动发车
                lineId = intent.getIntExtra("lineId", 0);
                stationId = intent.getIntExtra("stationId", 0);
                if (lineIds != null && !lineIds.isEmpty()) {
                    for (int i = 0; i < lineIds.size(); i++) {
                        if (lineIds.get(i) == lineId) {
                            lineIds.remove(i);
                            stationIds.remove(i);
                        }
                    }
                }
                if (timerMap.get(lineId) != null) {
                    DebugLog.e("CarPlanReceiver cancel" + lineId);
                    timerMap.get(lineId).cancel();
                    timerMap.remove(lineId);
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


