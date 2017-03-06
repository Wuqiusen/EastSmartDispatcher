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
 *
 */
public class GoneAdapter extends RecyclerView.Adapter<GoneAdapter.LineHolder> {
    private final LineParams mLineParams;
    private List<SendHistory> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private MainPresenter presenter;

    public GoneAdapter(List<SendHistory> mData, Context mContext, LineParams mLineParams, MainPresenter presenter) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mLineParams = mLineParams;
        this.presenter = presenter;
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
        else
            holder.tvTrainman.setText("");
        holder.tvPlanTime.setText(DisplayTimeUtil.substring(history.vehTime));
        if (history.arriveTime != null && !TextUtils.isEmpty(history.arriveTime))
        holder.tvArriveTime.setText(DisplayTimeUtil.substring(history.arriveTime));
        else
            holder.tvArriveTime.setText("");
//        holder.tvStopTime.setText(String.valueOf(history.vehTime));
        holder.tvIntervalTime.setText(String.valueOf(history.spaceTime));
        holder.tvSendTime.setText(DisplayTimeUtil.substring(history.vehTimeReal));
//        holder.tvScheduleStatus.setText(history.isDouble == 0 ? "双班":"单班"); ///
        holder.tvStatus.setText(history.status == 1 ? "正常":"异常"); ///
//        holder.tvStationStatus.setText(String.valueOf(history.vehTime));
        holder.tv_send_remark.setText(history.remarks);
        holder.tvWorkStatus.setText(history.typeName);

        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO){
            holder.tvTrainman.setVisibility(View.GONE);
        }else if(mLineParams.getSaleType() == MainPresenter.TYPE_SALE_MANUAL){
            holder.tvTrainman.setVisibility(View.VISIBLE);
        }
        // 备注
        holder.tv_send_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRemarkDialog(mData.get(position).id, mData.get(position).status, mData.get(position).remarks);
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
        holder.tv_send_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 openWithdrawCarDialog(mData.get(position).id);
            }
        });
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
        mDialog.setContentView(view,params);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * 备注
     */
    private void openRemarkDialog(final int objId, final int status, String remark) {
        final Dialog rDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.view_start_car_remarks_dialog,null);
        final RadioButton rbNormal = (RadioButton) view.findViewById(R.id.rb_normal);
        final RadioButton rbAbnormal = (RadioButton) view.findViewById(R.id.rb_abnormal);
        final EditText etRemarks = (EditText) view.findViewById(R.id.et_remarks);
        if (remark != null && !TextUtils.isEmpty(remark)){
            etRemarks.setText(remark);
            etRemarks.setSelection(remark.length());
        }
        if (status == 1){
            rbNormal.setChecked(true);
        }else {
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
        rDialog.setContentView(view,params);
        rDialog.setCancelable(true);
        rDialog.show();
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
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.tv_work_status)
        TextView tvWorkStatus;
//        @Bind(R.id.tv_no_work_status)
//        TextView tvNoWorkStatus;
        @Bind(R.id.tv_check_send_car)
        TextView tvCheckSendCar;
        @Bind(R.id.tv_send_remark)
        TextView tv_send_remark;
        @Bind(R.id.tv_send_withdraw)
        TextView tv_send_withdraw;
        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
