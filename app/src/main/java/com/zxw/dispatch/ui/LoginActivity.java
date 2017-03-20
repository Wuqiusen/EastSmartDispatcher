package com.zxw.dispatch.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.LoginPresenter;
import com.zxw.dispatch.presenter.view.LoginView;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.ui.my.ForgetPasswordActivity;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends PresenterActivity<LoginPresenter> implements LoginView {
    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.tv_forget_password)
    TextView tv_forgetPwd;
    @Bind(R.id.cb_remember_pwd)
    CheckBox cb_rememberPwd;
    private String userName;
    private String password;
    private String befUserName;
    private String befPassword;
    private boolean remPwd = false;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        remPwd = initData();
        initView();
        initEvent();

    }

    private void initView() {
        //showTitle("用户登录");
        isShowTitleLinearLayout(View.GONE);
        tv_forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        if (remPwd) {
            cb_rememberPwd.setChecked(true);
        }else{
            cb_rememberPwd.setChecked(false);
        }
    }

    private boolean initData() {
        if (!TextUtils.isEmpty(SpUtils.getCache(mContext,"username"))){
            befUserName = SpUtils.getCache(mContext,"username");
            et_username.setText(befUserName);
        }
        if (!TextUtils.isEmpty(SpUtils.getCache(mContext,"password"))){
            befPassword = SpUtils.getCache(mContext,"password");
            et_password.setText(befPassword);
            DebugLog.i(befUserName+":"+befPassword);
            return true;
        }
        return false;
    }


    @OnClick({R.id.btn_login,R.id.tv_forget_password,R.id.cb_remember_pwd})
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
               disPlay("用户登录");
               DebugLog.i("登录userName/password:"+userName+"/"+password);
               presenter.verifyAccount(userName, password);
              break;
            case R.id.cb_remember_pwd:
                if (!initUserEditText() && cb_rememberPwd.isChecked()) {
                    disPlay("记住密码");
                    SpUtils.setCache(mContext,"username",userName);
                    SpUtils.setCache(mContext,"password",password);
                }else{
                    disPlay("取消记住密码");
                    SpUtils.setCache(mContext,"username",null);
                    SpUtils.setCache(mContext,"password",null);
                }
                break;

            case R.id.tv_forget_password:
                Intent intent = new Intent(this,ForgetPasswordActivity.class);
                startActivity(intent);
                break;

        }
    }

    private boolean initUserEditText() {
        userName = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(userName)){
            disPlay("请输入用户名");
            cb_rememberPwd.setChecked(false);
            return true;
        }
        if(TextUtils.isEmpty(password)){
            disPlay("请输入密码");
            cb_rememberPwd.setChecked(false);
            return true;
        }
        return false;
    }


    private void initEvent() {
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >0 && !s.equals(befUserName)){
                    SpUtils.setCache(mContext,"username",s.toString().trim());
                    disPlay("username:"+s.toString().trim());
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >0 && !s.equals(befPassword)){
                    SpUtils.setCache(mContext,"password",s.toString().trim());
                    disPlay("password:"+s.toString().trim());

                }
            }
        });

    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, SelectDispatcherPointActivity.class));
        finish();
    }

    @Override
    public void loginFail(String info) {
        disPlay(info);
    }
}
