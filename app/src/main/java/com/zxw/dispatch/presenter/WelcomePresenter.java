package com.zxw.dispatch.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.zxw.data.bean.VersionBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.source.WelcomeSource;
import com.zxw.data.utils.MD5;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.presenter.view.WelcomeView;
import com.zxw.dispatch.utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.Subscriber;

/**
 * 作者：${MXQ} on 2017/2/8 11:29
 * 邮箱：1299242483@qq.com
 */
public class WelcomePresenter extends BasePresenter<WelcomeView> {

    private final WelcomeSource mSource;
    public WelcomePresenter(WelcomeView mvpView,Context context) {
        super(mvpView);
        this.mSource = new WelcomeSource();
    }

    public void checkVersion(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);//设置日期格式
        final String keycode2 = MD5.MD5Encode(df.format(new Date()));
        mSource.checkVersion(new Subscriber<VersionBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                   mvpView.disPlay(e.getMessage());
                   mvpView.loadMain();
            }

            @Override
            public void onNext(VersionBean versionBean) {
                   mvpView.getVersionDataSuccess(versionBean);
            }
        },keycode2);
    }

}
