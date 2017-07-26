package com.zxw.dispatch.recycler;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.ClickUtil;
import com.zxw.dispatch.utils.ToastHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class StopStayAdapter extends RecyclerView.Adapter<StopStayAdapter.LineHolder> {
    private OnClickStopCarListListener listener = null;
    private List<StopHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public StopStayAdapter(List<StopHistory> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    public StopStayAdapter(List<StopHistory> mData, Context mContext, OnClickStopCarListListener listener) {
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

    private LineHolder mHolder = null;
    @Override
    public void onBindViewHolder(final LineHolder holder, final int position) {
        // 依据backStyle显示按钮样式(后期删除position判断)
        mHolder = holder;
        if (position == 0){
            // 手动添加
            holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_white_btn_bg));
            holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            holder.tvCarCode.setTextColor(mContext.getResources().getColor(R.color.font_gray));
            holder.tvCarCode.setText("手动添加");
            holder.llReuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onClickManualButtonListener();
                }
            });
        }else {
            setBtnStyle(holder, mData.get(position - 1));
            holder.tvCarCode.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.llReuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null && !ClickUtil.isFastDoubleClickStopBtn()) {
                        listener.onClickStopCarListener(mData.get(position - 1));
                    }
                }
            });
            holder.llReuse.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mData.get(position - 1).inType == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("提示");
                        builder.setMessage("确定要将" + mData.get(position - 1).code + "从停场车辆删除？");
                        builder.create();

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listener.onLongClickStopCarListener(mData.get(position - 1).id + "");
                                dialogInterface.dismiss();

                            }
                        });
                        builder.show();

                    }else {
                        ToastHelper.showToast("自动检测进站车辆禁止删除");
                    }
                    return true;
                }
            });
        }

    }



    private void setBtnStyle(LineHolder holder, final StopHistory stop){
           switch (stop.type){
               case 1:
                   // 单层（红）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
                   holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_red));
                   holder.tvCarCode.setText(stop.code);

                   break;
               case 2:
                   // 单层（绿）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
                   holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_green));
                   holder.tvCarCode.setText(stop.code);
                   break;
               case 3:
                   // 单层（蓝）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_blue_btn_bg));
                   holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_blue));
                   holder.tvCarCode.setText(stop.code);
                   holder.tvCarCode.setText(stop.code);
                   break;
               case 4:
                   // 双层（红）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
                   holder.llLayerReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
                   holder.tvCarCode.setText(stop.code);
                   break;
               case 5:
                   // 双层（绿）
                   holder.llReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
                   holder.llLayerReuse.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
                   holder.tvCarCode.setText(stop.code);
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
        void onLongClickStopCarListener(String objId);
    }
}
