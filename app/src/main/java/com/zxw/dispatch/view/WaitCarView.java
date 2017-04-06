package com.zxw.dispatch.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.adapter.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * author MXQ
 * create at 2017/4/6 15:06
 * email: 1299242483@qq.com
 */
public class WaitCarView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private TextView tv_wtab1;
    private TextView tv_wtab2;
    private TextView tv_wtab3;
    private OnWaitCarTabListener mListener;
    private TextView tv_steward_send;
    private CustomViewPager vp_wait_car;
    private View eViewCover;
    private DragListView eSendRV;
    private List<View> waitViews = new ArrayList<>();


    public WaitCarView(Context context,int resId,OnWaitCarTabListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(resId,this);
        tv_wtab1 = (TextView) findViewById(R.id.tv_hor_wtab1);
        tv_wtab2 = (TextView) findViewById(R.id.tv_hor_wtab2);
        tv_wtab3 = (TextView) findViewById(R.id.tv_hor_wtab3);
        tv_wtab1.setOnClickListener(this);
        tv_wtab2.setOnClickListener(this);
        tv_wtab3.setOnClickListener(this);
        tv_steward_send = (TextView) findViewById(R.id.tv_steward_send);
        vp_wait_car = (CustomViewPager) findViewById(R.id.vp_wait_car);

        MyPagerAdapter wAdapter = new MyPagerAdapter(inflateWaitViews(),null);
        vp_wait_car.setAdapter(wAdapter);
        vp_wait_car.setCurrentItem(0);
        setWaitCarTabScrollBar(0);
        vp_wait_car.setPagingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_wtab1:
                mListener.onTabIsClick(0);
                break;
            case R.id.tv_wtab2:
                mListener.onTabIsClick(1);
                break;
            case R.id.tv_wtab3:
                mListener.onTabIsClick(2);
                break;
        }

    }


    private List<View> inflateWaitViews(){
        View view_wtab = View.inflate(mContext, R.layout.item_wait_car1,null);
        eViewCover = (View) view_wtab.findViewById(R.id.view_cover);
        eSendRV = (DragListView) view_wtab.findViewById(R.id.lv_send_car);
        View view4 = View.inflate(mContext,R.layout.view_test2,null);
        View view5 = View.inflate(mContext,R.layout.view_test3,null);
        waitViews.add(view_wtab);
        waitViews.add(view4);
        waitViews.add(view5);
        return waitViews;
    }

    public void setWaitCarCurrentItem(int i) {
        vp_wait_car.setCurrentItem(i);
    }

    public void setAdapter(DragListAdapter adapter){
       eSendRV.setAdapter(adapter);
    }

    public void setMyDragListener(DragListView.MyDragListener listener){
        eSendRV.setMyDragListener(listener);
    }

    public void setStewardSendVisibility(int isVisible) {
        tv_steward_send.setVisibility(isVisible);
    }

    public void setViewCoverVisibility(int isVisible) {
        eViewCover.setVisibility(isVisible);
    }



    public void setWaitCarTabScrollBar(int pos){
        switch (pos){
            case 0:
                tv_wtab1.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tv_wtab2.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tv_wtab3.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                break;
            case 1:
                tv_wtab2.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tv_wtab1.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tv_wtab3.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                break;
            case 2:
                tv_wtab3.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tv_wtab1.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tv_wtab2.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                break;

        }
    }

    private Drawable getDrawable(boolean isTab) {
        if (isTab) {
            return mContext.getResources().getDrawable(R.drawable.tab_white_rectangle);
        }
        return mContext.getResources().getDrawable(R.drawable.line_blue_height);
    }


    public interface OnWaitCarTabListener{
        void onTabIsClick(int pos);
    }
}
