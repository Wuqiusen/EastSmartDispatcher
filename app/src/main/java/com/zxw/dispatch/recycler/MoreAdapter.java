package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxw.data.bean.MoreHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DisplayTimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.LineHolder> {
    private List<MoreHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public MoreAdapter(List<MoreHistory> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_more, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        MoreHistory more = mData.get(position);
        holder.mCarSequence.setText(String.valueOf(more.sortNum));
        holder.mCarCode.setText(more.vehCode);
        holder.mDriver.setText(more.sjName);
        holder.mTrainman.setText(more.scName);
        holder.mPlanTime.setText(DisplayTimeUtil.substring(more.projectTime));
        holder.mIntervalTime.setText(String.valueOf(more.spaceMin));
        holder.mSystemEnterTime.setText(DisplayTimeUtil.substring(more.inTime1));
        holder.mEnterTime.setText(DisplayTimeUtil.substring(more.inTime2));
        holder.mMoreTime.setText("");
        holder.mMoreType.setText("");
        holder.mMoreDetail.setText("");
        holder.mState.setText(more.isScan == 1 ? "已读" : "未读");
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
        @Bind(R.id.tv_more_time)
        TextView mMoreTime;
        @Bind(R.id.tv_more_type)
        TextView mMoreType;
        @Bind(R.id.tv_more_detail)
        TextView mMoreDetail;
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
