package com.zxw.data.source;

import com.zxw.data.bean.Person;
import com.zxw.data.bean.Vehcile;
import com.zxw.data.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/10/17 09:30
 * email：cangjie2016@gmail.com
 */
public class QuerySource {
    public void queryPerson(Subscriber<List<Person>> subscriber, String code,
                            String keyCode, String perName, int type, int pageNo, int pageSize){
        HttpMethods.getInstance().queryPerson(subscriber, code, keyCode, perName, type, pageNo, pageSize);
    }

    public void queryVehcile(Subscriber<List<Vehcile>> subscriber, String code,
                             String keyCode, String vehCode, int pageNo, int pageSize){
        HttpMethods.getInstance().queryVehcile(subscriber, code, keyCode, vehCode, pageNo, pageSize);
    }
}
