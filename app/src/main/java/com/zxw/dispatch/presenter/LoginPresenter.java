package com.zxw.dispatch.presenter;

import com.zxw.data.bean.LoginBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.MD5;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.presenter.view.LoginView;
import com.zxw.dispatch.utils.SpUtils;

import java.util.Date;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/20 16:52
 * email：cangjie2016@gmail.com
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    public LoginPresenter(LoginView mvpView) {
        super(mvpView);
    }

    public void verifyAccount(String username, String password){
        String time = String.valueOf(new Date().getTime());
        String md5Password = MD5.MD5Encode(MD5.MD5Encode(password) + time);
        HttpMethods.getInstance().login(new Subscriber<LoginBean>() {
            @Override
            public void onCompleted() {
                mvpView.loginSuccess();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.loginFail(e.getMessage());
            }

            @Override
            public void onNext(LoginBean loginBean) {
                SpUtils.setCache(MyApplication.mContext, SpUtils.CODE, loginBean.code);
                SpUtils.setCache(MyApplication.mContext, SpUtils.NAME, loginBean.name);
                SpUtils.setCache(MyApplication.mContext, SpUtils.KEYCODE, loginBean.keyCode);
            }
        }, username, md5Password, time);
    }
}
