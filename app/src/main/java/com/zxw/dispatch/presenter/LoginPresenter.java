package com.zxw.dispatch.presenter;

import com.zxw.dispatch.presenter.view.LoginView;

/**
 * author：CangJie on 2016/9/20 16:52
 * email：cangjie2016@gmail.com
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    public LoginPresenter(LoginView mvpView) {
        super(mvpView);
    }

    public void verifyAccount(String username, String password){
        if(username.equals("zxzs") && password.equals("123123")){
            mvpView.loginSuccess();
        }else{
            mvpView.loginFail("用户名或密码错误");
        }
    }
}
