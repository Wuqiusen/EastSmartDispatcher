package com.zxw.data.source;

import com.zxw.data.bean.StopHistory;
import com.zxw.data.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 17:37
 * email：cangjie2016@gmail.com
 */
public class StopSource {

    public void loadStopByStay(Subscriber<List<StopHistory>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().getStopVehicleByStay(subscriber, userId, keyCode, lineId);
    }
    public void loadStopByEnd(Subscriber<List<StopHistory>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().getStopVehicleByEnd(subscriber, userId, keyCode, lineId);
    }


    public void stopCarEndToStay(Subscriber subscriber, String userId, String keyCode, int id) {
        HttpMethods.getInstance().stopCarEndToStay(subscriber, userId, keyCode, id);
    }
    public void stopCarStayToEnd(Subscriber subscriber, String userId, String keyCode, int id) {
        HttpMethods.getInstance().stopCarStayToEnd(subscriber, userId, keyCode, id);
    }
}
