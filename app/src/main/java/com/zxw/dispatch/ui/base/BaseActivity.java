package com.zxw.dispatch.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

import com.zxw.dispatch.utils.ClickUtil;
import com.zxw.dispatch.utils.ToastHelper;


public abstract class BaseActivity extends FragmentActivity{
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    protected abstract void showLoading();
    protected abstract void hideLoading();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (ClickUtil.isFastDoubleClick()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
}

    public void disPlay(String toast){
        ToastHelper.showToast(toast,this);
    }
}
