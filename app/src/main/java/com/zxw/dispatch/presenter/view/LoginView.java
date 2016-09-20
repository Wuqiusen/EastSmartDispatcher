package com.zxw.dispatch.presenter.view;

/**
 * author：CangJie on 2016/9/20 16:54
 * email：cangjie2016@gmail.com
 */
public interface LoginView extends BaseView {
    void loginSuccess();

    void loginFail(String info);
}
