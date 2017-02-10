package com.zxw.data.source;

import com.zxw.data.bean.ChangePwdBean;
import com.zxw.data.bean.SmsCodeBean;
import com.zxw.data.http.HttpMethods;

import rx.Subscriber;

/**
 * 作者：${MXQ} on 2017/2/9 11:39
 * 邮箱：1299242483@qq.com
 */
public class ForgetSource {
    // 获取验证码
    public void loadObtainCode(Subscriber<SmsCodeBean> subscriber, String code, String keyCode){
        HttpMethods.getInstance().obtainCode(subscriber,code,keyCode);
    }

    // 提交
    public void loadChangePwd(Subscriber<ChangePwdBean> subscriber,String code,String keyCode,String newPwd,String smsCode){
       HttpMethods.getInstance().changePwd(subscriber, code, keyCode, newPwd, smsCode);
    }

}
