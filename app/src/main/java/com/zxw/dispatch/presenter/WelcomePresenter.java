package com.zxw.dispatch.presenter;

import android.content.Context;
import android.os.Environment;

import com.zxw.data.bean.VersionBean;
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

    public void checkVersion() {
        final long startTime = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        final String keyCode = MD5.MD5Encode(df.format(new Date()));
        mSource.loadUpdateAppVersion(new Subscriber<VersionBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                // 获取版本失败，则重新获取
                mvpView.respondVersionFailed(e.getMessage(),startTime,keyCode);
                if (e.getMessage().contains("hostname"))
                    return;
                //mvpView.disPlay(e.getMessage());
                //保存到本地
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String filePath = Environment.getExternalStorageDirectory()+"/";
                String fileName = "log.txt";
                String log = "工号:" + SpUtils.getCache(MyApplication.mContext, SpUtils.CODE) +"\n"
                        + "result : " + e.getMessage() +"\n"
                        + format.format(new Date());
                MyApplication.writeTxtToFile(log, filePath, fileName);
            }

            @Override
            public void onNext(VersionBean versionBean) {
                 mvpView.respondVersionSuccess(startTime,versionBean);
            }
        },keyCode);

    }
}
