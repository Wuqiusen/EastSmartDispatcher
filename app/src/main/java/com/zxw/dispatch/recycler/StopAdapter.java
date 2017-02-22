package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DebugLog;
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
        final StopHistory stop = mData.get(position);
        // 依据backStyle显示按钮样式(后期删除position判断)
        int mBackStyle = stop.backStyle;
        if (mBackStyle == 1 || position == 0){       // 手动添加
              holder.tvCarCode.setText("手动添加");
              setBtnStyle(holder,1);
              holder.llReuse.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                       if(listener != null)
                         listener.onClickManualButtonListener();
                  }
              });
        }else if(mBackStyle == 2 || position == 1){  // 单层(绿)
            setBtnStyle(holder,2);
            isVehCodeEmpty(holder,stop.vehCode);
            holder.llReuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onClickStopCarListener(stop);
                }
            });
        }else if(mBackStyle == 3 || position == 2){  // 单层(红)
            setBtnStyle(holder,3);
            isVehCodeEmpty(holder,stop.vehCode);
        }else if(mBackStyle == 4){ // 单层(蓝)
            setBtnStyle(holder,4);
            isVehCodeEmpty(holder,stop.vehCode);
        }else if(mBackStyle == 5){ // 双层(红)
            setBtnStyle(holder,5);
            isVehCodeEmpty(holder,stop.vehCode);
        }else{                     // 双层(绿)
            setBtnStyle(holder,6);
            isVehCodeEmpty(holder,stop.vehCode);
        }

    }

    private void isVehCodeEmpty(LineHolder holder, String vehCode){
        if (!TextUtils.isEmpty(vehCode)){
            holder.tvCarCode.setText("粤BB"+ vehCode);
        }else{
            holder.tvCarCode.setText("粤BB" + "1234");
            DebugLog.e(vehCode + "");
        }
    }

    private void setBtnStyle(LineHolder holder,int type){
           switch (type){
               case 1:
                   // 手动添加
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_white_btn_bg));
                   holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                   holder.tvCarCode.setTextColor(mContext.getResources().getColor(R.color.font_gray));
                   break;
               case 2:
                   // 单层（绿）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
                   holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_green));
                   break;
               case 3:
                   // 单层（红）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
                   holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_red));
                   break;
               case 4:
                   // 单层（蓝）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_blue_btn_bg));
                   holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_blue));
                   break;
               case 5:
                   // 双层（红）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
                   holder.llLayerReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
                   break;
               case 6:
                   // 双层（绿）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
                   holder.llLayerReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
                   break;
           }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class LineHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_reuse)
        LinearLayout llReuse;
        @Bind(R.id.ll_layer_reuse)
        LinearLayout llLayerReuse;
        @Bind(R.id.tv_car_code)
        TextView tvCarCode;

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
