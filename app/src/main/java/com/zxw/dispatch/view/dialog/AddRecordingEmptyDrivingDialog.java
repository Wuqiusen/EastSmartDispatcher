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
 * create at 2017/4/7 16:24
 * email: 1299242483@qq.com
 */
public class AddRecordingEmptyDrivingDialog extends AlertDialog.Builder {

    private Context mContext;
    private EditText et_taskLineId;
    private EditText et_vehicleId;
    private EditText et_driverId;
    private EditText et_task_type;
    private EditText et_runNum; //折算单次
    private EditText et_runEmpMileage;
    private EditText et_beginTime;
    private EditText et_endTime;
    private Button btn_close;
    private Button btn_cancel;
    private Button btn_save;
    private AlertDialog dialog;
    private OnAddRecordingEmptyDrivingListener mListener;

    public AddRecordingEmptyDrivingDialog(Context context,OnAddRecordingEmptyDrivingListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        init(mContext);

    }

    private void init(Context context) {
        View v =View.inflate(context, R.layout.dialog_add_recording_empty_driving,null);
        et_taskLineId = (EditText) v.findViewById(R.id.et_taskLineId);
        et_vehicleId = (EditText) v.findViewById(R.id.et_vehicleId);
        et_beginTime = (EditText) v.findViewById(R.id.et_beginTime);
        et_endTime = (EditText) v.findViewById(R.id.et_endTime);
        et_driverId = (EditText) v.findViewById(R.id.et_driverId);
        et_runNum = (EditText) v.findViewById(R.id.et_runNum);
        et_runEmpMileage = (EditText) v.findViewById(R.id.et_runEmpMileage);
        et_task_type = (EditText) v.findViewById(R.id.et_task_type);

        btn_close = (Button) v.findViewById(R.id.btn_close);
        btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        btn_save = (Button) v.findViewById(R.id.btn_save);
        showSaveAndCancelBtnStyle();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.isShowing();
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    String taskLineId = et_taskLineId.getText().toString().trim();
                    if (TextUtils.isEmpty(taskLineId)){
                        ToastHelper.showToast("请输入所属线路");
                        return;
                    }
                    String vehicleId = et_vehicleId.getText().toString().trim();
                    if (TextUtils.isEmpty(vehicleId)){
                        ToastHelper.showToast("请输入车牌号");
                        return;
                    }

                    String driverId = et_driverId.getText().toString().trim();
                    if (TextUtils.isEmpty(driverId)){
                        ToastHelper.showToast("请输入驾驶员ID");
                        return;
                    }
                    String taskType = et_task_type.getText().toString().trim();
                    if (TextUtils.isEmpty(taskType)){
                        ToastHelper.showToast("请输入任务名称");
                        return;
                    }

                    String runNum = et_runNum.getText().toString().trim();
                    if (TextUtils.isEmpty(runNum)){
                        ToastHelper.showToast("请输入折算单次");
                        return;
                    }

                    String runEmpMileage = et_runEmpMileage.getText().toString().trim();
                    if (TextUtils.isEmpty(runEmpMileage)){
                        ToastHelper.showToast("请输入空驶里程");
                        return;
                    }

                    String beginTime = et_beginTime.getText().toString().trim();
                    if (TextUtils.isEmpty(beginTime)){
                        ToastHelper.showToast("请输入计划发车时间");
                        return;
                    }

                    String endTime = et_endTime.getText().toString().trim();
                    if (TextUtils.isEmpty(endTime)){
                        ToastHelper.showToast("请输入计划结束时间");
                        return;
                    }

                    mListener.onSaveAddRecordingCarTask(vehicleId,driverId,taskType,
                             runNum,runEmpMileage,beginTime,endTime);
                }
            }
        });

    }


    public void showCloseBtnStyle(){
        btn_close.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.GONE);
        btn_save.setVisibility(View.GONE);
    }

    public void showSaveAndCancelBtnStyle(){
        btn_close.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.VISIBLE);
    }


    public interface OnAddRecordingEmptyDrivingListener{
        void onSaveAddRecordingCarTask(String vehicleId,String driverId,String taskType,
                                       String runNum,String runEmpMileage,String beginTime,String endTime);
    }

}
