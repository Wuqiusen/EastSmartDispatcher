package com.zxw.dispatch.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zxw.data.bean.DepartCar;
import com.zxw.data.source.DepartSource;
import com.zxw.data.utils.LogUtil;

import java.text.SimpleDateFormat;
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
 * Created by huson on 2017/2/26.
 * 940762301@qq.com
 */

public class SendCarUtils {
    private int lineID;
    private List<DepartCar> departCars;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private DepartSource mSource = new DepartSource();
    private String code = SpUtils.getCache(mContext, SpUtils.USER_ID);
    private String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);
    private SendCarResult sendCarResult;//车辆发车结果回调
    private Map<Integer, Boolean> sendResult = new HashMap<>();//是否发车成功
    private Map<Integer, Boolean> isSend = new HashMap<>();//是否已发车

    public SendCarUtils(int lineId, List<DepartCar> waitVehicleList) {
        this.lineID = lineId;
        this.departCars = waitVehicleList;
        createSendResult();
        startTimer();
    }

    private void createSendResult() {
        for (DepartCar departCar : departCars) {
            isSend.put(departCar.getId(), false);
            sendResult.put(departCar.getId(), false);
        }
    }

    private void startTimer() {
//        //定时器
        Log.w("createTimer----------", "创建定时器");
        createTimeTool();
        mTimer.schedule(mTimerTask, 0, 1000 * 30);

    }

    private void stopTimer() {
        Log.w("stopTimer---------", "停止定时器");
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void createTimeTool() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.w("createTimer---------", "定时器运行");
                    if (departCars != null && !departCars.isEmpty()) {
                        if (!isSend.get(departCars.get(0).getId()) && !sendResult.get(departCars.get(0).getId())) {
                            if (checkTime(departCars.get(0))) {
                                isSend.put(departCars.get(0).getId(), true);
                                Log.w("createTimer--------", "checkTime");
                                sendCar();
                            }
                        }
                    } else {
                        Log.w("createTimer-----------", "定时器停止");
                        if (sendCarResult != null) {
                            sendCarResult.onSendCarFail(lineID);
                        }
                    }

                }
            };
        }

    }


    private void sendCar() {
        mSource.sendCar(new Subscriber() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.w("sendCar---------", departCars.get(0).getCode() + "发车失败");
                if (sendCarResult != null) {
                    sendCarResult.onSendCarFail(lineID);
                }
                LogUtil.loadRemoteError("sendCar " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                DebugLog.e("sendCar" + lineID);
                Log.w("sendCar--------", departCars.get(0).getCode() + "已经发出");
                if (sendCarResult != null) {
                    sendCarResult.onSendCarSuccess(lineID);
                }
                sendResult.put(departCars.get(0).getId(), true);
                if (isSend.get(departCars.get(0).getId()) && sendResult.get(departCars.get(0).getId())) {
                    stopTimer();//发车成功后，取消定时器，发送更新待发车列表广播，马上检测第二辆车是否到达发车时间
                    departCars.remove(0);
                    startTimer();
                }


            }
        }, code, keyCode, departCars.get(0).getId());
    }

    private boolean checkTime(final DepartCar w) {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        Log.w("checkTime----------", w.getCode() + "等待发车");
        if (w.getVehTime() != null && !TextUtils.isEmpty(w.getVehTime())) {
            if (Integer.valueOf(w.getVehTime()) <= Integer.valueOf(str)) {
                Log.w("checkTime---------", w.getCode() + "发车时间到");
                return true;
            }
        }
        return false;
    }

    public void setTimerCancel() {
        stopTimer();
    }

    public interface SendCarResult {
        void onSendCarSuccess(int lineId);

        void onSendCarFail(int lineId);
    }

    public void setOnSendCarResult(SendCarResult sendCarResult) {
        this.sendCarResult = sendCarResult;
    }

}
