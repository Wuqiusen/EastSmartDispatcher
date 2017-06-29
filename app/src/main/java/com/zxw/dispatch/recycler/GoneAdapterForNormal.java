package com.zxw.dispatch.recycler;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.SendHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.BasePresenter;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.utils.DisplayTimeUtil;
import com.zxw.dispatch.view.dialog.StartCarRemarkDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class GoneAdapterForNormal extends RecyclerView.Adapter<GoneAdapterForNormal.LineHolder> {
    private final LineParams mLineParams;
    private List<SendHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private MainPresenter presenter;

    public GoneAdapterForNormal(List<SendHistory> mData, Context mContext, LineParams mLineParams, MainPresenter presenter) {
        this.mData = mData;
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mLineParams = mLineParams;
        this.presenter = presenter;
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_start_car_line_operate, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final LineHolder holder, final int position) {
        SendHistory history = mData.get(position);
        holder.tvCarSequence.setText(String.valueOf(mData.size() - (position)));
        holder.tvCarCode.setText(history.code);
        if (history.taskEditBelongId == history.taskEditRunId){
            holder.tvCarCode.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
        }else {
            holder.tvCarCode.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
        }
        holder.tvDriver.setText(history.driverName);
        if (history.stewardName != null && !TextUtils.isEmpty(history.stewardName))
            holder.tvTrainman.setText(history.stewardName);
        else
            holder.tvTrainman.setText("");

//      holder.tvStopTime.setText(String.valueOf(history.vehTime));
        holder.tvIntervalTime.setText(String.valueOf(history.spaceTime));
        holder.tvPlanTime.setText(DisplayTimeUtil.substring(history.vehTime));
        // 到站时刻
        if (history.arriveTime != null && !TextUtils.isEmpty(history.arriveTime))
        holder.tvArriveTime.setText(DisplayTimeUtil.substring(history.arriveTime));
        else
            holder.tvArriveTime.setText("");
        // 实际发车时刻
        if (history.vehTimeReal != null && !TextUtils.isEmpty(history.vehTimeReal))
            holder.tvSendTime.setText(DisplayTimeUtil.substring(history.vehTimeReal));
        else
            holder.tvSendTime.setText("");
        // 停场时间
        holder.tv_stop_car_minute.setText(setStopCarMinute(history));


//      holder.tvScheduleStatus.setText(history.isDouble == 0 ? "双班":"单班");
//      holder.tvStationStatus.setText(String.valueOf(history.vehTime));
        holder.tvWorkStatus.setText(history.typeName);
        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO){
            holder.tvTrainman.setVisibility(View.GONE);
        }else if(mLineParams.getSaleType() == MainPresenter.TYPE_SALE_MANUAL){
            holder.tvTrainman.setVisibility(View.VISIBLE);
        }
        //1正常、2异常
        if (mData.get(position).status == 1){
            holder.tv_send_remark.setTextColor(mContext.getResources().getColor(R.color.font_blue2));
        }else{
            holder.tv_send_remark.setTextColor(mContext.getResources().getColor(R.color.font_gray));
        }
        // 备注
        holder.tv_send_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StartCarRemarkDialog(mContext, mData.get(position).id, mData.get(position).status, mData.get(position).remarks,
                         new StartCarRemarkDialog.OnStartCarRemarkListener() {
                             @Override
                             public void goneCarNormalRemarks(int objId, int status, String remarks, BasePresenter.LoadDataStatus loadDataStatus) {
                                 presenter.goneCarNormalRemarks(objId, status, remarks, loadDataStatus);
                             }

                             @Override
                             public void goneCarAbNormalRemarks(int objId, int status, String remarks,
                                                                int runOnce, double runMileage, double runEmpMileage, BasePresenter.LoadDataStatus loadDataStatus) {
                                 presenter.goneCarAbNormalRemarks(objId,status,remarks,runOnce,runMileage,runEmpMileage, loadDataStatus);
                             }
                });
            }
        });

        String driverStatus = "";
        if (mData.get(position).opStatus !=null){
            switch (mData.get(position).opStatus){
                case 1:
                    driverStatus = "待开始";
                    break;
                case 2:
                    driverStatus = "进行中";
                    break;
                case 3:
                    driverStatus = "异常终止";
                    break;
                case 4:
                    driverStatus = "正常结束";
                    break;
            }
        }
        holder.tv_driver_ok.setText(driverStatus);

        // 撤回
        // 如果已有实际发车时间, 则因此撤回按钮
        if(TextUtils.isEmpty(mData.get(position).vehTimeReal)){
            holder.tv_send_withdraw.setVisibility(View.VISIBLE);
        }else{
            holder.tv_send_withdraw.setVisibility(View.GONE);
        }
        holder.tv_send_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 openWithdrawCarDialog(mData.get(position).id);
            }
        });


    }

    private String setStopCarMinute(SendHistory history){
        Long arriveMinute;
        Long sendMinute = null;
        if (history.arriveTime != null && !TextUtils.isEmpty(history.arriveTime)){
            arriveMinute = Long.valueOf(history.arriveTime.substring(0,2)) * 60
                    + Long.valueOf(history.arriveTime.substring(2,4));
        }else{
            return "";
        }
        if (history.vehTimeReal != null && !TextUtils.isEmpty(history.vehTimeReal)){
            sendMinute = Long.valueOf(history.vehTimeReal.substring(0,2)) * 60
                    + Long.valueOf(history.vehTimeReal.substring(2,4));
        }else{
            return "";
        }
        Long min = (Long)sendMinute - arriveMinute;
        if (min >= 0) {
            return String.valueOf(min);
        }else{
            return "";
        }

    }


    /**
     * 查看
     */
    private void openCarMsgDialog() {
        final Dialog mDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.view_check_car_details_dialog,null);
        Button btn_close = (Button) view.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
        mDialog.setContentView(view,params);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * 撤回
     */
    private void openWithdrawCarDialog(final int objId) {
        final Dialog mDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.view_withdraw_dialog,null);
        TextView tv_prompt = (TextView) view.findViewById(R.id.tv_prompt);
        tv_prompt.setText("您确定把车辆撤回到待发车辆列表？");
        final Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirm.setClickable(false);
                presenter.callBackGoneCar(objId, new BasePresenter.LoadDataStatus() {
                    @Override
                    public void OnLoadDataFinish() {
                        mDialog.dismiss();
                    }
                });

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(view,params);
        mDialog.setCancelable(true);
        mDialog.show();
    }



    public int getCount() {
        return mData.size();
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
//        @Bind(R.id.tv_stop_time)
//        TextView tvStopTime;
        @Bind(R.id.tv_interval_time)
        TextView tvIntervalTime;
        @Bind(R.id.tv_send_time)
        TextView tvSendTime;
        @Bind(R.id.tv_driver)
        TextView tvDriver;
        @Bind(R.id.tv_trainman)
        TextView tvTrainman;
//        @Bind(R.id.tv_schedule_status)
//        TextView tvScheduleStatus;
        @Bind(R.id.tv_work_status)
        TextView tvWorkStatus;
        @Bind(R.id.tv_driver_ok)
        TextView tv_driver_ok;
        @Bind(R.id.tv_send_remark)
        TextView tv_send_remark;
        @Bind(R.id.tv_send_withdraw)
        TextView tv_send_withdraw;
        @Bind(R.id.tv_stop_car_minute)
        TextView tv_stop_car_minute;
        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
