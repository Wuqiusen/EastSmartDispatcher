package com.zxw.dispatch.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.DepartCar;
import com.zxw.data.source.DepartSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private int sendIndex = 0;
    private Timer mTimer = new Timer();
    DepartSource mSource = new DepartSource();
    String code = SpUtils.getCache(mContext, SpUtils.USER_ID);
    String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);
    private SendCarResult sendCarResult;
    private Thread thread;
    private TimerTask timerTask;

    public SendCarUtils(int lineId, List<DepartCar> waitVehicleList) {
        this.lineID = lineId;
        this.departCars = waitVehicleList;
        createTimer();
    }

    private void createTimer() {
//        //定时器
        Log.w("createTimer---", "创建定时器");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (checkTime(departCars.get(0))) {
                    Log.w("createTimer---", "checkTime");
                    isSendSuccess();
                }

            }
        }, 0, 1000 * 30);
//        if (checkTime(departCars.get(0))){
//            if (isSendSuccess()){
//                departCars.remove(0);
//                createTimer();
//            }
//        }else {
//            if (thread != null){
//                thread.start();
//            }else {
//                sleepTime().start();
//            }
//
//        }

    }

    private Thread sleepTime() {
        return thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                createTimer();
            }
        });
    }


    private void isSendSuccess() {
        Log.w("isSendSuccess---", departCars.get(0).getCode() + "已经发车");
        mSource.sendCar(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.w("isSendSuccess---", departCars.get(0).getCode() + "发车失败");
                if (sendCarResult != null) {
                    sendCarResult.onSendCarFail(lineID);
                }

            }

            @Override
            public void onNext(BaseBean baseBean) {
                DebugLog.e("sendCar" + lineID);
                Log.w("isSendSuccess---", departCars.get(0).getCode() + "已经发出");
                if (sendCarResult != null) {
                    sendCarResult.onSendCarSuccess(lineID);
                }
                departCars.remove(0);
                mTimer.cancel();
                createTimer();
            }
        }, code, keyCode, departCars.get(sendIndex).getId());
    }

    private boolean checkTime(final DepartCar w) {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        Log.w("checkTime---", w.getCode() + "等待发车");
        if (w.getVehTime() != null && !TextUtils.isEmpty(w.getVehTime())) {
            if (Integer.valueOf(w.getVehTime()) <= Integer.valueOf(str)) {
                Log.w("checkTime---", w.getCode() + "发车时间到");
                return true;
            }
        }
        return false;
    }

    public interface SendCarResult {
        void onSendCarSuccess(int lineId);

        void onSendCarFail(int lineId);
    }

    public void setOnSendCarResult(SendCarResult sendCarResult) {
        this.sendCarResult = sendCarResult;
    }

}
