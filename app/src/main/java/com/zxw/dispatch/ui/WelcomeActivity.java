package com.zxw.dispatch.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxw.data.bean.VersionBean;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.WelcomePresenter;
import com.zxw.dispatch.presenter.view.WelcomeView;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.DownLoadAndSetUpAPK;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.MyDialog;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WelcomeActivity extends PresenterActivity<WelcomePresenter> implements WelcomeView {
    @Bind(R.id.img_welcome)
    ImageView img_welcome;
    @Bind(R.id.tv_version_name)
    TextView tv_version_name;

    private PackageManager pm;
    private int currentVersion;
    private String downUrl;

    @Override
    protected WelcomePresenter createPresenter() {
        return new WelcomePresenter(this,this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initData();
        hideHeadArea();

        animation();

    }

    private void initData() {
        pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(),0);
            String versionName = info.versionName;
            tv_version_name.setText("V "+versionName);
            currentVersion = info.versionCode;
            DebugLog.i("当前版本号:"+currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void animation() {
        // 初始化控件
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2F, 0.9F);
        animationSet.setDuration(1000);
        animationSet.addAnimation(alphaAnimation);
        // 监听动画过程
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                presenter.upLoadLog();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 版本更新
                 presenter.checkVersion();

            }
        });
        img_welcome.startAnimation(animationSet);
    }


    @Override
    public void getVersionDataSuccess(final VersionBean versionBean) {
        if (versionBean != null && versionBean.codeNum > currentVersion) {
            final MyDialog newVersionDialog = new MyDialog(this, "版本信息", versionBean.remarks, MyDialog.HAVEBUTTON);
            newVersionDialog.show();
            newVersionDialog.setCancelable(false);
            newVersionDialog.ButtonCancel(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newVersionDialog.dismiss();
                    loadMain();

                }
            });
            newVersionDialog.ButtonQuery(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newVersionDialog.dismiss();
                    new DownLoadAndSetUpAPK().DownLoadAndSetUpAPK(WelcomeActivity.this, versionBean.url, new DownLoadAndSetUpAPK.LoadFailure() {
                        @Override
                        public void onLoadFailureListener() {
                            ToastHelper.showToast("更新失败", mContext);
                        }
                    });
                }
            });
        }
    }




    public void loadMain() {
        if(isUserLogin()){
            goToMainPage();
        }else{
            goToLoginPage();
        }
    }


    public boolean isUserLogin(){
        String code = SpUtils.getCache(MyApplication.mContext, SpUtils.CODE);
        String keycode = SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);
        DebugLog.w(code);
        DebugLog.w(keycode);
        if(TextUtils.isEmpty(code) || TextUtils.isEmpty(keycode))
            return false;
        return true;
    }

    public void goToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();//关闭当前界面
    }

    public void goToLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();//关闭当前界面
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadMain();
    }
}
