package com.zxw.dispatch.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.adapter.DragListAdapterForNotOperatorEmpty;
import com.zxw.dispatch.adapter.DragListAdapterForOperatorEmpty;
import com.zxw.dispatch.adapter.MyPagerAdapter;
import com.zxw.dispatch.ui.MainActivity;

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
    private ChildViewPager vp_wait_car;
    private View eViewCover;
    private DragListView eSendRV1, eSendRV2, eSendRV3;
    private List<View> waitViews = new ArrayList<>();
    private TextView eSendRV1_tv_steward_show, eSendRV2_tv_steward_show, eSendRV3_tv_steward_show;


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
        vp_wait_car = (ChildViewPager) findViewById(R.id.vp_wait_car);

        MyPagerAdapter wAdapter = new MyPagerAdapter(inflateWaitViews(),null);
        vp_wait_car.setAdapter(wAdapter);
        vp_wait_car.setCurrentItem(0);
        setWaitCarTabScrollBar(0);
        ((MainActivity)mContext).showMenuAutoDepart(View.VISIBLE);
        vp_wait_car.setPagingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_hor_wtab1:
                mListener.onTabIsClick(0,View.VISIBLE);
                break;
            case R.id.tv_hor_wtab2:
                mListener.onTabIsClick(1,View.INVISIBLE);
                break;
            case R.id.tv_hor_wtab3:
                mListener.onTabIsClick(2,View.INVISIBLE);
                break;
        }

    }


    private List<View> inflateWaitViews(){
        View view_wtab = View.inflate(mContext, R.layout.item_wait_car1,null);
        eSendRV1_tv_steward_show = (TextView) view_wtab.findViewById(R.id.tv_steward_show);
        eViewCover = (View) view_wtab.findViewById(R.id.view_cover);
        eSendRV1 = (DragListView) view_wtab.findViewById(R.id.lv_send_car);

        View view_wtab2 = View.inflate(mContext, R.layout.item_wait_car2,null);
        eSendRV2_tv_steward_show = (TextView) view_wtab2.findViewById(R.id.tv_steward_show);
        eSendRV2 = (DragListView) view_wtab2.findViewById(R.id.lv_send_car);

        View view_wtab3 = View.inflate(mContext, R.layout.item_wait_car2,null);
        eSendRV3_tv_steward_show = (TextView) view_wtab3.findViewById(R.id.tv_steward_show);
        eSendRV3 = (DragListView) view_wtab3.findViewById(R.id.lv_send_car);

        waitViews.add(view_wtab);
        waitViews.add(view_wtab2);
        waitViews.add(view_wtab3);
        return waitViews;
    }

    public void setWaitCarCurrentItem(int i) {
        vp_wait_car.setCurrentItem(i);
    }

    public void setTab1tWaitCarCount(DragListAdapter adapter){
        tv_wtab1.setText(showCount(R.string.line_operate,adapter.getCount()));
    }

    public void setTab2tWaitCarCount(DragListAdapterForOperatorEmpty mDragListAdapter){
        tv_wtab2.setText(showCount(R.string.operator_empty,mDragListAdapter.getCount()));
    }

    public void setTab3tWaitCarCount(DragListAdapterForNotOperatorEmpty mDragListAdapter){
        tv_wtab3.setText(showCount(R.string.not_operator_empty,mDragListAdapter.getCount()));
    }

    private String showCount(int stringRes,int carCount){
        String format= mContext.getResources().getString(stringRes);
        return String.format(format,carCount);
    }

    public void setAdapterForNormal(DragListAdapter adapter){
       eSendRV1.setAdapter(adapter);
    }
    public void setAdapterForOperatorEmpty(DragListAdapterForOperatorEmpty adapter){
       eSendRV2.setAdapter(adapter);
    }
    public void setAdapterForNotOperatorEmpty(DragListAdapterForNotOperatorEmpty adapter){
       eSendRV3.setAdapter(adapter);
    }

    public void setMyDragListener(DragListView.MyDragListener listener){
        eSendRV1.setMyDragListener(listener);
    }

    public void setStewardSendVisibility(int isVisible) {
        eSendRV1_tv_steward_show.setVisibility(isVisible);
        eSendRV2_tv_steward_show.setVisibility(isVisible);
        eSendRV3_tv_steward_show.setVisibility(isVisible);
    }

    public void setViewCoverVisibility(int isVisible) {
        eViewCover.setVisibility(isVisible);
    }



    public void setWaitCarTabScrollBar(int pos){
        switch (pos){
            case 0:
                tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab3.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_wtab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 1:
                tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab3.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_wtab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 2:
                tv_wtab3.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab3.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;

        }
    }



    public interface OnWaitCarTabListener{
        void onTabIsClick(int pos,int isVisible);
    }
}
