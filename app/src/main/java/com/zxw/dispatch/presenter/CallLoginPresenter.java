package com.zxw.dispatch.presenter;

import android.content.Context;

import com.zxw.data.bean.CommunicateLoginBean;
import com.zxw.data.bean.LoginBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.LogUtil;
import com.zxw.data.utils.MD5;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.commucation.ConnectUtil;
import com.zxw.dispatch.presenter.view.CallLoginView;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;

import java.util.Date;

import rx.Subscriber;

import static com.zxw.dispatch.MyApplication.mContext;
import static io.rong.imlib.statistics.UserData.username;

public class CallLoginPresenter extends BasePresenter<CallLoginView> {
    public CallLoginPresenter(CallLoginView mvpView, Context context) {
        super(mvpView);
    }

    public void verifyAccount(String userName, String password) {
        String time = String.valueOf(new Date().getTime());
        String md5Password = MD5.MD5Encode(MD5.MD5Encode(password) + time);
        HttpMethods.getInstance().callLogin(new Subscriber<CommunicateLoginBean>() {
            @Override
            public void onCompleted() {
                mvpView.loginSuccess();
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("verifyAccount " + e.getMessage());
                mvpView.loginFail(e.getMessage());
            }

            @Override
            public void onNext(CommunicateLoginBean loginBean) {
                ToastHelper.showToast("登陆成功");
                SpUtils.setCache(mContext, SpUtils.CALL_USER_ID, loginBean.userId);
                SpUtils.setCache(mContext, SpUtils.CALL_KEYCODE, loginBean.keyCode);
                // 拿token连接融云服务器
                ConnectUtil.connect(MyApplication.mContext.getApplicationInfo(), mContext, loginBean.token);
            }
        }, userName, md5Password, time);
    }
}
