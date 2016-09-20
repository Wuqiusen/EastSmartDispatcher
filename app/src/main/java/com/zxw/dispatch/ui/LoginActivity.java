package com.zxw.dispatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.zxw.dispatch.MainActivity;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.LoginPresenter;
import com.zxw.dispatch.presenter.view.LoginView;
import com.zxw.dispatch.ui.base.PresenterActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends PresenterActivity<LoginPresenter> implements LoginView {
    @Bind(R.id.et_username)
    EditText et_username;
    @Bind(R.id.et_password)
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        showTitle("用户登录");

    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @OnClick(R.id.btn_login)
    public void verifyAccount(){
        String userName = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(userName)){
            disPlay("请输入用户名");
            return;
        }
        if(TextUtils.isEmpty(password)){
            disPlay("请输入密码");
            return;
        }
        presenter.verifyAccount(userName, password);
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void loginFail(String info) {
        disPlay(info);
    }
}
