package com.zxw.dispatch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.zxw.dispatch.R;

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
                loadMain();
            }
        });
        img_welcome.startAnimation(animationSet);
    }

    public void loadMain() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();//关闭当前界面
    }
}
