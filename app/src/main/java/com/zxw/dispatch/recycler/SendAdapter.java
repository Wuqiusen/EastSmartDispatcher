package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
        SendHistory history = mData.get(position);
        holder.tvCarSequence.setText(String.valueOf(position + 1));
        holder.tvCarCode.setText(history.code);
        holder.tvDriver.setText(history.driverName);
        if (history.stewardName != null && !TextUtils.isEmpty(history.stewardName))
            holder.tvTrainman.setText(history.stewardName);
        holder.tvPlanTime.setText(DisplayTimeUtil.substring(history.vehTime));
        if (history.arriveTime != null && !TextUtils.isEmpty(history.arriveTime))
        holder.tvArriveTime.setText(DisplayTimeUtil.substring(history.arriveTime));
//        holder.tvStopTime.setText(String.valueOf(history.vehTime));
        holder.tvIntervalTime.setText(String.valueOf(history.spaceTime));
//        holder.tvSendTime.setText(String.valueOf(history.vehTime));
        holder.tvScheduleStatus.setText(history.isDouble == 0 ? "双班":"单班");
//        holder.tvStationStatus.setText(String.valueOf(history.vehTime));
        holder.tvWorkStatus.setText(history.type == 1? "正线运营": "");
        if (history.unRunTaskStatus == 1){
            holder.tvNoWorkStatus.setText("无");
        }else if (history.unRunTaskStatus == 2){
            holder.tvNoWorkStatus.setText("有");
        }else {
            holder.tvNoWorkStatus.setText("完成");
        }
        holder.tvCheckSendCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class LineHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_car_sequence)
        TextView tvCarSequence;
        @Bind(R.id.tv_car_code)
        TextView tvCarCode;
        @Bind(R.id.tv_plan_time)
        TextView tvPlanTime;
        @Bind(R.id.tv_arrive_time)
        TextView tvArriveTime;
        @Bind(R.id.tv_stop_time)
        TextView tvStopTime;
        @Bind(R.id.tv_interval_time)
        TextView tvIntervalTime;
        @Bind(R.id.tv_send_time)
        TextView tvSendTime;
        @Bind(R.id.tv_driver)
        TextView tvDriver;
        @Bind(R.id.tv_trainman)
        TextView tvTrainman;
        @Bind(R.id.tv_schedule_status)
        TextView tvScheduleStatus;
        @Bind(R.id.tv_station_status)
        TextView tvStationStatus;
        @Bind(R.id.tv_work_status)
        TextView tvWorkStatus;
        @Bind(R.id.tv_no_work_status)
        TextView tvNoWorkStatus;
        @Bind(R.id.tv_check_send_car)
        TextView tvCheckSendCar;
        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
