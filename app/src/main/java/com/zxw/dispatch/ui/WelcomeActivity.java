package com.zxw.dispatch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {
    @Bind(R.id.img_welcome)
    ImageView img_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        animation();
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

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(isUserLogin()){
                    goToMainPage();
                }else{
                    goToLoginPage();
                }
            }
        });
        img_welcome.startAnimation(animationSet);
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
}
