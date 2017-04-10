package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.zxw.data.bean.MissionType;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.MySpinnerAdapter;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * author MXQ
 * create at 2017/4/7 18:44
 * email: 1299242483@qq.com
 */
public class AddRecordingCarTaskDialog extends AlertDialog.Builder implements View.OnClickListener{

    private Context mContext;
    private OnAddRecordingCarTaskListener mListener;
    private RadioButton rb_operator_empty;
    private TextView tv_operator_empty;
    private RadioButton rb_no_operator_empty;
    private TextView tv_no_operator_empty;
    private Spinner sp_task_name;
    private SmartEditText smartEt_vehicleId;
    private SmartEditText smartEt_driverId;
    private EditText et_beginTime;
    private EditText et_endTime;
    private EditText et_runNum;
    private EditText et_runEmpMileage;
    private Button btn_save;
    private Button btn_cancel;
    private AlertDialog dialog;
    private String currentType = OPERATOR_EMPTY;
    private final static String OPERATOR_EMPTY = "2", NOT_OPERATOR_EMPTY = "3";
    private int taskId = -1;
    private MySpinnerAdapter notOperatorEmptyAdapter, operatorEmptyAdapter;
    private List<MissionType> mMissionTypes = new ArrayList<>();
    private List<MissionType.TaskContentBean> emptyTaskContent = new ArrayList<>();
    private List<MissionType.TaskContentBean> noEmptyTaskContent = new ArrayList<>();


    public AddRecordingCarTaskDialog(Context context, List<MissionType> missionTypes,OnAddRecordingCarTaskListener listener) {
        super(context,R.style.alder_dialog);
        this.mContext = context;
        this.mMissionTypes = missionTypes;
        this.mListener = listener;
        initData(missionTypes);
        initView(context);
    }


    private void initData(List<MissionType> missionTypes) {
        MissionType emptyMissionType = missionTypes.get(1);
        emptyTaskContent= emptyMissionType.getTaskContent();
        MissionType noEmptyMissionType = missionTypes.get(2);
        noEmptyTaskContent = noEmptyMissionType.getTaskContent();
        operatorEmptyAdapter = new MySpinnerAdapter(mContext,emptyTaskContent);
        notOperatorEmptyAdapter = new MySpinnerAdapter(mContext,noEmptyTaskContent);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.dialog_car_task,null);
        rb_operator_empty = (RadioButton) view.findViewById(R.id.rb_operator_empty);
        tv_operator_empty = (TextView) view.findViewById(R.id.tv_operator_empty);
        rb_no_operator_empty = (RadioButton) view.findViewById(R.id.rb_no_operator_empty);
        tv_no_operator_empty = (TextView) view.findViewById(R.id.tv_no_operator_empty);
        smartEt_vehicleId = (SmartEditText) view.findViewById(R.id.smartEt_vehicleId);
        smartEt_vehicleId.addQueryCarCodeEditTextListener();
        smartEt_driverId = (SmartEditText) view.findViewById(R.id.smartEt_driverId);
        smartEt_driverId.addQueryDriverEditTextListener();
        et_beginTime = (EditText) view.findViewById(R.id.et_beginTime);
        et_endTime = (EditText) view.findViewById(R.id.et_endTime);
        et_runNum = (EditText) view.findViewById(R.id.et_runNum);
        et_runEmpMileage = (EditText) view.findViewById(R.id.et_runEmpMileage);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        sp_task_name = (Spinner) view.findViewById(R.id.sp_task_name);
        rb_operator_empty.setOnClickListener(this);
        tv_operator_empty.setOnClickListener(this);
        rb_no_operator_empty.setOnClickListener(this);
        tv_no_operator_empty.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        setEmptyRbChecked();
        dialog = setView(view).show();
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_operator_empty:    //营运空驶
            case R.id.tv_operator_empty:
                setEmptyRbChecked();
                break;
            case R.id.rb_no_operator_empty: // 非营运空驶
            case R.id.tv_no_operator_empty:
                setNoEmptyRbChecked();
                break;
            case R.id.btn_save:
                saveNewAddCarTask();
                break;
            case R.id.btn_cancel:
                closeDialog();
                break;
        }
    }

    private void setEmptyRbChecked() {
        currentType = OPERATOR_EMPTY;
        rb_operator_empty.setChecked(true);
        rb_no_operator_empty.setChecked(false);
        sp_task_name.setAdapter(operatorEmptyAdapter);
    }

    private void setNoEmptyRbChecked() {
        currentType = NOT_OPERATOR_EMPTY;
        rb_operator_empty.setChecked(false);
        rb_no_operator_empty.setChecked(true);
        sp_task_name.setAdapter(notOperatorEmptyAdapter);
    }

    public void getTaskIdFromTaskName(String type,String taskName){
        String name = null;
        switch (type){
            case "1":
                for (int i=0;i<emptyTaskContent.size();i++){
                     name = emptyTaskContent.get(i).getTaskName();
                    if (!TextUtils.isEmpty(name) && taskName.equals(name)){
                         taskId = emptyTaskContent.get(i).getTaskId();
                         return;
                    }else{
                        continue;
                    }
                }
                break;
            case "2":
                for (int i=0;i<noEmptyTaskContent.size();i++){
                     name = noEmptyTaskContent.get(i).getTaskName();
                    if (!TextUtils.isEmpty(name) && taskName.equals(name)){
                        taskId = noEmptyTaskContent.get(i).getTaskId();
                        return;
                    }else{
                        continue;
                    }
                }
                break;
        }
    }


    private void saveNewAddCarTask() {
        if (dialog != null && dialog.isShowing()){
            int taskId = -1;
            if(emptyTaskContent == null || emptyTaskContent.size() == 0)
                return;
            if(noEmptyTaskContent == null || noEmptyTaskContent.size() == 0)
                return;
            if (emptyTaskContent != null && emptyTaskContent.size() > 0){
                int selectedItemPosition = sp_task_name.getSelectedItemPosition();
                if (currentType.equals(OPERATOR_EMPTY)){
                    taskId = emptyTaskContent.get(selectedItemPosition).getTaskId();
                }else if(currentType.equals(NOT_OPERATOR_EMPTY)){
                    taskId = noEmptyTaskContent.get(selectedItemPosition).getTaskId();
                }
            }else {
                return;//
            }
            if (smartEt_vehicleId.getVehicleInfo() == null){
                 ToastHelper.showToast("请输入车牌号");
                 return;
            }
            String carId = String.valueOf(smartEt_vehicleId.getVehicleInfo().getVehicleId());
            if (TextUtils.isEmpty(carId)){
                ToastHelper.showToast("请输入车牌号");
                return;
            }


            if (smartEt_driverId.getPeopleInfo() == null){
                ToastHelper.showToast("请输入驾驶员");
                return;
            }
            String driverId = String.valueOf(smartEt_driverId.getPeopleInfo().personId);
            if (TextUtils.isEmpty(driverId)){
                ToastHelper.showToast("请输入驾驶员");
                return;
            }
            String beginTime = et_beginTime.getText().toString().trim();
            if (TextUtils.isEmpty(beginTime)) {
                ToastHelper.showToast("请输入开始时间");
                return;
            }
            String endTime = et_endTime.getText().toString().trim();
            if (TextUtils.isEmpty(endTime)) {
                ToastHelper.showToast("请输入结束时间");
                return;
            }
            String runNum = et_runNum.getText().toString().trim();
            if (TextUtils.isEmpty(runNum)){
                ToastHelper.showToast("请输入折算单次(次)");
                return;
            }
            String runEmpMileage = et_runEmpMileage.getText().toString().trim();
            if (TextUtils.isEmpty(runEmpMileage)) {
                ToastHelper.showToast("请输入空驶里程(km)");
                return;
            }
            mListener.OnAddRecordingCarTask(currentType, String.valueOf(taskId), carId, driverId,
                     beginTime, endTime, runNum, runEmpMileage);
            dialog.dismiss();
        }

    }

    private void closeDialog() {
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public interface OnAddRecordingCarTaskListener{
        void OnAddRecordingCarTask(String type,String taskId,String vehicleId,String driverId,
                                   String beginTime,String endTime,String runNum,String runEmpMileage);
    }
}
