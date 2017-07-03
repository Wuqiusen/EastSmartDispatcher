package com.zxw.dispatch.presenter;

import com.zxw.data.bean.LoginBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.LogUtil;
import com.zxw.data.utils.MD5;
import com.zxw.dispatch.presenter.view.LoginView;
import com.zxw.dispatch.utils.SpUtils;

import java.util.Date;

import rx.Subscriber;

import static com.zxw.dispatch.MyApplication.mContext;

/**
 * author：CangJie on 2016/9/20 16:52
 * email：cangjie2016@gmail.com
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    public LoginPresenter(LoginView mvpView) {
        super(mvpView);
    }

    public void verifyAccount(final String username, final String password, final boolean isCache){
        String time = String.valueOf(new Date().getTime());
        String md5Password = MD5.MD5Encode(MD5.MD5Encode(password) + time);
        HttpMethods.getInstance().login(new Subscriber<LoginBean>() {
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
            public void onNext(LoginBean loginBean) {
                if (isCache) {
                    SpUtils.setCache(mContext,"username",username);
                    SpUtils.setCache(mContext,"password",password);
                }else{
                    SpUtils.setCache(mContext,"username",null);
                    SpUtils.setCache(mContext,"password",null);
                }
                SpUtils.setCache(mContext, SpUtils.USER_ID, loginBean.userId);
                SpUtils.setCache(mContext, SpUtils.NAME, loginBean.name);
                SpUtils.setCache(mContext, SpUtils.KEYCODE, loginBean.keyCode);
            }
        }, username, md5Password, time);
    }

    public String getCacheUserName(){
        return SpUtils.getCache(mContext,"username");
    }

    public String getCachePassword(){
        return SpUtils.getCache(mContext,"password");
    }
}
