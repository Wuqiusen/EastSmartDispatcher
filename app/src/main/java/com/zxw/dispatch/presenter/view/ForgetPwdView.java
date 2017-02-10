package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.SmsCodeBean;

/**
 * 作者：${MXQ} on 2017/2/8 16:45
 * 邮箱：1299242483@qq.com
 */
public interface ForgetPwdView extends BaseView{
    void smsCodeGetSuccess(SmsCodeBean smsCodeBean);
    void smsCodeGetFailed();
}
