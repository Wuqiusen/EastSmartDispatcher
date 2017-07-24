package com.zxw.dispatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.commucation.ConnectUtil;
import com.zxw.dispatch.commucation.RealCallActivity;
import com.zxw.dispatch.presenter.CallLoginPresenter;
import com.zxw.dispatch.presenter.view.CallLoginView;
import com.zxw.dispatch.ui.base.PresenterActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


public class CallLoginActivity extends PresenterActivity<CallLoginPresenter> implements CallLoginView {

    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    private String userName;
    private String password;
    @Override
    protected CallLoginPresenter createPresenter() {
        return new CallLoginPresenter(this, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_login);
        ButterKnife.bind(this);
        String token = "tPVt59myZ0CAtwYWy8QyI1qTWmUx1yakeXAwiP7BXEVWgTIq2m1r/5IyljXVt3j4GPk9w+sOERHuyNGYA1TCMg==";
        ConnectUtil.connect(MyApplication.mContext.getApplicationInfo(), mContext, token);
    }

    @OnClick({R.id.btn_login,R.id.tv_forget_password})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                userName = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(TextUtils.isEmpty(userName)){
                    disPlay("请输入用户名");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    disPlay("请输入密码");
                    return;
                }
                presenter.verifyAccount(userName, password);
                break;
        }
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, CallLauncherActivity.class));
        finish();
    }

    @Override
    public void loginFail(String message) {

    }
}
