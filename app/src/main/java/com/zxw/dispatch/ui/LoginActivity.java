package com.zxw.dispatch.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

//        et_username.setOnLoadValueListener(new OnLoadValueListener() {
//            @Override
//            public void onLoadValue() {
////                List<Car> carCode = new ArrayList();
////                Car car1 = new Car();
////                car1.id = 3;
////                car1.vehicleCode = "fd222";
////                carCode.add(car1);
////                Car car2 = new Car();
////                car2.id = 5;
////                car2.vehicleCode = "fd226";
////                carCode.add(car2);
//                QuerySource source = new QuerySource();
//                source.queryVehcile(new Subscriber<List<Vehcile>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<Vehcile> vehciles) {
//
//                    }
//                });
//                et_username.setCarAdapter(new CarAdapter(carCode, et_username, mContext));
//            }
//        });
    }

    private void initView() {
        showTitle("用户登录");
        tv_forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @OnClick({R.id.btn_login,R.id.tv_forget_password})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:
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
                break;
            case R.id.tv_forget_password:
                Intent intent = new Intent(this,ForgetPasswordActivity.class);
                startActivity(intent);
                break;

        }
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
