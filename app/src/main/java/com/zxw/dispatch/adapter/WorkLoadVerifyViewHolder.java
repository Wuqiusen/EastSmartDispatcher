package com.zxw.dispatch.adapter;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zxw.data.bean.DriverWorkloadItem;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.BasePresenter;
import com.zxw.dispatch.utils.ToastHelper;


/**
 * Created by wuqiusen on 2017/6/16.
 */

public class WorkLoadVerifyViewHolder extends BaseViewHolder<DriverWorkloadItem> {
    TextView tvNo;
    TextView tvVehId;
    TextView tvDriverName;
    TextView tvVehTime;
    TextView tvOutTime;
    TextView tvArriveTime;
    TextView tvGps;
    TextView tvDriverOk;
    TextView tv_delete;
    TextView tv_remarks;
    TextView tv_work_type;
    TextView tv_work_content;

    private OnWorkLoadItemClickListener mListener;
    private final static int DIALOG_TYPE_OUT_TIME = 1, DIALOG_TYPE_ARRIVE_TIME = 2;
    private int mDriverOpTag;
    private int mGpsTag;
    private BasePresenter.LoadDataStatus loadDataStatus;
    
    public WorkLoadVerifyViewHolder(ViewGroup parent, OnWorkLoadItemClickListener listener) {
        super(parent, R.layout.item_work_load_verify);
        tvNo = $(R.id.tv_no);
        tvVehId = $(R.id.tv_vehId);
        tvDriverName = $(R.id.tv_driver_name);
        tvVehTime = $(R.id.tv_veh_time);
        tvOutTime = $(R.id.tv_out_time);
        tvArriveTime = $(R.id.tv_arrive_time);
        tvGps = $(R.id.tv_gps);
        tvDriverOk = $(R.id.tv_driver_ok);
        tv_delete = $(R.id.tv_delete);
        tv_remarks = $(R.id.tv_remarks);
        tv_work_type = $(R.id.tv_work_type);
        tv_work_content = $(R.id.tv_work_content);
        this.mListener = listener;
    }

    @Override
    public void setData(final DriverWorkloadItem data) {
        super.setData(data);
        tvNo.setText(String.valueOf(getDataPosition() + 1));
        tvVehId.setText(data.getVehCode());
        tvDriverName.setText(data.getDriverName());
        tvVehTime.setText(data.getVehTime());
        // 后期:正常_打勾、异常_打叉；是否需呀设置监听
        tvOutTime.setText(data.getOutTime());
        tvArriveTime.setText(data.getArrivalTime());
        String gps = "";
        switch (data.getGpsStatus()){
            case 0:
                gps = "异常";
                break;
            case 1:
                gps = "正常";
                break;
        }
        tvGps.setText(gps);
        String driverStatus = "";
        switch (data.getOpStatus()){
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
        tv_work_type.setText(data.getTypeName());//任务类型
        tv_work_content.setText(data.getContent());//任务
        tvDriverOk.setText(driverStatus);
        tvDriverOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDriverOkDialog(data);
            }
        });
        tvGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGpsDialog(data);
            }
        });
        tvOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOutTimeDialog(data.getObjId(), data.getOutTime());
            }
        });
        tvArriveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showArriveTimeDialog(data.getObjId(), data.getArrivalTime());
            }
        });
        if (data.getOpStatus() != 4 || data.getGpsStatus() != 1 || TextUtils.isEmpty(data.getOutTime()) || TextUtils.isEmpty(data.getArrivalTime())){
            tv_delete.setTextColor(getContext().getResources().getColor(R.color.font_blue2));
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(data);
                }
            });
        }else{
            tv_delete.setTextColor(getContext().getResources().getColor(R.color.font_gray));
            tv_delete.setOnClickListener(null);
        }
        //备注
        final String remarks = data.getRemarks();
        tv_remarks.setText(remarks);
        if(TextUtils.isEmpty(remarks)){
            tv_remarks.setOnClickListener(null);
        }else{
            tv_remarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRemarksDialog(remarks);
                }
            });
        }
    }

    private void showRemarksDialog(String remarks) {
        final Dialog dialog = new Dialog(getContext(),R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(),R.layout.dialog_work_load_remark,null);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        TextView tv_remarks  = (TextView) view.findViewById(R.id.tv_remarks);
        tv_remarks.setText(remarks);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view,params);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showDeleteDialog(final DriverWorkloadItem data) {
        final Dialog dialog = new Dialog(getContext(),R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(),R.layout.dialog_work_load_delete,null);
        final Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DebugLog.w("gps" + mGpsTag);
                btn_confirm.setClickable(false);
                loadDataStatus = new BasePresenter.LoadDataStatus() {
                    @Override
                    public void OnLoadDataFinish() {
                        btn_confirm.setClickable(true);
                    }
                };
                mListener.onDelete(data.getObjId(), loadDataStatus);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view,params);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showOutTimeDialog(final long objId, String outTime) {
        showTimeDialog(objId, outTime, DIALOG_TYPE_OUT_TIME);
    }
    private void showArriveTimeDialog(final long objId, String ArrivalTime) {
        showTimeDialog(objId, ArrivalTime, DIALOG_TYPE_ARRIVE_TIME);
    }


    private void showTimeDialog(final long objId, String time,final int type) {
        final Dialog dialog = new Dialog(getContext(),R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(),R.layout.dialog_work_load_time,null);
        final EditText et_time = (EditText) view.findViewById(R.id.et_time);
        et_time.setText(time);
        final Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DebugLog.w("gps" + mGpsTag);
                String time = et_time.getText().toString().trim();
                if (TextUtils.isEmpty(time) || time.length() != 4){
                    ToastHelper.showToast("请输入正确时间");
                    return;
                }
                loadDataStatus = new BasePresenter.LoadDataStatus() {
                    @Override
                    public void OnLoadDataFinish() {
                        btn_confirm.setClickable(true);
                    }
                };
                btn_confirm.setClickable(false);
                if (type == DIALOG_TYPE_OUT_TIME){
                    mListener.onAlertOutTime(objId, time, loadDataStatus);
                }else{
                    mListener.onAlertArriveTime(objId, time, loadDataStatus);
                }
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view,params);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showGpsDialog(final DriverWorkloadItem data){
        final Dialog dialog = new Dialog(getContext(),R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(),R.layout.dialog_work_load_gps,null);
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        final RadioButton rb_ok = (RadioButton) view.findViewById(R.id.rb_ok);
        final RadioButton rb_error = (RadioButton) view.findViewById(R.id.rb_error);
        if (data.getGpsStatus() == 0){
            rb_ok.setChecked(false);
            rb_error.setChecked(true);
        }else if(data.getGpsStatus() == 1){
            rb_ok.setChecked(true);
            rb_error.setChecked(false);
        }
        mGpsTag = data.getGpsStatus();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId){
                switch (checkedId){
                    case R.id.rb_error:
                        mGpsTag = 0;
                        rb_ok.setChecked(false);
                        rb_error.setChecked(true);
                        break;
                    case R.id.rb_ok:
                        mGpsTag = 1;
                        rb_ok.setChecked(true);
                        rb_error.setChecked(false);
                        break;
                }
            }
        });
        final Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        final Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirm.setClickable(false);
                loadDataStatus = new BasePresenter.LoadDataStatus() {
                    @Override
                    public void OnLoadDataFinish() {
                        btn_confirm.setClickable(true);
                    }
                };
//                DebugLog.w("gps" + mGpsTag);
                mListener.onAlertGpsStatus(data.getObjId(), mGpsTag, loadDataStatus);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view,params);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void showDriverOkDialog(final DriverWorkloadItem data){
        final Dialog dialog = new Dialog(getContext(),R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(),R.layout.dialog_work_load_driver_ok,null);
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        final RadioButton rb_wait_ok = (RadioButton) view.findViewById(R.id.rb_wait_ok);
        final RadioButton rb_unagree = (RadioButton) view.findViewById(R.id.rb_unagree);
        final RadioButton rb_agree = (RadioButton) view.findViewById(R.id.rb_agree);
        if (data.getOpStatus() == 2){
            setRbsIsChecked(rb_wait_ok,rb_unagree,rb_agree);
        }else if(data.getOpStatus() == 3){
            setRbsIsChecked(rb_unagree,rb_wait_ok,rb_agree);
        }else if (data.getOpStatus() == 4){
            setRbsIsChecked(rb_agree,rb_wait_ok,rb_unagree);
        }
        mDriverOpTag = data.getOpStatus();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId){
                switch (checkedId){
                    case R.id.rb_unagree:
                        mDriverOpTag = 3;
                        setRbsIsChecked(rb_unagree,rb_wait_ok,rb_agree);
                        break;
                    case R.id.rb_agree:
                        mDriverOpTag = 4;
                        setRbsIsChecked(rb_agree,rb_wait_ok,rb_unagree);
                        break;
                }
            }
        });
        final Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirm.setClickable(false);
                loadDataStatus = new BasePresenter.LoadDataStatus() {
                    @Override
                    public void OnLoadDataFinish() {
                        btn_confirm.setClickable(true);
                    }
                };
                //司机确认
                mListener.onAlertDriverStatus(data.getObjId(), mDriverOpTag, loadDataStatus);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view,params);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void setRbsIsChecked(RadioButton rb0, RadioButton rb1, RadioButton rb2){
        rb0.setChecked(true);
        rb1.setChecked(false);
        rb2.setChecked(false);
    }

    public interface OnWorkLoadItemClickListener {
        void onAlertOutTime(long objId, String str, BasePresenter.LoadDataStatus loadDataStatus);
        void onAlertArriveTime(long objId, String str, BasePresenter.LoadDataStatus loadDataStatus);
        void onAlertGpsStatus(long objId, int str, BasePresenter.LoadDataStatus loadDataStatus);
        void onAlertDriverStatus(long objId, int str, BasePresenter.LoadDataStatus loadDataStatus);
        void onDelete(long objId, BasePresenter.LoadDataStatus loadDataStatus);
    }
}
