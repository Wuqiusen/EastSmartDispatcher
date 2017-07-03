package com.zxw.dispatch.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.VersionBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.source.WelcomeSource;
import com.zxw.data.utils.LogUtil;
import com.zxw.data.utils.MD5;
import com.zxw.dispatch.presenter.view.WelcomeView;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * 作者：${MXQ} on 2017/2/8 11:29
 * 邮箱：1299242483@qq.com
 */
public class WelcomePresenter extends BasePresenter<WelcomeView> {
    private Context mContext;

    private final WelcomeSource mSource;
    public WelcomePresenter(WelcomeView mvpView,Context context) {
        super(mvpView);
        this.mSource = new WelcomeSource();
        this.mContext = context;
    }

    public void checkVersion(){
        String time = String.valueOf(new Date().getTime());
        mSource.checkVersion(new Subscriber<VersionBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                   mvpView.disPlay(e.getMessage());
                   mvpView.loadMain();
                LogUtil.loadRemoteError("checkVersion " + e.getMessage());
            }

            @Override
            public void onNext(VersionBean versionBean) {
                   mvpView.getVersionDataSuccess(versionBean);
            }
        },time);
    }

    public void upLoadLog(){
        final List<String> errorLog = SpUtils.getErrorLog(mContext);
        if (errorLog == null || errorLog.get(0) == null || TextUtils.isEmpty(errorLog.get(0))) {
            DebugLog.i("errorLog == null");
            return;
        } else {
            DebugLog.i("errorLog != null");
        }
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        HttpMethods.getInstance().upLoadLog(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("upLoadLog " + e.getMessage());
            }

            @Override
            public void onNext(BaseBean baseBean) {
                ToastHelper.showToast("上传成功");

            }
        }, errorLog.get(0), errorLog.get(1), MD5.MD5Encode(df.format(new Date())));
    }

}
