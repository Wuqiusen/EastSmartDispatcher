package com.zxw.data.http;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author：CangJie on 2017-6-7 10:30:29
 * email：cangjie2016@gmail.com
 */
public class HttpGPsRequest {
    public static final String BASE_URL = "http://39.108.76.81:8088/getGps/";
    public Retrofit retrofit = RetrofitSetting.getInstance();

    private static class SingletonHolder{
        private static final HttpGPsRequest INSTANCE = new HttpGPsRequest();
    }

    public static HttpGPsRequest getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    public void gpsByVehCode(Subscriber<HttpGPsRequest.GpsBaseBean> subscriber,String vehCode) {
        HttpInterfaces.GPS gps = retrofit.create(HttpInterfaces.GPS.class);
        Observable<HttpGPsRequest.GpsBaseBean> observable = gps.gps(BASE_URL + vehCode);
        toSubscribe(observable, subscriber);
    }

    public class GpsBaseBean{
        public boolean success; // 成功，或 失败
        public String message;
        public String errCode;
        public Gps gps;

        public class Gps{
            public double latitude;
            public double longitude;
            public Integer dateInt;
            public Integer timeInt;
        }

        @Override
        public String toString() {
            return "GpsBaseBean{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    ", errCode='" + errCode + '\'' +
                    ", gps=" + gps +
                    '}';
        }
    }

}
