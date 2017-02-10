package com.zxw.dispatch.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DebugLog;

import static android.widget.AdapterView.INVALID_POSITION;

/**
 * author：CangJie on 2017/2/10 14:45
 * email：cangjie2016@gmail.com
 */
public class DragRecyclerView extends RecyclerView {

    private boolean isLock;// 是否上锁.
    private View dragView; // 拖动的条目

    private int dragPoint;// 在当前数据项中的位置
    private int dragYOffset;// 当前视图和屏幕的距离(这里只使用了y方向上)
    private WindowManager.LayoutParams windowParams;
    private WindowManager windowManager;

    private ImageView dragImageView;// 被拖拽的项(item)，其实就是一个ImageView

    public DragRecyclerView(Context context) {
        super(context);
    }

    public DragRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param isLock 拖拽功能的开关，true为关闭
     */
    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }

    /***
     * touch事件拦截 在这里我进行相应拦截，
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 按下
        if (ev.getAction() == MotionEvent.ACTION_DOWN && !isLock) {
            int x = (int) ev.getX();// 获取相对与ListView的x坐标
            int y = (int) ev.getY();// 获取相应与ListView的y坐标
            DebugLog.w("list onInterceptTouchEvent ev.x" + x + ", ev.y "+ y);
            // 获取当前位置的item视图(可见状态)
            dragView = findChildViewUnder(x, y);
            // 无效不进行处理
            if (indexOfChild(dragView) == INVALID_POSITION) {
                return super.onInterceptTouchEvent(ev);
            }

            // 获取到的dragPoint其实就是在你点击指定item项中的高度.
            dragPoint = y - dragView.getTop();
            // 这个值是固定的:其实就是ListView这个控件与屏幕最顶部的距离（一般为标题栏+状态栏.
            dragYOffset = (int) (ev.getRawY() - y);

            // 获取可拖拽的图标
            View dragger = dragView.findViewById(R.id.ll_container);
            //点击的x坐标大于移动按钮的x坐标，就当成是按到了iv_move触发了移动
            if (dragger != null) { //如果想点击item的任意位置都能进行拖拽，把x > dragger.getLeft()限定去掉就行
                dragView.setBackgroundColor(Color.parseColor("#a35151"));
                dragView.setDrawingCacheEnabled(true);// 开启cache.
                Bitmap bm = Bitmap.createBitmap(dragView.getDrawingCache());// 根据cache创建一个新的bitmap对象,就是你拖着狂奔的对象
                startDrag(bm, y);// 初始化影像
            }
            return false;
        }
        return false;
    }


    /**
     * 触摸事件处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        DebugLog.w("list onTouchEvent ev.x" + ev.getX() + ", ev.y "+ ev.getY());
        // item的view不为空，且获取的dragPosition有效
        if (dragImageView != null && !isLock) {

            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    int upY = (int) ev.getY();
                    stopDrag(upY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int moveY = (int) ev.getY();
                    onDrag(moveY);

                    break;
                case MotionEvent.ACTION_DOWN:
                    break;
                default:
                    break;
            }
            return true;// 取消ListView滑动.
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 拖动执行，在Move方法中执行
     *
     * @param y
     */
    public void onDrag(int y) {
        int drag_top = y - dragPoint;// 拖拽view的top值不能＜0，否则则出界.
        if (dragImageView != null && drag_top >= 0) {
            windowParams.alpha = 0.65f;
            windowParams.y = y - dragPoint + dragYOffset;
            windowManager.updateViewLayout(dragImageView, windowParams);// 时时移动(拖拽移动的核心)
        }
    }

    /**
     * 停止拖动，删除影像
     */
    public void stopDrag(int y) {
        // 判断是否在发车列表控件之上

        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    /**
     * 准备拖动，初始化拖动项的图像
     *
     * @param bm
     * @param y
     */
    private void startDrag(Bitmap bm, int y) {
        /***
         * 初始化window.
         */
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - dragPoint + dragYOffset;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE// 不需获取焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE// 不需接受触摸事件
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON// 保持设备常开，并保持亮度不变。
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // 窗口占满整个屏幕，忽略周围的装饰边框（例如状态栏）。此窗口需考虑到装饰边框的内容。

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;

    }

}
