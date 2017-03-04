package com.zxw.data.source;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.WaitVehicle;
import com.zxw.data.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/20 17:37
 * email：cangjie2016@gmail.com
 */
public class DepartSource {

    public void loadWaitVehicle(Subscriber<List<WaitVehicle>> subscriber, String code, int lineId, int stationId, String keyCode, int pageNo, int pageSize){
        HttpMethods.getInstance().waitVehicle(subscriber, code, lineId, stationId, keyCode, pageNo, pageSize);
    }

    public void sortVehicle(Subscriber subscriber, String code, String keyCode, int opId, int replaceId){
        HttpMethods.getInstance().sortVehicle(subscriber, code, keyCode, opId, replaceId);
    }

    public void addVehicle(Subscriber<BaseBean> subscriber, String code, String keyCode,
                           int lineId, int stationId, int vehId,
                           int sjId, String scId, String projectTime,
                           int spaceMin, String inTime2, String sortNum){
        HttpMethods.getInstance().addVehicle(subscriber, code, keyCode, lineId, stationId, vehId, sjId, scId, projectTime, spaceMin, inTime2, sortNum);
    }

    public void sendCar(Subscriber subscriber, String code, String keyCode, int opId){
        HttpMethods.getInstance().sendCar(subscriber, code, keyCode, opId);
    }

    public void updateVehicle(Subscriber subscriber, String code,
                              String keyCode, int opId, int vehId,
                              int sjId, String scId, String projectTime,
                              int spaceMin, String inTime2){
        HttpMethods.getInstance().updateVehicle(subscriber, code, keyCode, opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
    }

    public void departList(Subscriber<List<DepartCar>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().departList(subscriber, userId, keyCode, lineId);
    }
    public void vehicleStopCtrl(Subscriber subscriber, String userId, String keyCode,
                                String vehicleId, String driverId, int saleType, String stewardId, String taskEditRunId){
        HttpMethods.getInstance().vehicleStopCtrl(subscriber, userId, keyCode, vehicleId, driverId, saleType, stewardId, taskEditRunId);
    }

    public void vehicleToSchedule(Subscriber subscriber, String userId, String keyCode,
                                   String objId, int workScheduleType){
        HttpMethods.getInstance().vehicleToSchedule(subscriber, userId, keyCode, objId, workScheduleType);
    }
    public void changePersonInfo(Subscriber subscriber, String userId, String keyCode,
                                 int objId, int personId, int type){
        HttpMethods.getInstance().changePersonInfo(subscriber, userId, keyCode, objId, personId, type);
    }
}
