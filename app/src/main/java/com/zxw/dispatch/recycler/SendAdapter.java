package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxw.data.bean.SendHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DisplayTimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class SendAdapter extends RecyclerView.Adapter<SendAdapter.LineHolder> {
    private List<SendHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public SendAdapter(List<SendHistory> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_send, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        SendHistory send = mData.get(position);
        holder.mCarSequence.setText(String.valueOf(send.sortNum));
        holder.mCarCode.setText(send.vehCode);
        holder.mDriver.setText(send.sjName);
        holder.mTrainman.setText(send.scName);
        holder.mPlanTime.setText(DisplayTimeUtil.substring(send.projectTime));
        holder.mIntervalTime.setText(String.valueOf(send.spaceMin));
        holder.mSystemEnterTime.setText(DisplayTimeUtil.substring(send.inTime1));
        holder.mEnterTime.setText(DisplayTimeUtil.substring(send.inTime2));
        holder.mSendTime.setText(DisplayTimeUtil.substring(send.realTime));
        holder.mSendState.setText(send.isScan == 1 ? "自动" : "人工");
        holder.mState.setText(send.isScan == 1 ? "已读" : "未读");
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
        @Bind(R.id.tv_send_time)
        TextView mSendTime;
        @Bind(R.id.tv_send_state)
        TextView mSendState;
        @Bind(R.id.tv_state)
        TextView mState;

        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
