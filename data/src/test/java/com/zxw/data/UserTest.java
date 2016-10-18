package com.zxw.data;

import com.zxw.data.bean.LoginBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.MD5;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import rx.Scheduler;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * author：CangJie on 2016/10/12 15:26
 * email：cangjie2016@gmail.com
 */
public class UserTest {

    private String keyCode;

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
    public void testLogin(){
        String code = "000001";
        String password = "123456";
        String time = String.valueOf(new Date().getTime());
        String md5Password = MD5.MD5Encode(MD5.MD5Encode(password) + time);
        System.out.println(time);
        System.out.println(md5Password);
        HttpMethods.getInstance().login(new Subscriber<LoginBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LoginBean loginBean) {
                keyCode = loginBean.keyCode;
            }
        }, code, md5Password, time);
        Assert.assertEquals("1", keyCode);
    }
}
