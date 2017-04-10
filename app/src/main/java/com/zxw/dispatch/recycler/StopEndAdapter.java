package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class StopEndAdapter extends RecyclerView.Adapter<StopEndAdapter.LineHolder> {
    private OnClickStopEndCarListListener listener = null;
    private List<StopHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public StopEndAdapter(List<StopHistory> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    public StopEndAdapter(List<StopHistory> mData, Context mContext, OnClickStopEndCarListListener listener) {
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

        holder.llLayerReuse.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.tvCarCode.setText(mData.get(position).code);
            holder.tvCarCode.setTextColor(mContext.getResources().getColor(R.color.font_black));
            holder.llReuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onClickStopEndCarListener(mData.get(position));
                }
            });

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

    public interface OnClickStopEndCarListListener {
        void onClickStopEndCarListener(StopHistory stopCar);
    }
}
