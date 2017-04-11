package com.zxw.dispatch.recycler;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.SendHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.utils.DisplayTimeUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class GoneAdapterForNotOperatorEmpty extends RecyclerView.Adapter<GoneAdapterForNotOperatorEmpty.LineHolder> {
    private final LineParams mLineParams;
    private List<SendHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private MainPresenter presenter;

    public GoneAdapterForNotOperatorEmpty(List<SendHistory> mData, Context mContext, LineParams mLineParams, MainPresenter presenter) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mLineParams = mLineParams;
        this.presenter = presenter;
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_start_car_operate_empty_driving, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final LineHolder holder, final int position) {
        SendHistory history = mData.get(position);
        holder.tvCarSequence.setText(String.valueOf(position + 1));
        holder.tvCarCode.setText(history.code);
        holder.tv_mission_name.setText(history.typeName);
        holder.tv_station_name.setText(history.electronRailName);
        holder.tv_count.setText(String.valueOf(history.runNum));
        holder.tv_empty_km.setText(history.runEmpMileage);
        if (history.taskEditBelongId == history.taskEditRunId) {
            holder.tvCarCode.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
        } else {
            holder.tvCarCode.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
        }
        holder.tvDriver.setText(history.driverName);
        if (history.stewardName != null && !TextUtils.isEmpty(history.stewardName))
            holder.tvTrainman.setText(history.stewardName);
        else
            holder.tvTrainman.setText("");

//      holder.tvStopTime.setText(String.valueOf(history.vehTime));
        holder.tvPlanTime.setText(DisplayTimeUtil.substring(history.vehTime));
        holder.tv_end_time.setText(DisplayTimeUtil.substring(history.taskEndTime));
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


//      holder.tvScheduleStatus.setText(history.isDouble == 0 ? "双班":"单班");
//      holder.tvStationStatus.setText(String.valueOf(history.vehTime));
        holder.tv_send_remark.setText(history.remarks);
        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO) {
            holder.tvTrainman.setVisibility(View.GONE);
        } else if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_MANUAL) {
            holder.tvTrainman.setVisibility(View.VISIBLE);
        }
        // 备注
        holder.tv_send_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    openRemarkDialog(mData.get(position).id, mData.get(position).status, mData.get(position).remarks);
                }catch (Exception e){

                }
            }
        });
        // 查看
//      holder.tvCheckSendCar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                   openCarMsgDialog();
//            }
//      });
        // 撤回
        // 如果已有实际发车时间, 则因此撤回按钮
        if (TextUtils.isEmpty(mData.get(position).vehTimeReal)) {
            holder.tv_send_withdraw.setVisibility(View.VISIBLE);
        } else {
            holder.tv_send_withdraw.setVisibility(View.GONE);
        }
        holder.tv_send_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWithdrawCarDialog(mData.get(position).id);
            }
        });
        try {
            if (mData.get(position).isMakeup == 2
                    ) {
                holder.ll_container.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_blue));
            } else {
                holder.ll_container.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
            }
        } catch (Exception e) {

        }

        try{
            holder.tvStatus.setText(history.status == 1 ? "正常":"异常");
        }catch (Exception e){
            holder.tvStatus.setText("");
        }
    }

    private String setStopCarMinute(SendHistory history) {
        Long arriveMinute;
        Long sendMinute = null;
        if (history.arriveTime != null && !TextUtils.isEmpty(history.arriveTime)) {
            arriveMinute = Long.valueOf(history.arriveTime.substring(0, 2)) * 60
                    + Long.valueOf(history.arriveTime.substring(2, 4));
        } else {
            return "";
        }
        if (history.vehTimeReal != null && !TextUtils.isEmpty(history.vehTimeReal)) {
            sendMinute = Long.valueOf(history.vehTimeReal.substring(0, 2)) * 60
                    + Long.valueOf(history.vehTimeReal.substring(2, 4));
        } else {
            return "";
        }
        Long min = (Long) sendMinute - arriveMinute;
        if (min >= 0) {
            return String.valueOf(min);
        } else {
            return "";
        }

    }


    /**
     * 查看
     */
    private void openCarMsgDialog() {
        final Dialog mDialog = new Dialog(mContext, R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext, R.layout.view_check_car_details_dialog, null);
        Button btn_close = (Button) view.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(view, params);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * 撤回
     */
    private void openWithdrawCarDialog(final int objId) {
        final Dialog mDialog = new Dialog(mContext, R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext, R.layout.view_withdraw_dialog, null);
        TextView tv_prompt = (TextView) view.findViewById(R.id.tv_prompt);
        tv_prompt.setText("您确定把车辆撤回到待发车辆列表？");
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.callBackGoneCar(objId);
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(view, params);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * 备注
     */
    private void openRemarkDialog(final int objId, final int status, String remark) {
        final Dialog rDialog = new Dialog(mContext, R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext, R.layout.view_start_car_remarks_dialog, null);
        final RadioButton rbNormal = (RadioButton) view.findViewById(R.id.rb_normal);
        final RadioButton rbAbnormal = (RadioButton) view.findViewById(R.id.rb_abnormal);
        final EditText etRemarks = (EditText) view.findViewById(R.id.et_remarks);
        if (remark != null && !TextUtils.isEmpty(remark)) {
            etRemarks.setText(remark);
            etRemarks.setSelection(remark.length());
        }
        if (status == 1) {
            rbNormal.setChecked(true);
        } else {
            rbAbnormal.setChecked(true);
        }
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mStatus = 1;
                if (rbAbnormal.isChecked()) mStatus = 2;
                if (rbNormal.isChecked()) mStatus = 1;
                presenter.goneCarRemarks(objId, mStatus, etRemarks.getText().toString());
                rDialog.dismiss();
            }
        });
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rDialog.dismiss();
            }
        });
        rDialog.setContentView(view, params);
        rDialog.setCancelable(true);
        rDialog.show();
    }


    public int getCount() {
        return mData.size();
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class LineHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_container)
        LinearLayout ll_container;
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
        @Bind(R.id.tv_send_time)
        TextView tvSendTime;
        @Bind(R.id.tv_driver)
        TextView tvDriver;
        @Bind(R.id.tv_trainman)
        TextView tvTrainman;
        //        @Bind(R.id.tv_schedule_status)
//        TextView tvScheduleStatus;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        //        @Bind(R.id.tv_no_work_status)
//        TextView tvNoWorkStatus;
        @Bind(R.id.tv_check_send_car)
        TextView tvCheckSendCar;
        @Bind(R.id.tv_send_remark)
        TextView tv_send_remark;
        @Bind(R.id.tv_send_withdraw)
        TextView tv_send_withdraw;
        @Bind(R.id.tv_mission_name)
        TextView tv_mission_name;
        @Bind(R.id.tv_station_name)
        TextView tv_station_name;
        @Bind(R.id.tv_count)
        TextView tv_count;
        @Bind(R.id.tv_empty_km)
        TextView tv_empty_km;
        @Bind(R.id.tv_end_time)
        TextView tv_end_time;

        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
