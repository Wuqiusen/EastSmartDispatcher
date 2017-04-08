package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.ToastHelper;


/**
 * author MXQ
 * create at 2017/4/7 14:39
 * email: 1299242483@qq.com
 */
public class AddRecordingLineOpreateDialog extends AlertDialog.Builder {

    private Context mContext;
    private OnAddRecordingLineOperateListener mListener;
    private EditText et_vehicleId;
    private EditText et_taskLineId;
    private EditText et_driverId;
    private EditText et_arrive_time;
    private EditText et_plan_time;
    private EditText et_stop_time;
    private EditText et_task_type;
    private Button btn_save;
    private Button btn_cancel;
    private Button btn_close;
    private AlertDialog dialog;

    public AddRecordingLineOpreateDialog(Context context, OnAddRecordingLineOperateListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mListener = listener;
        init(mContext);
    }

    private void init(Context context) {
        View view = View.inflate(mContext,R.layout.dialog_add_recording_line_opreate,null);
        et_vehicleId = (EditText) view.findViewById(R.id.et_vehicleId);
        et_taskLineId = (EditText) view.findViewById(R.id.et_taskLineId);
        et_driverId = (EditText) view.findViewById(R.id.et_driverId);
        et_arrive_time = (EditText) view.findViewById(R.id.et_arrive_time);
        et_plan_time = (EditText) view.findViewById(R.id.et_plan_time);
        et_stop_time = (EditText) view.findViewById(R.id.et_stop_time);
        et_task_type = (EditText) view.findViewById(R.id.et_task_type);

        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_close = (Button) view.findViewById(R.id.btn_close);

        shouCloseBtnStyle();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog!=null && dialog.isShowing()){
                   String vehicleId = et_vehicleId.getText().toString().trim();
                    if (TextUtils.isEmpty(vehicleId)) {
                        ToastHelper.showToast("请输入车牌号");
                        return;
                    }

                    String taskLineId = et_taskLineId.getText().toString().trim();
                    if (TextUtils.isEmpty(taskLineId)){
                        ToastHelper.showToast("请输入所属线路");
                        return;
                    }

                    String driverId = et_driverId.getText().toString().trim();
                    if (TextUtils.isEmpty(driverId)){
                        ToastHelper.showToast("请输入驾驶员");
                        return;
                    }

                    String taskType = et_task_type.getText().toString().trim();
                    if (TextUtils.isEmpty(taskType)){
                        ToastHelper.showToast("请输入任务类型");
                        return;
                    }

                    String arriveTime = et_arrive_time.getText().toString().trim();
                    if (TextUtils.isEmpty(arriveTime)){
                        ToastHelper.showToast("请输入到站时刻");
                        return;
                    }

                    String planTime = et_plan_time.getText().toString().trim();
                    if (TextUtils.isEmpty(planTime)){
                        ToastHelper.showToast("请输入计划时刻");
                        return;
                    }

                    String stopTime = et_stop_time.getText().toString().trim();
                    if (TextUtils.isEmpty(stopTime)){
                        ToastHelper.showToast("请输入停场时间");
                        return;
                    }
                    // 具体参数未确定是否对应
                    mListener.onSaveAddRecordingCarTask(vehicleId,taskLineId,driverId,taskType);
                    dialog.dismiss();
                }
            }
        });

        dialog = setView(view).show();
    }


    public void shouCloseBtnStyle(){
        btn_close.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.GONE);
        btn_save.setVisibility(View.GONE);
    }

    public void shouSaveAndCancelBtnStyle(){
        btn_close.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.VISIBLE);
    }


    public interface OnAddRecordingLineOperateListener{
        void onSaveAddRecordingCarTask(String vehicleId,String taskLineId,String driverId,String taskType);
    }
}
