package com.zxw.data.http;

import com.zxw.data.bean.BackHistory;
import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LoginBean;
import com.zxw.data.bean.MoreHistory;
import com.zxw.data.bean.Person;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.bean.Vehcile;
import com.zxw.data.bean.WaitVehicle;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author：CangJie on 2016/10/12 15:23
 * email：cangjie2016@gmail.com
 */
public class HttpMethods {
    public static final String BASE_URL = "http://192.168.0.93:8080/yd_app/";
    public Retrofit retrofit = RetrofitSetting.getInstance();

    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<BaseBean<T>, T> {
        @Override
        public T call(BaseBean<T> httpResult) {
            if (httpResult.returnCode == 505) {
                throw new ApiException(httpResult.returnCode ,httpResult.returnInfo);
            }
            return httpResult.returnData;
        }
    }

    public void login(Subscriber<LoginBean> subscriber, String code, String password, String time){
        HttpInterfaces.User user = retrofit.create(HttpInterfaces.User.class);
        Observable<LoginBean> observable = user.login(code, password, time).map(new HttpResultFunc<LoginBean>());
        toSubscribe(observable, subscriber);
    }

    public void lines(Subscriber<List<Line>> subscriber, String code, String keyCode, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<Line>> map = browse.lines(code, keyCode, pageNo, pageSize).map(new HttpResultFunc<List<Line>>());
        toSubscribe(map, subscriber);
    }

    public void waitVehicle(Subscriber<List<WaitVehicle>> subscriber, String code, int lineId, int stationId, String keyCode, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<WaitVehicle>> map = browse.waitVehicle(code, keyCode, lineId, stationId, pageNo, pageSize).map(new HttpResultFunc<List<WaitVehicle>>());
        toSubscribe(map, subscriber);
    }

    // 已发车
    public void sendHistory(Subscriber<List<SendHistory>> subscriber, String code, int lineId, int stationId, String keyCode, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<SendHistory>> map = browse.sendHistory(code, keyCode, lineId, stationId, pageNo, pageSize).map(new HttpResultFunc<List<SendHistory>>());
        toSubscribe(map, subscriber);
    }

    // 已停班
    public void stopHistory(Subscriber<List<StopHistory>> subscriber, String code, int lineId, int stationId, String keyCode, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<StopHistory>> map = browse.stopHistory(code, keyCode, lineId, stationId, pageNo, pageSize).map(new HttpResultFunc<List<StopHistory>>());
        toSubscribe(map, subscriber);
    }

    // 已收班
    public void backHistory(Subscriber<List<BackHistory>> subscriber, String code, int lineId, int stationId, String keyCode, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<BackHistory>> map = browse.backHistory(code, keyCode, lineId, stationId, pageNo, pageSize).map(new HttpResultFunc<List<BackHistory>>());
        toSubscribe(map, subscriber);
    }

    // 更多
    public void moreHistory(Subscriber<List<MoreHistory>> subscriber, String code, int lineId, int stationId, String keyCode, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<MoreHistory>> map = browse.moreHistory(code, keyCode, lineId, stationId, pageNo, pageSize).map(new HttpResultFunc<List<MoreHistory>>());
        toSubscribe(map, subscriber);
    }

    // 排序
    public void sortVehicle(Subscriber<BaseBean> subscriber, String code, String keyCode, int opId, int sortNum){
        HttpInterfaces.Operater operater = retrofit.create(HttpInterfaces.Operater.class);
        Observable<BaseBean> observable = operater.sortVehicle(code, keyCode, opId, sortNum);
        toSubscribe(observable, subscriber);
    }

    // 添加车辆
    public void addVehicle(Subscriber<BaseBean> subscriber, String code, String keyCode,
                           int lineId, int stationId, int vehId,
                           int sjId, String scId, String projectTime,
                            int spaceMin, String inTime2, String sortNum){
        HttpInterfaces.Operater operater = retrofit.create(HttpInterfaces.Operater.class);
        Observable<BaseBean> observable = operater.addVehicle(code, keyCode, lineId, stationId, vehId, sjId, scId, projectTime, spaceMin, inTime2, sortNum);
        toSubscribe(observable, subscriber);
    }

    // 发车
    public void sendVehicle(Subscriber<BaseBean> subscriber, String code, String keyCode, int opId, int type){
        HttpInterfaces.Operater operater = retrofit.create(HttpInterfaces.Operater.class);
        Observable<BaseBean> observable = operater.sendVehicle(code, keyCode, opId, type);
        toSubscribe(observable, subscriber);
    }

    // 修改车辆信息
    public void updateVehicle(Subscriber<BaseBean> subscriber, String code,
                              String keyCode, int opId, int vehId,
                              int sjId, String scId, String projectTime,
                              int spaceMin, String inTime2){
        HttpInterfaces.Operater operater = retrofit.create(HttpInterfaces.Operater.class);
        Observable<BaseBean> observable = operater.updateVehicle(code, keyCode, opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
        toSubscribe(observable, subscriber);
    }

    //查询车号
    public void queryVehcile(Subscriber<List<Vehcile>> subscriber, String code,
                             String keyCode, String vehCode, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<Vehcile>> map = browse.queryVehcile(code, keyCode, vehCode, pageNo, pageSize).map(new HttpResultFunc<List<Vehcile>>());
        toSubscribe(map, subscriber);
    }

    //查询员工
    public void queryPerson(Subscriber<List<Person>> subscriber, String code,
                            String keyCode, String perName, int type, int pageNo, int pageSize){
        HttpInterfaces.Browse browse = retrofit.create(HttpInterfaces.Browse.class);
        Observable<List<Person>> map = browse.queryPerson(code, keyCode, perName, type, pageNo, pageSize).map(new HttpResultFunc<List<Person>>());
        toSubscribe(map, subscriber);
    }


}
