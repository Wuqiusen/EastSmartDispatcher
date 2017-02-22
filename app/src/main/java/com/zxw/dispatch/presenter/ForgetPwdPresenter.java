package com.zxw.dispatch.presenter;

import android.content.Context;

import com.zxw.data.bean.ChangePwdBean;
import com.zxw.data.bean.SmsCodeBean;
import com.zxw.data.source.ForgetSource;
import com.zxw.dispatch.presenter.view.ForgetPwdView;
import com.zxw.dispatch.utils.SpUtils;

import rx.Subscriber;


/**
 * 作者：${MXQ} on 2017/2/8 16:45
 * 邮箱：1299242483@qq.com
 */
public class ForgetPwdPresenter extends BasePresenter<ForgetPwdView>{

    private final ForgetSource mSource;
    private Context fContext;

    public ForgetPwdPresenter(ForgetPwdView mvpView, Context context) {
        super(mvpView);
        this.fContext = context;
        mSource = new ForgetSource();
    }

    /***
     * 获取验证码{待修改：URL 和 参数对象}
     */
    public void obtainSmsCode() {
       mSource.loadObtainCode(new Subscriber<SmsCodeBean>() {
           @Override
           public void onCompleted() {

           }

           @Override
           public void onError(Throwable e) {
               mvpView.disPlay(e.getMessage());
               mvpView.smsCodeGetFailed();
           }

           @Override
           public void onNext(SmsCodeBean smsCodeBean) {
               mvpView.smsCodeGetSuccess(smsCodeBean);
           }
       },SpUtils.getCache(fContext,SpUtils.USER_ID),SpUtils.getCache(fContext,SpUtils.KEYCODE));
    }


    /**
     * 提交：修改密码{待修改：URL 和 参数对象}
     * @param etNewPwd  新密码
     * @param etSmsCode 验证码
     */
    public void setChangePwd(String etNewPwd, String etSmsCode) {
        mSource.loadChangePwd(new Subscriber<ChangePwdBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(ChangePwdBean changePwdBean) {
                mvpView.disPlay("修改密码成功！");
                mvpView.finish();
            }
        },SpUtils.getCache(fContext,SpUtils.USER_ID),SpUtils.getCache(fContext,SpUtils.KEYCODE),etNewPwd,etSmsCode);
    }


}
