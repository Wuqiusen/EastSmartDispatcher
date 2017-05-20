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
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zxw.data.bean.DriverWorkloadItem;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.ToastHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by moxiaoqing on 2017/5/19.
 */

public class WorkLoadVerifyAdapter extends RecyclerView.Adapter<WorkLoadVerifyAdapter.WorkLoadVerifyViewHolder>{
    private Context mContext;
    private List<DriverWorkloadItem> mData;
    private OnWorkLoadItemClickListener mListener;
    private final LayoutInflater mLayoutInflater;

    private int mDriverOpTag;
    private int mGpsTag;

    public WorkLoadVerifyAdapter(Context context, List<DriverWorkloadItem> data,OnWorkLoadItemClickListener listener){
         this.mContext = context;
         this.mData = data;
         this.mListener = listener;
         mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public WorkLoadVerifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_work_load_verify,parent,false);
        return new WorkLoadVerifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkLoadVerifyViewHolder holder, final int position) {
        int tag = mData.get(position).getOpStatus();
        if (tag == 2){
            holder.tvDriverOk.setText("待确认");
        }else if(tag == 3){
            holder.tvDriverOk.setText("不同意");
        }else if(tag == 4){
            holder.tvDriverOk.setText("同意");
        }
        holder.tvNo.setText(String.valueOf(position + 1));
        holder.tvVehId.setText(mData.get(position).getVehCode());
        holder.tvDriverName.setText(mData.get(position).getDriverName());
        holder.tvVehTime.setText(mData.get(position).getVehTime());
        // 后期:正常_打勾、异常_打叉；是否需呀设置监听
        holder.tvOutTime.setText(mData.get(position).getOutTime());
        holder.tvArriveTime.setText(mData.get(position).getArrivalTime());
        String gps = "";
        switch (mData.get(position).getGpsStatus()){
            case 1:
                gps = "异常";
                break;
            case 2:
                gps = "正常";
                break;
        }
        holder.tvGps.setText(gps);
        String driverStatus = "";
        switch (mData.get(position).getOpStatus()){
            case 2:
                driverStatus = "待确认";
                break;
            case 3:
                driverStatus = "不同意";
                break;
            case 4:
                driverStatus = "同意";
                break;
        }
        holder.tvDriverOk.setText(driverStatus);
        holder.tvDriverOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDriverOkDialog(position, mData.get(position).getOpStatus());
            }
        });
        holder.tvGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGpsDialog(position, mData.get(position).getGpsStatus());
            }
        });
        holder.tvOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOutTimeDialog(mData.get(position).getObjId(), mData.get(position).getOutTime());
            }
        });
        holder.tvArriveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showArriveTimeDialog(mData.get(position).getObjId(), mData.get(position).getArrivalTime());
            }
        });
    }

    private void showOutTimeDialog(final long objId, String outTime) {
        showTimeDialog(objId, outTime, 1);
    }
    private void showArriveTimeDialog(final long objId, String ArrivalTime) {
        showTimeDialog(objId, ArrivalTime, 1);
    }


    private void showTimeDialog(final long objId, String time,final int type) {
        final Dialog dialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.dialog_work_load_time,null);
        final EditText et_time = (EditText) view.findViewById(R.id.et_time);
        et_time.setText(time);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
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
                if (type == 1){
                    mListener.onAlertOutTime(objId, time);
                }else{
                    mListener.onAlertArriveTime(objId, time);
                }
//                dialog.dismiss();
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

    private void showGpsDialog(final int position,int tag){
        final Dialog dialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.dialog_work_load_gps,null);
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        final RadioButton rb_ok = (RadioButton) view.findViewById(R.id.rb_ok);
        final RadioButton rb_error = (RadioButton) view.findViewById(R.id.rb_error);
        if (tag == 1){
            rb_ok.setChecked(false);
            rb_error.setChecked(true);
        }else if(tag == 2){
            rb_ok.setChecked(true);
            rb_error.setChecked(false);
        }
        mGpsTag = tag;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId){
                switch (checkedId){
                    case R.id.rb_error:
                        mGpsTag = 1;
                        rb_ok.setChecked(false);
                        rb_error.setChecked(true);
                        break;
                    case R.id.rb_ok:
                        mGpsTag = 2;
                        rb_ok.setChecked(true);
                        rb_error.setChecked(false);
                        break;
                }
            }
        });
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DebugLog.w("gps" + mGpsTag);
                mListener.onAlertGpsStatus(mData.get(position).getObjId(), mGpsTag);
//                dialog.dismiss();
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

    private void showDriverOkDialog(final int position, int tag){
        final Dialog dialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.dialog_work_load_driver_ok,null);
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        final RadioButton rb_wait_ok = (RadioButton) view.findViewById(R.id.rb_wait_ok);
        final RadioButton rb_unagree = (RadioButton) view.findViewById(R.id.rb_unagree);
        final RadioButton rb_agree = (RadioButton) view.findViewById(R.id.rb_agree);
        if (tag == 2){
            setRbsIsChecked(rb_wait_ok,rb_unagree,rb_agree);
        }else if(tag == 3){
            setRbsIsChecked(rb_unagree,rb_wait_ok,rb_agree);
        }else if (tag == 4){
            setRbsIsChecked(rb_agree,rb_wait_ok,rb_unagree);
        }
        mDriverOpTag = tag;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
             @Override
             public void onCheckedChanged(RadioGroup group,int checkedId){
                  switch (checkedId){
                      case R.id.rb_wait_ok:
                          mDriverOpTag = 2;
                          setRbsIsChecked(rb_wait_ok,rb_unagree,rb_agree);
                          break;
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
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //司机确认
                mListener.onAlertDriverStatus(mData.get(position).getObjId(), mDriverOpTag);
//                dialog.dismiss();
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

    private void setRbsIsChecked(RadioButton rb0,RadioButton rb1,RadioButton rb2){
           rb0.setChecked(true);
           rb1.setChecked(false);
           rb2.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class WorkLoadVerifyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_no)
        TextView tvNo;
        @Bind(R.id.tv_vehId)
        TextView tvVehId;
        @Bind(R.id.tv_driver_name)
        TextView tvDriverName;
        @Bind(R.id.tv_veh_time)
        TextView tvVehTime;
        @Bind(R.id.tv_out_time)
        TextView tvOutTime;
        @Bind(R.id.tv_arrive_time)
        TextView tvArriveTime;
        @Bind(R.id.tv_gps)
        TextView tvGps;
        @Bind(R.id.tv_driver_ok)
        TextView tvDriverOk;
        public WorkLoadVerifyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnWorkLoadItemClickListener {
        void onAlertOutTime(long objId, String str);
        void onAlertArriveTime(long objId, String str);
        void onAlertGpsStatus(long objId, int str);
        void onAlertDriverStatus(long objId, int str);
    }
}
