package com.zxw.data.source;

import com.zxw.data.bean.Vehicle;
import com.zxw.data.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/10/17 09:30
 * email：cangjie2016@gmail.com
 */
public class QuerySource {

    public void queryVehcile(Subscriber<List<Vehicle>> subscriber, String code,
                             String keyCode, String vehCode, int pageNo, int pageSize){
        HttpMethods.getInstance().queryVehcile(subscriber, code, keyCode, vehCode, pageNo, pageSize);
    }
}
