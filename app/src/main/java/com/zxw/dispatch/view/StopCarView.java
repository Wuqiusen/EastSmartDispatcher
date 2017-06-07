package com.zxw.dispatch.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.MyPagerAdapter;
import com.zxw.dispatch.recycler.StopEndAdapter;
import com.zxw.dispatch.recycler.StopStayAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * author LZQ
 * create at 2017-4-7 17:43:50
 */
public class StopCarView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private TextView tv_wtab1;
    private TextView tv_wtab2;
    private OnStopCarTabListener mListener;
    private ChildViewPager vp_stop_car;
    private List<View> stopViews = new ArrayList<>();
    private RecyclerView eStopRV1;
    private RecyclerView eStopRV2;


    public StopCarView(Context context, int resId, OnStopCarTabListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(resId,this);
        tv_wtab1 = (TextView) findViewById(R.id.tv_hor_stop_tab1);
        tv_wtab2 = (TextView) findViewById(R.id.tv_hor_stop_tab2);
        tv_wtab1.setOnClickListener(this);
        tv_wtab2.setOnClickListener(this);
        vp_stop_car = (ChildViewPager) findViewById(R.id.vp_stop_car);

        MyPagerAdapter wAdapter = new MyPagerAdapter(inflateStopViews(),null);
        vp_stop_car.setAdapter(wAdapter);
        vp_stop_car.setCurrentItem(0);
        setStopCarTabScrollBar(0);
        vp_stop_car.setPagingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_hor_stop_tab1:
                mListener.onTabIsClick(0);
                break;
            case R.id.tv_hor_stop_tab2:
                mListener.onTabIsClick(1);
                break;
        }

    }


    private List<View> inflateStopViews(){
        View view_stab1 = View.inflate(mContext,R.layout.item_stop_car,null);
        eStopRV1 = (RecyclerView) view_stab1.findViewById(R.id.rv_menu_stop_car);
        eStopRV1.setLayoutManager(new GridLayoutManager(mContext,8));

        View view_stab2 = View.inflate(mContext,R.layout.item_stop_car1,null);
        eStopRV2 = (RecyclerView) view_stab2.findViewById(R.id.rv_menu_stop_car);
        eStopRV2.setLayoutManager(new GridLayoutManager(mContext,8));

        stopViews.add(view_stab1);
        stopViews.add(view_stab2);
        return stopViews;
    }

    public void setStopCarCurrentItem(int i) {
        vp_stop_car.setCurrentItem(i);
    }

    public void setTab1tStopCarCount(StopStayAdapter adapter){
        tv_wtab1.setText(showCount(R.string.stop_stay,adapter.getItemCount()-1));
    }
    public void setTab2tStopCarCount(StopEndAdapter adapter){
        tv_wtab2.setText(showCount(R.string.stop_end,adapter.getItemCount()));
    }

    private String showCount(int stringRes,int carCount){
        String format= mContext.getResources().getString(stringRes);
        return String.format(format,carCount);
    }

    public void setAdapterForStay(RecyclerView.Adapter adapter){
        eStopRV1.setAdapter(adapter);
    }
    public void setAdapterForEnd(RecyclerView.Adapter adapter){
        eStopRV2.setAdapter(adapter);
    }

    public void setStopCarTabScrollBar(int pos){
        setStopCarNormalTabStyle();
        switch (pos){
            case 0:
                tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 1:
                tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
        }
    }

    private void setStopCarNormalTabStyle() {
        tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
        tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
    }

    private Drawable getDrawable(boolean isTab) {
        if (isTab) {
            return mContext.getResources().getDrawable(R.drawable.tab_white_rectangle);
        }
        return mContext.getResources().getDrawable(R.drawable.line_blue_height);
    }


    public interface OnStopCarTabListener {
        void onTabIsClick(int pos);
    }
}
