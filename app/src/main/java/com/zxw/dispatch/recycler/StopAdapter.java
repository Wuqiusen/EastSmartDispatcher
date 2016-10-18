package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DisplayTimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class StopAdapter extends RecyclerView.Adapter<StopAdapter.LineHolder> {
    private List<StopHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public StopAdapter(List<StopHistory> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_stop, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        StopHistory stop = mData.get(position);
        holder.mCarSequence.setText(String.valueOf(stop.sortNum));
        holder.mCarCode.setText(stop.vehCode);
        holder.mDriver.setText(stop.sjName);
        holder.mTrainman.setText(stop.scName);
        holder.mPlanTime.setText(DisplayTimeUtil.substring(stop.projectTime));
        holder.mIntervalTime.setText(String.valueOf(stop.spaceMin));
        holder.mSystemEnterTime.setText(DisplayTimeUtil.substring(stop.inTime1));
        holder.mEnterTime.setText(DisplayTimeUtil.substring(stop.inTime2));
        holder.mState.setText(stop.isScan == 1 ? "已读" : "未读");
        holder.mStopTime.setText(DisplayTimeUtil.substring(stop.stopTime));
        holder.mRemark.setText(stop.remarks);
        holder.mOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class LineHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_car_sequence)
        TextView mCarSequence;
        @Bind(R.id.tv_car_code)
        TextView mCarCode;
        @Bind(R.id.tv_driver)
        TextView mDriver;
        @Bind(R.id.tv_trainman)
        TextView mTrainman;
        @Bind(R.id.tv_plan_time)
        TextView mPlanTime;
        @Bind(R.id.tv_interval_time)
        TextView mIntervalTime;
        @Bind(R.id.tv_system_enter_time)
        TextView mSystemEnterTime;
        @Bind(R.id.tv_enter_time)
        TextView mEnterTime;
        @Bind(R.id.tv_stop_time)
        TextView mStopTime;
        @Bind(R.id.tv_remark)
        TextView mRemark;
        @Bind(R.id.tv_state)
        TextView mState;
        @Bind(R.id.tv_operator)
        TextView mOperator;

        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
