package com.zxw.dispatch.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.zxw.data.bean.DepartCar;
import com.zxw.data.source.DepartSource;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SendCarUtils;
import com.zxw.dispatch.utils.SpUtils;

import java.util.ArrayList;
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
    private List<Integer> lineIds = new ArrayList<>();
    private CarPlanReceiver carPlanReceiver;
    private Map<Integer, SendCarUtils> autoData = new HashMap<>();
    private Map<Integer, Integer> failData = new HashMap<>();
    private Map<Integer, Timer> failTimer = new HashMap<>();

    private DepartSource mSource = new DepartSource();
    private String code = SpUtils.getCache(mContext, SpUtils.USER_ID);
    private String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);
    private Intent megIntent = new Intent("com.zxw.dispatch.MSG_RECEIVER");
    private Intent updateIntent = new Intent("com.zxw.dispatch.MSG_RECEIVER");
    private Intent rIntent;

    private final static int CANCEL_AUTO = 11;
    private final static int IS_AUTO = 12;

    Handler handler =  new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case CANCEL_AUTO:
                        Log.w("service---", "关闭自动发车");
                        if (rIntent != null)
                        removeAutoData(rIntent.getIntExtra("lineId", 0));
                        break;
                    case IS_AUTO:
                        Log.w("service---", "判断是否自动发车");
                        if (rIntent != null)
                        checkIsAuto(rIntent.getIntExtra("lineKey", 0));
                        break;
                    default:
                        rIntent = null;
                        break;
                }

            } catch (Exception e) {
            }
        }
    };

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
            final int lineId = intent.getIntExtra("lineId", 0);
            lineIds.add(lineId);
            failData.put(lineId, 0);
            failTimer.put(lineId, new Timer());
            loadCarDataTimer(lineId);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 获取待发车数据
     *
     * @param lineId
     */
    public void loadCarDataTimer(final int lineId) {
        mSource.departList(new Subscriber<List<DepartCar>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                failSleep(lineId);
            }

            @Override
            public void onNext(final List<DepartCar> waitVehicles) {
                DebugLog.e("loadCarDataTimer" + lineId);
                //如果获取数据为空则停止该线路自动发车功能
                if (waitVehicles == null || waitVehicles.isEmpty()) {
                    if (autoData.get(lineId) != null) {
                        autoData.remove(lineId);
                        removeAutoData(lineId);
                        //通知main设置为手动发车
                    }
                    checkIsAuto(lineId);
                } else {
                    createSendCarUtil(lineId, waitVehicles);
                    updateFailCount(lineId);
                }

            }
        }, code, keyCode, lineId);
    }

    /**
     * 失败两次以上进行休眠
     *
     * @param lineId
     */
    private void failSleep(final int lineId) {
        Log.e("failSleep---", "加载失败");
        failData.put(lineId, failData.get(lineId) + 1);
        Log.e("failSleep---", "失败次数：" + failData.get(lineId));
        if (failData.get(lineId) == 2) {
            if (failTimer.get(lineId) == null) {
                failTimer.put(lineId, new Timer());
            }
            failTimer.get(lineId).schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.e("failSleep---", "重新加载");
                    loadCarDataTimer(lineId);
                }
            }, 0, 1000 * 30);
        } else if (failData.get(lineId) < 2) {
            loadCarDataTimer(lineId);
        }
    }

    //更新失败次数
    private void updateFailCount(int lineId) {
        failData.put(lineId, 0);
        if (failTimer.get(lineId) != null)
            failTimer.get(lineId).cancel();
    }

    /**
     * 创建自动发车工具
     *
     * @param lineId
     * @param waitVehicles
     */
    private void createSendCarUtil(int lineId, List<DepartCar> waitVehicles) {
        SendCarUtils sendCarUtils = new SendCarUtils(lineId, waitVehicles);
        sendCarUtils.setOnSendCarResult(new SendCarUtils.SendCarResult() {
            @Override
            public void onSendCarSuccess(int lineId) {
                //发车成功，发送广播更新列表数据
                updateIntent.putExtra("lineId", lineId);
                sendBroadcast(updateIntent);
                checkIsAuto(lineId);
            }

            @Override
            public void onSendCarFail(int lineId) {
                //发送失败，重新获取发车列表
                removeAutoData(lineId);
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
            Log.w("onReceive---", "广播已接收");
            Message obtain = Message.obtain();
            rIntent = intent;
            if (intent.getStringExtra("type") != null && TextUtils.equals(intent.getStringExtra("type"), "getData")) {
                obtain.what = IS_AUTO;
                handler.sendMessage(obtain);


            } else {
                //取消自动发车
                obtain.what = CANCEL_AUTO;
                handler.sendMessage(obtain);

            }
        }
    }

    /**
     * 判断是否设置了自动发车
     */
    private void checkIsAuto(int lineKey) {
        //获取自动发车列表，发送广播给Main判断该线路是否设置自动发车
        boolean isAuto = false;
        if (lineIds != null && !lineIds.isEmpty()) {
            for (Integer lineId : lineIds) {
                if (lineId == lineKey) {
                    isAuto = true;
                }
            }
        }

        //发送广播更新发车模式
        megIntent.putExtra("isAuto", isAuto);
        megIntent.putExtra("type", "getData");
        sendBroadcast(megIntent);
    }

    /**
     * 移除定时器
     *
     * @param lineId
     */
    private void removeAutoData(int lineId) {
        if (lineIds != null && !lineIds.isEmpty()) {
            for (int i = 0; i < lineIds.size(); i++) {
                if (lineIds.get(i) == lineId) {
                    Log.e("removeAutoData--", "移除定时器:" + lineId);
                    lineIds.remove(i);
                    autoData.get(lineId).setTimerCancel();
                    autoData.remove(lineId);//移除自动发车
                    failTimer.remove(lineId);
                }
            }
        }
        closeService();
    }

    /**
     * 关闭service
     */
    private void closeService() {
        if (lineIds == null || lineIds.isEmpty()) {
            Log.e("closeService---", "service数据为空");
            this.onDestroy();
        }
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(carPlanReceiver);
        carPlanReceiver = null;
        clearData();
        super.onDestroy();
        Log.e("onDestroy---", "service销毁");
        rIntent = null;
    }

    private void clearData() {
        if (autoData != null && !autoData.isEmpty())
            for (int key : autoData.keySet()) {
                autoData.get(key).setTimerCancel();
                autoData.remove(key);
            }

        if (failTimer != null && !failTimer.isEmpty())
            for (int key : failTimer.keySet()) {
                failTimer.get(key).cancel();
                failTimer.remove(key);
            }

        failData.clear();
        lineIds.clear();
    }
}


