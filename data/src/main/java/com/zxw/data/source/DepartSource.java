package com.zxw.data.source;

import com.zxw.data.bean.BaseBean;
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

    public void sortVehicle(Subscriber<BaseBean> subscriber, String code, String keyCode, int opId, int sortNum){
        HttpMethods.getInstance().sortVehicle(subscriber, code, keyCode, opId, sortNum);
    }

    public void addVehicle(Subscriber<BaseBean> subscriber, String code, String keyCode,
                           int lineId, int stationId, int vehId,
                           int sjId, String scId, String projectTime,
                           int spaceMin, String inTime2, String sortNum){
        HttpMethods.getInstance().addVehicle(subscriber, code, keyCode, lineId, stationId, vehId, sjId, scId, projectTime, spaceMin, inTime2, sortNum);
    }

    public void sendVehicle(Subscriber<BaseBean> subscriber, String code, String keyCode, int opId, int type){
        HttpMethods.getInstance().sendVehicle(subscriber, code, keyCode, opId, type);
    }

    public void updateVehicle(Subscriber<BaseBean> subscriber, String code,
                              String keyCode, int opId, int vehId,
                              int sjId, String scId, String projectTime,
                              int spaceMin, String inTime2){
        HttpMethods.getInstance().updateVehicle(subscriber, code, keyCode, opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
    }
}
