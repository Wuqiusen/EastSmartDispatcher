package com.zxw.dispatch;

import android.util.Log;

import org.junit.Test;

import rx.Observable;
import rx.Subscriber;

/**
 * author：CangJie on 2016/9/6 11:49
 * email：cangjie2016@gmail.com
 */
public class MainActivityTest{
    private static final String TAG = "MainActivityTest";
    @Test
    public void test_rxjava(){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World");
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.w(TAG, "onCompleted: " );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.w(TAG, "onNext: "+ s );
            }
        });
    }
}