package com.zxw.dispatch.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.LoginPresenter;
import com.zxw.dispatch.presenter.view.LoginView;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.ui.my.ForgetPasswordActivity;

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

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        isShowTitleLinearLayout(View.GONE);
        tv_forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.checkbox_remark_style);
        drawable.setBounds(0,0,30,30);
        cb_rememberPwd.setCompoundDrawables(drawable,null,null,null);


        String cacheUserName = presenter.getCacheUserName();
        if (!TextUtils.isEmpty(cacheUserName)){
            et_username.setText(cacheUserName);
        }

        String cachePassword = presenter.getCachePassword();
        if (!TextUtils.isEmpty(cachePassword)){
            et_password.setText(cachePassword);
        }

        if (!TextUtils.isEmpty(cacheUserName) && !TextUtils.isEmpty(cachePassword) ) {
            cb_rememberPwd.setChecked(true);
        }else{
            cb_rememberPwd.setChecked(false);
        }
    }


    @OnClick({R.id.btn_login,R.id.tv_forget_password})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
                userName = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if(TextUtils.isEmpty(userName)){
                    disPlay("请输入用户名");
                    cb_rememberPwd.setChecked(false);
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    cb_rememberPwd.setChecked(false);
                    disPlay("请输入密码");
                    return;
                }
               presenter.verifyAccount(userName, password, cb_rememberPwd.isChecked());
              break;
            case R.id.tv_forget_password:
                Intent intent = new Intent(this,ForgetPasswordActivity.class);
                startActivity(intent);
                break;
        }
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
