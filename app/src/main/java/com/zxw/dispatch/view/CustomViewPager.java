package com.zxw.dispatch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zxw.dispatch.utils.DebugLog;

/**
 * 作者：${MXQ} on 2017/2/8 10:31
 * 邮箱：1299242483@qq.com
 */
public class CustomViewPager extends LazyViewPager {

    private boolean isPagingEnabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }


    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}