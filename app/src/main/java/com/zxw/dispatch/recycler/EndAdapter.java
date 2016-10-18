package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxw.data.bean.BackHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DisplayTimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class EndAdapter extends RecyclerView.Adapter<EndAdapter.LineHolder> {
    private List<BackHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public EndAdapter(List<BackHistory> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_end, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        BackHistory back = mData.get(position);
        holder.mCarSequence.setText(String.valueOf(back.sortNum));
        holder.mCarCode.setText(back.vehCode);
        holder.mDriver.setText(back.sjName);
        holder.mTrainman.setText(back.scName);
        holder.mPlanTime.setText(DisplayTimeUtil.substring(back.projectTime));
        holder.mIntervalTime.setText(String.valueOf(back.spaceMin));
        holder.mSystemEnterTime.setText(DisplayTimeUtil.substring(back.inTime1));
        holder.mEnterTime.setText(DisplayTimeUtil.substring(back.inTime2));
        holder.mEndTime.setText(DisplayTimeUtil.substring(back.backTime));
        holder.mRemark.setText("");
        holder.mState.setText(back.isScan == 1 ? "已读" : "未读");
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
        @Bind(R.id.tv_end_time)
        TextView mEndTime;
        @Bind(R.id.tv_remark)
        TextView mRemark;
        @Bind(R.id.tv_state)
        TextView mState;

        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
