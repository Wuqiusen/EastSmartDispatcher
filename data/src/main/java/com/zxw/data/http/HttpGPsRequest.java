package com.zxw.data.http;

import com.zxw.data.bean.BackHistory;
import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.ChangePwdBean;
import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.DriverWorkloadItem;
import com.zxw.data.bean.FuzzyVehicleBean;
import com.zxw.data.bean.InformContentBean;
import com.zxw.data.bean.InformDataBean;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.LoginBean;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.MoreHistory;
import com.zxw.data.bean.NonMissionType;
import com.zxw.data.bean.Person;
import com.zxw.data.bean.PersonInfo;
import com.zxw.data.bean.SchedulePlanBean;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.SmsCodeBean;
import com.zxw.data.bean.SpotBean;
import com.zxw.data.bean.StopCarCodeBean;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.bean.VehicleNumberBean;
import com.zxw.data.bean.VersionBean;
import com.zxw.data.bean.WaitVehicle;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
        public boolean success;
        public String message;
        public String errCode;
        public Gps gps;

        public class Gps{
            public float latitude;
            public float longitude;
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
