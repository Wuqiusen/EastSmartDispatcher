package com.zxw.dispatch.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.zxw.data.bean.SmsCodeBean;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.ForgetPwdPresenter;
import com.zxw.dispatch.presenter.view.ForgetPwdView;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.utils.GetCodeBtnStyle;
import com.zxw.dispatch.utils.IsNetworkAvailable;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.MyEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：${MXQ} on 2017/2/8 16:44
 * 邮箱：1299242483@qq.com
 */
public class ForgetPasswordActivity extends PresenterActivity<ForgetPwdPresenter> implements ForgetPwdView {
    @Bind(R.id.tv_user_code)
    MyEditText tv_code;
    @Bind(R.id.et_user_pwd_new)
    MyEditText tv_pwd_new;
    @Bind(R.id.tv_user_pwd_twice)
    MyEditText tv_pwd_twice;
    @Bind(R.id.et_smscode)
    MyEditText et_smscode;
    @Bind(R.id.btn_get_code)
    Button btn_get_code;
    @Bind(R.id.btn_change_pwd_send)
    Button btn_change_pwd_send;
    private String tvCode;
    private String etNewPwd;
    private String etTwicePwd;
    private String etSmsCode;

    @Override
    protected ForgetPwdPresenter createPresenter() {
        return new ForgetPwdPresenter(this,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        showTitle("忘记密码");
        showBackButton();

    }

    @OnClick({R.id.btn_get_code,R.id.btn_change_pwd_send})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_get_code:
                tvCode = tv_code.getText().toString();
                et_smscode.requestFocus();
                if (!checkIsEmpty(tvCode,"请先输入工号")){
                    if (!IsNetworkAvailable.isNetworkAvailable(ForgetPasswordActivity.this)) {
                        disPlay("当前网络不可用");
                    }else{
                        btn_get_code.setClickable(false);
                        // 获取验证码{待修改}
                        presenter.obtainSmsCode();
                    }
                }
                break;
            case R.id.btn_change_pwd_send:
                // 提交按钮
                ChangePWD();
                break;
        }
    }

    private void ChangePWD() {
        tvCode = tv_code.getText().toString();
        etNewPwd = tv_pwd_new.getText().toString();
        etTwicePwd = tv_pwd_twice.getText().toString();
        etSmsCode = et_smscode.getText().toString();
        if (TextUtils.isEmpty(etNewPwd) || TextUtils.isEmpty(etTwicePwd)){
            ToastHelper.showToast("密码不可为空，请认真设置！");
            return;
        }
        if (!etNewPwd.equals(etTwicePwd)){
            ToastHelper.showToast("两次密码不相同，请重新设置！");
            return;
        }
        if (etNewPwd.length() < 8) {
            ToastHelper.showToast("密码长度不能小于8位！");
            return;
        }
        if (etNewPwd.length() > 15) {
            ToastHelper.showToast("密码长度不能大于15位！");
            return;
        }
        if (TextUtils.isEmpty(etSmsCode)) {
            ToastHelper.showToast("验证码不可为空！");
            return;
        }
        // 提交请求{待修改}
        presenter.setChangePwd(etNewPwd,etSmsCode);
    }


    @Override
    public void smsCodeGetFailed() {
        GetCodeBtnStyle.btnStyle().setClickTrue();
    }


    @Override
    public void smsCodeGetSuccess(SmsCodeBean smsCodeBean) {
        GetCodeBtnStyle.btnStyle().GetCodeBtnStyle(btn_get_code);
    }

}
