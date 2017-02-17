package com.zxw.dispatch.recycler;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.ToastHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class StopAdapter extends RecyclerView.Adapter<StopAdapter.LineHolder> {
    private OnClickStopCarListListener listener = null;
    private List<StopHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public StopAdapter(List<StopHistory> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    public StopAdapter(List<StopHistory> mData, Context mContext, OnClickStopCarListListener listener) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.listener = listener;
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.smart_reusebtn, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        if (position == 0) {
            setBtnShowStyle(holder,0,mContext.getResources().getColor(R.color.white));
            holder.tvCarCodeOne.setText("手动添加");
            holder.llParentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastHelper.showToast("added");
                    if(listener != null)
                        listener.onClickManualButtonListener();
                }
            });
        }
        else if (position == 1){
            final StopHistory stop = mData.get(position);
            setBtnShowStyle(holder,1,R.drawable.ll_stop_car_green_btn_bg);
            holder.tvCarCodeOne.setText("car"+ position);
            holder.llParentContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onClickStopCarListener(stop);
                }
            });
        }
        else if (position == 2){
            setBtnShowStyle(holder,2,R.drawable.ll_stop_car_red_btn_bg);
            holder.tvCarCodeTwo.setText("car"+ position);
        }
        else{
            setBtnShowStyle(holder,1,R.drawable.ll_stop_car_blue_btn_bg);
            holder.tvCarCodeOne.setText("car"+ position);
        }

    }

    private void setBtnShowStyle(LineHolder holder,int type,int drawableResId){
           switch (type){
               case 0:
                   // 手动添加
                   holder.llTwoContainer.setVisibility(View.GONE);
                   holder.llOneContainer.setVisibility(View.VISIBLE);
                   holder.llOneContainer.setMinimumHeight(146);
                   holder.llOneContainer.setGravity(Gravity.CENTER_VERTICAL);
                   holder.tvOne.setVisibility(View.GONE);
                   holder.tvCarCodeOne.setVisibility(View.VISIBLE);
                   holder.tvCarCodeOne.setTextColor(mContext.getResources().getColor(R.color.font_gray));
                   break;
               case 1:
                   // 单层（红、绿、蓝）
                   holder.llTwoContainer.setVerticalGravity(View.GONE);
                   holder.llOneContainer.setVisibility(View.VISIBLE);
                   holder.tvOne.setVisibility(View.VISIBLE);
                   holder.tvCarCodeOne.setVisibility(View.VISIBLE);
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       holder.tvCarCodeOne.setTextColor(mContext.getResources().getColor(R.color.font_white));
                       holder.llParentContainer.setBackground(mContext.getResources().getDrawable(drawableResId));
                   }
                   break;
               case 2:
                   // 双层（红、绿）
                   holder.llOneContainer.setVisibility(View.GONE);
                   holder.llTwoContainer.setVisibility(View.VISIBLE);
                   holder.tvTwo.setVisibility(View.VISIBLE);
                   holder.tvCarCodeTwo.setVisibility(View.VISIBLE);
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       holder.tvCarCodeOne.setTextColor(mContext.getResources().getColor(R.color.font_white));
                       holder.llParentContainer.setBackground(mContext.getResources().getDrawable(drawableResId));
                       holder.llTwoContainer.setBackground(mContext.getResources().getDrawable(drawableResId));
                   }
                   break;
           }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class LineHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_container_parent)
        LinearLayout llParentContainer;
        // 布局1
        @Bind(R.id.ll_container_one)
        LinearLayout llOneContainer;
        @Bind(R.id.tv_one)
        TextView tvOne;
        @Bind(R.id.tv_car_code_one)
        TextView tvCarCodeOne;
        // 布局2
        @Bind(R.id.ll_container_two)
        LinearLayout llTwoContainer;
        @Bind(R.id.tv_two)
        TextView tvTwo;
        @Bind(R.id.tv_car_code_two)
        TextView tvCarCodeTwo;

        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnClickStopCarListListener{
        void onClickManualButtonListener();
        void onClickStopCarListener(StopHistory stopCar);
    }
}
