package com.zxw.dispatch.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.adapter.MyPagerAdapter;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.GoneAdapterForNormal;
import com.zxw.dispatch.recycler.GoneAdapterForNotOperatorEmpty;
import com.zxw.dispatch.recycler.GoneAdapterForOperatorEmpty;

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
    private ChildViewPager vp_start_car;
    private RecyclerView eGoneRV1, eGoneRV2, eGoneRV3;
    private List<View> startViews = new ArrayList<View>();
    private OnStartCarTabListener mListener;
    private TextView eGoneRV1_tv_steward_show, eGoneRV2_tv_steward_show, eGoneRV3_tv_steward_show;

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
        vp_start_car = (ChildViewPager) findViewById(R.id.vp_start_car);

        MyPagerAdapter sAdapter = new MyPagerAdapter(inflateStartViews(),null);
        vp_start_car.setAdapter(sAdapter);
        vp_start_car.setCurrentItem(0);
        setStartCarTabScrollBar(0);
        vp_start_car.setPagingEnabled(false);
    }
    private List<View> inflateStartViews(){
        View view_stab1 = View.inflate(mContext,R.layout.item_gone_car1,null);
        eGoneRV1_tv_steward_show = (TextView) view_stab1.findViewById(R.id.tv_steward_show);
        eGoneRV1 = (RecyclerView) view_stab1.findViewById(R.id.rv_gone_car);
        eGoneRV1.setLayoutManager(new LinearLayoutManager(mContext));
        eGoneRV1.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));

        View view_stab2 = View.inflate(mContext,R.layout.item_gone_car2,null);
        eGoneRV2_tv_steward_show = (TextView) view_stab2.findViewById(R.id.tv_steward_show);
        eGoneRV2 = (RecyclerView) view_stab2.findViewById(R.id.rv_gone_car);
        eGoneRV2.setLayoutManager(new LinearLayoutManager(mContext));
        eGoneRV2.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));

        View view_stab3 = View.inflate(mContext,R.layout.item_gone_car2,null);
        eGoneRV3_tv_steward_show = (TextView) view_stab3.findViewById(R.id.tv_steward_show);
        eGoneRV3 = (RecyclerView) view_stab3.findViewById(R.id.rv_gone_car);
        eGoneRV3.setLayoutManager(new LinearLayoutManager(mContext));
        eGoneRV3.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));
        startViews.add(view_stab1);
        startViews.add(view_stab2);
        startViews.add(view_stab3);
        return startViews;
    }


    public void setTab1tStartCarCount(GoneAdapterForNormal goneAdapter){
        tv_stab1.setText(showCount(R.string.line_operate,goneAdapter.getCount()));
    }
    public void setTab2tStartCarCount(GoneAdapterForOperatorEmpty goneAdapter){
        tv_stab2.setText(showCount(R.string.operator_empty,goneAdapter.getCount()));
    }
    public void setTab3tStartCarCount(GoneAdapterForNotOperatorEmpty goneAdapter){
        tv_stab3.setText(showCount(R.string.not_operator_empty,goneAdapter.getCount()));
    }

    private String showCount(int stringRes,int carCount){
        String format= mContext.getResources().getString(stringRes);
        return String.format(format,carCount);
    }


    public void setEGoneRVAdapterForNormal(RecyclerView.Adapter adapter){
        eGoneRV1.setAdapter(adapter);
    }
    public void setEGoneRVAdapterForOperatorEmpty(RecyclerView.Adapter adapter){
        eGoneRV2.setAdapter(adapter);
    }
    public void setEGoneRVAdapterForNotOperatorEmpty(RecyclerView.Adapter adapter){
        eGoneRV3.setAdapter(adapter);
    }

    public void setStewardGoneVisibility(int isVisible) {
        eGoneRV1_tv_steward_show.setVisibility(isVisible);
        eGoneRV2_tv_steward_show.setVisibility(isVisible);
        eGoneRV3_tv_steward_show.setVisibility(isVisible);
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
