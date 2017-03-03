package com.zxw.data;

import com.zxw.data.bean.BackHistory;
import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.MoreHistory;
import com.zxw.data.bean.Person;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.bean.Vehicle;
import com.zxw.data.bean.WaitVehicle;
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
    public void testLines(){
    }

    @Test
    public void testWaitVehicle(){
        HttpMethods.getInstance().waitVehicle(new Subscriber<List<WaitVehicle>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();

            }

            @Override
            public void onNext(List<WaitVehicle> waitVehicles) {
                System.out.print(waitVehicles.size()+"size");
                result = waitVehicles.toString();
            }
        }, code, lineId, stationId,keyCode, 1, 20);
        Assert.assertEquals("1", result);
    }
    @Test
    public void testSendHistory(){
        HttpMethods.getInstance().sendHistory(new Subscriber<List<SendHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();

            }

            @Override
            public void onNext(List<SendHistory> waitVehicles) {

                result = waitVehicles.toString();
            }
        }, code, lineId, stationId, keyCode, 1, 20);
        Assert.assertEquals("1", result);
    }

    @Test
    public void testBackHistory(){
        HttpMethods.getInstance().backHistory(new Subscriber<List<BackHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();

            }

            @Override
            public void onNext(List<BackHistory> waitVehicles) {

                result = waitVehicles.toString();
            }
        }, code, lineId, stationId, keyCode, 1, 20);
        Assert.assertEquals("1", result);
    }
    @Test
    public void testStopHistory(){
        HttpMethods.getInstance().stopHistory(new Subscriber<List<StopHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();

            }

            @Override
            public void onNext(List<StopHistory> waitVehicles) {

                result = waitVehicles.toString();
            }
        }, code, lineId, stationId,keyCode, 1, 20);
        Assert.assertEquals("1", result);
    }
    @Test
    public void testMoreHistory(){
        HttpMethods.getInstance().moreHistory(new Subscriber<List<MoreHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();

            }

            @Override
            public void onNext(List<MoreHistory> waitVehicles) {

                result = waitVehicles.toString();
            }
        }, code, lineId, stationId, keyCode, 1, 20);
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
    public void testAddVehicle(){
        int vehId = 7701;
        int sjId = 28;
        String scId = "30921";
        HttpMethods.getInstance().addVehicle(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                result = baseBean.toString();
            }
        }, code, keyCode, lineId, stationId, vehId, sjId, scId, "0800", 3, "0750", null);

        Assert.assertEquals("1", result);
    }

    @Test
    public void testSendVehicle(){
        HttpMethods.getInstance().sendVehicle(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                result = baseBean.toString();
            }
        }, code, keyCode, 13, 2);
        Assert.assertEquals("1", result);
    }
    @Test
    public void testUpdateVehicle(){

        int vehId = 7701;
        int sjId = 28;
        int opId = 3;
        String scId = "30921";
        HttpMethods.getInstance().updateVehicle(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                result = baseBean.toString();
            }
        }, code, keyCode, opId, vehId, sjId, scId, "0800", 6, "0750");

        Assert.assertEquals("1", result);
    }

    @Test
    public void testVehcile(){
        String vehCode = "n10";
        HttpMethods.getInstance().queryVehcile(new Subscriber<List<Vehicle>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();
            }

            @Override
            public void onNext(List<Vehicle> vehicles) {
                result = vehicles.toString();
            }
        }, code, keyCode, vehCode, 1, 20);
        Assert.assertEquals("1", result);
    }
    @Test
    public void testPerson(){
        String perName = "50584a2c4b88d536";
        int type = 1;
        HttpMethods.getInstance().queryPerson(new Subscriber<List<Person>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                result = e.getMessage();
            }

            @Override
            public void onNext(List<Person> persons) {
                result = persons.toString();
            }
        }, code, keyCode, perName, type, 1, 20);
        Assert.assertEquals("1", result);
    }
}
