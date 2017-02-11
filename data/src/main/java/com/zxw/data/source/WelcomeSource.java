package com.zxw.data.source;

import com.zxw.data.bean.VersionBean;
import com.zxw.data.http.HttpMethods;

import rx.Subscriber;

/**
 * 作者：${MXQ} on 2017/2/8 11:31
 * 邮箱：1299242483@qq.com
 */
public class WelcomeSource {

    public void checkVersion(Subscriber<VersionBean> subscriber, String keyCode){
        HttpMethods.getInstance().checkVersion(subscriber,keyCode);
    }
}
