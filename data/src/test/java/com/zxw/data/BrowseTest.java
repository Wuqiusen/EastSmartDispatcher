package com.zxw.data;

import com.zxw.data.bean.FuzzyVehicleBean;
import com.zxw.data.http.HttpGPsRequest;
import com.zxw.data.http.HttpMethods;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * author：CangJie on 2016/10/12 16:20
 * email：cangjie2016@gmail.com
 */
public class BrowseTest {
    private String keyCode = "ba8d73699f1fbf1271da1fc4259fe5b0";
    private String code = "000001";
    private int lineId = 210;
    private int stationId = 821;
    private String result;

    @Before
    public void setup(){
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }

            @Override
            public Scheduler getNewThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test
    public void testGps(){  // 获取经纬度
        HttpGPsRequest.getInstance().gpsByVehCode(new Subscriber<HttpGPsRequest.GpsBaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                result = e.getMessage();
            }

            @Override
            public void onNext(HttpGPsRequest.GpsBaseBean gpsBaseBean) {

                result = gpsBaseBean.toString();
            }
        },"粤B04317D");
        Assert.assertEquals("1", result);
    }


    @Test
    public void testSortVehicle(){
        int opId = 13;
        int replaceId = 2;
        HttpMethods.getInstance().sortVehicle(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }

        }, code, keyCode, opId, replaceId);
    }
    @Test
    public void testVehcile(){
        String vehCode = "n10";
        HttpMethods.getInstance().queryVehcile(new Subscriber<List<FuzzyVehicleBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();
            }

            @Override
            public void onNext(List<FuzzyVehicleBean> vehicles) {
                result = vehicles.toString();
            }
        }, code, keyCode, vehCode, lineId + "");
        Assert.assertEquals("1", result);
    }
}
