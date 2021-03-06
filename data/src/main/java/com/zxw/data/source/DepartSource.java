package com.zxw.data.source;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.WaitVehicle;
import com.zxw.data.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/20 17:37
 * email：cangjie2016@gmail.com
 */
public class DepartSource {

    // 线路
    private static final Integer TYPE_LINE = 1;
    // 营运任务
    private static final Integer TYPE_OPERATION = 2;
    // 非营运任务
    private static final Integer TYPE_OTHER = 3;

    public void sortVehicle(Subscriber subscriber, String code, String keyCode, int opId, int replaceId){
        HttpMethods.getInstance().sortVehicle(subscriber, code, keyCode, opId, replaceId);
    }


    public void sendCar(Subscriber subscriber, String code, String keyCode, int opId){
        HttpMethods.getInstance().sendCar(subscriber, code, keyCode, opId);
    }


    public void departListByLine(Subscriber<List<DepartCar>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().getScheduleList(subscriber, userId, keyCode, lineId, TYPE_LINE);
    }

    public void departListByOperation(Subscriber<List<DepartCar>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().getScheduleList(subscriber, userId, keyCode, lineId, TYPE_OPERATION);
    }

    public void departListByOther(Subscriber<List<DepartCar>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().getScheduleList(subscriber, userId, keyCode, lineId, TYPE_OTHER);
    }

    public void goneListByLine(Subscriber<BaseBean<List<SendHistory>>> subscriber, String userId, String keyCode, int lineId, int pageNo, int pageSize){
        HttpMethods.getInstance().getGoneHistoryList(subscriber, userId, keyCode, lineId, TYPE_LINE, pageNo, pageSize);
    }

    public void goneListByOperation(Subscriber<List<SendHistory>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().getScheduleHistoryList(subscriber, userId, keyCode, lineId, TYPE_OPERATION);
    }

    public void goneListByOther(Subscriber<List<SendHistory>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().getScheduleHistoryList(subscriber, userId, keyCode, lineId, TYPE_OTHER);
    }



    public void vehicleStopCtrl(Subscriber subscriber, String userId, String keyCode,
                                String vehicleId, String driverId, int saleType, String stewardId, String taskEditRunId){
        HttpMethods.getInstance().vehicleStopCtrl(subscriber, userId, keyCode, vehicleId, driverId, saleType, stewardId, taskEditRunId);
    }

    public void stopToSchedule(Subscriber subscriber, String userId, String keyCode, String objId, int type, String taskId, String taskType,
                               String beginTime, String endTime,  String runNum, String runEmpMileage,  int workScheduleType,String remarks){
        HttpMethods.getInstance().stopToSchedule(subscriber, userId, keyCode, objId, type, taskId, taskType, beginTime, endTime, runNum, runEmpMileage, workScheduleType,remarks);
    }
    public void changePersonInfo(Subscriber subscriber, String userId, String keyCode,
                                 int objId, int personId, int type){
        HttpMethods.getInstance().changePersonInfo(subscriber, userId, keyCode, objId, personId, type);
    }
}
