package com.zxw.dispatch.presenter.view;


public interface CallLoginView extends BaseView {
    void loginSuccess();

    void loginFail(String message);
}
