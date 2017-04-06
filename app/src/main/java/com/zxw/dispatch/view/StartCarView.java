package com.zxw.dispatch.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.MyPagerAdapter;
import com.zxw.dispatch.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * author MXQ
 * create at 2017/4/6 10:36
 * email: 1299242483@qq.com
 */
public class StartCarView extends LinearLayout implements View.OnClickListener{

    private Context mContext;
    private TextView tv_stab1;
    private TextView tv_stab2;
    private TextView tv_stab3;
    private TextView tv_steward_gone;
    private CustomViewPager vp_start_car;
    private RecyclerView eGoneRV;
    private List<View> startViews = new ArrayList<View>();
    private OnStartCarTabListener mListener;

    public StartCarView(Context context,int resId,OnStartCarTabListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(resId,this);
        tv_stab1 = (TextView) findViewById(R.id.tv_stab1);
        tv_stab2 = (TextView) findViewById(R.id.tv_stab2);
        tv_stab3 = (TextView) findViewById(R.id.tv_stab3);
        tv_stab1.setOnClickListener(this);
        tv_stab2.setOnClickListener(this);
        tv_stab3.setOnClickListener(this);
        tv_steward_gone = (TextView) findViewById(R.id.tv_steward_gone);
        vp_start_car = (CustomViewPager) findViewById(R.id.vp_start_car);
        MyPagerAdapter sAdapter = new MyPagerAdapter(inflateStartViews(),null);
        vp_start_car.setAdapter(sAdapter);
        vp_start_car.setCurrentItem(0);
        setStartCarTabScrollBar(0);
        vp_start_car.setPagingEnabled(false);
    }
    private List<View> inflateStartViews(){
        View view_stab1 = View.inflate(mContext,R.layout.item_gone_car1,null);
        eGoneRV = (RecyclerView) view_stab1.findViewById(R.id.rv_gone_car);
        eGoneRV.setLayoutManager(new LinearLayoutManager(mContext));
        eGoneRV.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        View view_stab2 = View.inflate(mContext,R.layout.view_test2,null);
        View view_stab3 = View.inflate(mContext,R.layout.view_test3,null);
        startViews.add(view_stab1);
        startViews.add(view_stab2);
        startViews.add(view_stab3);
        return startViews;
    }



    public void setEGoneRVAdapter(RecyclerView.Adapter adapter){
        eGoneRV.setAdapter(adapter);
    }

    public void setStewardGoneVisibility(int isVisible) {
        tv_steward_gone.setVisibility(isVisible);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_stab1:
                mListener.onTabIsClick(0);
                break;
            case R.id.tv_stab2:
                mListener.onTabIsClick(1);
                break;
            case R.id.tv_stab3:
                mListener.onTabIsClick(2);
                break;
        }

    }

    public void setStartCarCurrentItem(int i) {
        vp_start_car.setCurrentItem(i);
    }

    public void setStartCarTabScrollBar(int pos){
        switch (pos){
            case 0:
                tv_stab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab3.setTextColor(mContext.getResources().getColor(R.color.font_black));

                tv_stab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_stab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_stab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 1:
                tv_stab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab3.setTextColor(mContext.getResources().getColor(R.color.font_black));

                tv_stab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_stab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_stab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 2:
                tv_stab3.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab2.setTextColor(mContext.getResources().getColor(R.color.font_black));

                tv_stab3.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_stab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_stab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;

        }
    }

    private Drawable getDrawable(boolean isTab) {
        if (isTab) {
            return mContext.getResources().getDrawable(R.drawable.tab_white_rectangle);
        }
        return mContext.getResources().getDrawable(R.drawable.line_blue_height);
    }


    public interface OnStartCarTabListener{
        void onTabIsClick(int pos);
    }


}
