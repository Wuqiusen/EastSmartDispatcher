package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.MissionType;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.LogUtil;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.MySpinnerAdapter;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;

/**
 * author MXQ
 * create at 2017/5/5 14:13
 * email: 1299242483@qq.com
 * describe: 补录
 */
public class RecordingCarTaskDialog extends AlertDialog.Builder {

    private Context mContext;

    private RadioButton rb_normal, rb_operator_empty, rb_operator_not_empty, rb_help, rb_off_duty;
    private LinearLayout containerView1, containerView2, containerView3, containerView4;
    private TextView containerView5;
    private EditText et_recording_item1_start_time,et_recording_item1_end_time;
    private Spinner sp_recording_item2_car_task,sp_recording_item3_car_task;
    private EditText et_recording_item2_start_time,et_recording_item2_end_time,et_recording_item2_run_count
                     ,et_recording_item2_km,et_recording_item2_remarks;
    private EditText et_recording_item3_start_time,et_recording_item3_end_time,et_recording_item3_run_count
            ,et_recording_item3_km,et_recording_item3_remarks;
    private EditText et_recording_item4_start_time,et_recording_item4_end_time;
    private LinearLayout ll_steward_recording_item1,ll_steward_recording_item2,ll_steward_recording_item3;
    private SmartEditText smartEt_recording_item1_vehicleId,smartEt_recording_item1_driverId,smartEt_recording_item1_stewardId;
    private SmartEditText smartEt_recording_item2_vehicleId,smartEt_recording_item2_driverId,smartEt_recording_item2_stewardId;
    private SmartEditText smartEt_recording_item3_vehicleId,smartEt_recording_item3_driverId,smartEt_recording_item3_stewardId;
    private SmartEditText smartEt_recording_item4_vehicleId,smartEt_recording_item4_driverId,smartEt_recording_item4_stewardId;

    private SmartEditText seLine;

    private List<MissionType.TaskContentBean> emptyTaskContent, notEmptyTaskContent;
    private int currentCategory = 1;
    private OnAddRecordingListener mListener;
    private LineParams mLineParams;
    private final int mLineId;
    private AlertDialog dialog;
    private HashMap<RadioButton, MissionType.TaskContentBean> normalTaskIdMap;
    private List<RadioButton> radioButtonList;
    private LinearLayout ll_steward_recording_item4;
    private LinearLayout ll_steward_recording_item41;


    public RecordingCarTaskDialog(Context context, LineParams lineParams, int lineId, OnAddRecordingListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mLineParams = lineParams;
        this.mLineId = lineId;
        this.mListener = listener;

        loadMissionTypeByRemote();
    }

    private void loadMissionTypeByRemote() {
        HttpMethods.getInstance().missionList(new Subscriber<List<MissionType>>() {
                                                  @Override
                                                  public void onCompleted() {

                                                  }

                                                  @Override
                                                  public void onError(Throwable e) {
                                                      LogUtil.loadRemoteError("missionList " + e.getMessage());
                                                  }

                                                  @Override
                                                  public void onNext(List<MissionType> missionTypes) {
                                                      DebugLog.w(missionTypes.toString());
                                                      init(missionTypes);

                                                  }
                                              }, SpUtils.getCache(mContext, SpUtils.USER_ID),
                SpUtils.getCache(mContext, SpUtils.KEYCODE), mLineId + "");
    }

    private void init(List<MissionType> missionTypes) {
        View container = View.inflate(mContext, R.layout.dialog_recording_car, null);
        TextView tv_recordingCar = (TextView) container.findViewById(R.id.tv_recording_car);
        RadioGroup rg_category = (RadioGroup) container.findViewById(R.id.rg_category);
        rb_normal = (RadioButton) container.findViewById(R.id.rb_normal);
        rb_operator_empty = (RadioButton) container.findViewById(R.id.rb_operator_empty);
        rb_operator_not_empty = (RadioButton) container.findViewById(R.id.rb_operator_not_empty);
        rb_help = (RadioButton) container.findViewById(R.id.rb_help);
        rb_off_duty = (RadioButton) container.findViewById(R.id.rb_off_duty);
        rg_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideContainerView();
                switch (checkedId) {
                    case R.id.rb_normal:
                        currentCategory = 1;
                        showContainerView1();
                        break;
                    case R.id.rb_operator_empty:
                        currentCategory = 2;
                        showContainerView2();
                        break;
                    case R.id.rb_operator_not_empty:
                        currentCategory = 3;
                        showContainerView3();
                        break;
                    case R.id.rb_help:
                        currentCategory = 4;
                        showContainerView4();
                        break;
                    case R.id.rb_off_duty:
                        currentCategory = 5;
                        showContainerView5();
                        break;
                }
            }
        });

        containerView1 = (LinearLayout) container.findViewById(R.id.item1);
        containerView2 = (LinearLayout) container.findViewById(R.id.item2);
        containerView3 = (LinearLayout) container.findViewById(R.id.item3);
        containerView4 = (LinearLayout) container.findViewById(R.id.item4);
        containerView5 = (TextView) container.findViewById(R.id.item5);

        recordingHelpView(containerView4);
        for (MissionType missionType : missionTypes) {
            switch (missionType.getType()) {
                case 1:
                    // 线路运营
                    recordingNormalView(missionType, containerView1);
                    break;
                case 2:
                    // 营运空驶
                    recordingOperatorEmptyView(missionTypes, containerView2);
                    break;
                case 3:
                    // 非营运空驶
                    recordingOperatorNotEmptyView(missionTypes, containerView3);
                    break;
                case 4:
                    // 支援
                    break;
            }
        }


        container.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null || !dialog.isShowing())
                    return;
                switch (currentCategory) {
                    case 1:
                        if (onClickNormalMission())
//                            dialog.dismiss();
                        break;
                    case 2:
                        // 营运空驶
                        if (onClickOperatorEmptyMission())
//                            dialog.dismiss();
                        break;
                    case 3:
                        // 非营运空驶
                        if (onClickOperatorNotEmptyMission())
//                            dialog.dismiss();
                        break;
                    case 4:
                        if (onClickHelpMission())
//                            dialog.dismiss();
                        break;
                    case 5:
                        //if (onClickOffDutyMission())
                        //    dialog.dismiss();
                        break;
                }
            }
        });

        container.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog = setView(container).show();
        dialog.setCancelable(false);

    }

    private void recordingHelpView(LinearLayout item4) {
        smartEt_recording_item4_vehicleId = (SmartEditText) item4.findViewById(R.id.smartEt_recording_item4_vehicleId);
        smartEt_recording_item4_vehicleId.addQueryCarCodeEditTextListener();
        smartEt_recording_item4_driverId = (SmartEditText) item4.findViewById(R.id.smartEt_recording_item4_driverId);
        smartEt_recording_item4_driverId.addQueryDriverEditTextListener();
        ll_steward_recording_item41 = (LinearLayout) item4.findViewById(R.id.ll_steward_recording_item4);
        smartEt_recording_item4_stewardId = (SmartEditText) item4.findViewById(R.id.smartEt_recording_item4_stewardId);
        et_recording_item4_start_time = (EditText) item4.findViewById(R.id.et_recording_item4_start_time);
        et_recording_item4_end_time = (EditText) item4.findViewById(R.id.et_recording_item4_end_time);

        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO){
            ll_steward_recording_item41.setVisibility(View.GONE);

        }else{
            ll_steward_recording_item41.setVisibility(View.VISIBLE);
            smartEt_recording_item4_stewardId.addQueryTrainManEditTextListener();
        }

        seLine = (SmartEditText) item4.findViewById(R.id.se_line);
        seLine.addQueryLineEditTextListener(mLineId + "");
    }


    private void recordingNormalView(MissionType missionType, LinearLayout item1) {
        RadioGroup rg_stop_car_item1 = (RadioGroup) item1.findViewById(R.id.rg_recording_car_item1);
        smartEt_recording_item1_vehicleId = (SmartEditText) item1.findViewById(R.id.smartEt_recording_item1_vehicleId);
        smartEt_recording_item1_vehicleId.addQueryCarCodeEditTextListener();
        smartEt_recording_item1_driverId = (SmartEditText) item1.findViewById(R.id.smartEt_recording_item1_driverId);
        smartEt_recording_item1_driverId.addQueryDriverEditTextListener();
        ll_steward_recording_item1 = (LinearLayout) item1.findViewById(R.id.ll_steward_recording_item1);
        smartEt_recording_item1_stewardId = (SmartEditText) item1.findViewById(R.id.smartEt_recording_item1_stewardId);
        et_recording_item1_start_time = (EditText) item1.findViewById(R.id.et_recording_item1_start_time);
        et_recording_item1_end_time = (EditText) item1.findViewById(R.id.et_recording_item1_end_time);

        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO) {
            ll_steward_recording_item1.setVisibility(View.GONE);

        } else {
            ll_steward_recording_item1.setVisibility(View.VISIBLE);
            smartEt_recording_item1_stewardId.addQueryTrainManEditTextListener();
        }

        if (normalTaskIdMap == null) {
            normalTaskIdMap = new HashMap<>();
        } else {
            normalTaskIdMap.clear();
        }
        if (radioButtonList == null) {
            radioButtonList = new ArrayList<>();
        } else {
            radioButtonList.clear();
        }
        final List<MissionType.TaskContentBean> taskContents = missionType.getTaskContent();
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < taskContents.size(); i++) {
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setText(taskContents.get(i).getTaskName());
            normalTaskIdMap.put(radioButton, taskContents.get(i));
            rg_stop_car_item1.addView(radioButton, i, layoutParams);
            if (mLineId == taskContents.get(i).getTaskId())
                rg_stop_car_item1.check(radioButton.getId());
            radioButtonList.add(radioButton);
        }
    }

    private void recordingOperatorEmptyView(List<MissionType> missionTypes, LinearLayout item2) {
        sp_recording_item2_car_task = (Spinner) item2.findViewById(R.id.sp_recording_item_car_task);
        et_recording_item2_start_time = (EditText) item2.findViewById(R.id.et_recording_item_start_time);
        et_recording_item2_end_time = (EditText) item2.findViewById(R.id.et_recording_item_end_time);
        et_recording_item2_run_count = (EditText) item2.findViewById(R.id.et_recording_item_run_count);
        et_recording_item2_km = (EditText) item2.findViewById(R.id.et_recording_item_km);
        et_recording_item2_remarks = (EditText) item2.findViewById(R.id.et_recording_item_remarks);
        et_recording_item2_remarks.setHint(mContext.getResources().getString(R.string.hint_operate_empty_dialog));
        smartEt_recording_item2_vehicleId = (SmartEditText) item2.findViewById(R.id.smartEt_recording_item_vehicleId);
        smartEt_recording_item2_vehicleId.addQueryCarCodeEditTextListener();
        smartEt_recording_item2_driverId = (SmartEditText) item2.findViewById(R.id.smartEt_recording_item_driverId);
        smartEt_recording_item2_driverId.addQueryDriverEditTextListener();
        ll_steward_recording_item2 = (LinearLayout) item2.findViewById(R.id.ll_steward_recording_item2_3);
        smartEt_recording_item2_stewardId = (SmartEditText) item2.findViewById(R.id.smartEt_recording_item_stewardId);

        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO) {
            ll_steward_recording_item2.setVisibility(View.GONE);
        } else {
            ll_steward_recording_item2.setVisibility(View.VISIBLE);
            smartEt_recording_item2_stewardId.addQueryTrainManEditTextListener();
        }

        MissionType emptyMissionType = missionTypes.get(1);
        emptyTaskContent = emptyMissionType.getTaskContent();

// 建立Adapter并且绑定数据源
        MySpinnerAdapter operatorEmptyAdapter = new MySpinnerAdapter(mContext, emptyTaskContent);
        sp_recording_item2_car_task.setAdapter(operatorEmptyAdapter);
    }


    private void recordingOperatorNotEmptyView(List<MissionType> missionTypes, LinearLayout item3) {
        sp_recording_item3_car_task = (Spinner) item3.findViewById(R.id.sp_recording_item_car_task);
        et_recording_item3_start_time = (EditText) item3.findViewById(R.id.et_recording_item_start_time);
        et_recording_item3_end_time = (EditText) item3.findViewById(R.id.et_recording_item_end_time);
        et_recording_item3_run_count = (EditText) item3.findViewById(R.id.et_recording_item_run_count);
        et_recording_item3_km = (EditText) item3.findViewById(R.id.et_recording_item_km);
        et_recording_item3_remarks = (EditText) item3.findViewById(R.id.et_recording_item_remarks);
        et_recording_item3_remarks.setHint(mContext.getResources().getString(R.string.hint_operate_empty_dialog));
        smartEt_recording_item3_vehicleId = (SmartEditText) item3.findViewById(R.id.smartEt_recording_item_vehicleId);
        smartEt_recording_item3_vehicleId.addQueryCarCodeEditTextListener();
        smartEt_recording_item3_driverId = (SmartEditText) item3.findViewById(R.id.smartEt_recording_item_driverId);
        smartEt_recording_item3_driverId.addQueryDriverEditTextListener();
        ll_steward_recording_item3 = (LinearLayout) item3.findViewById(R.id.ll_steward_recording_item2_3);
        smartEt_recording_item3_stewardId = (SmartEditText) item3.findViewById(R.id.smartEt_recording_item_stewardId);

        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO) {
            ll_steward_recording_item3.setVisibility(View.GONE);
        } else {
            ll_steward_recording_item3.setVisibility(View.VISIBLE);
            smartEt_recording_item3_stewardId.addQueryTrainManEditTextListener();
        }

        MissionType noEmptyMissionType = missionTypes.get(2);
        notEmptyTaskContent = noEmptyMissionType.getTaskContent();

// 建立Adapter并且绑定数据源
        MySpinnerAdapter notOperatorEmptyAdapter = new MySpinnerAdapter(mContext, notEmptyTaskContent);
        sp_recording_item3_car_task.setAdapter(notOperatorEmptyAdapter);
    }


    private boolean onClickNormalMission() {
        if (smartEt_recording_item1_vehicleId.getVehicleInfo() == null) {
            ToastHelper.showToast("请输入车牌号");
            return false;
        }
        String mVehicleId = String.valueOf(smartEt_recording_item1_vehicleId.getVehicleInfo().getVehicleId());
        if (TextUtils.isEmpty(mVehicleId)) {
            ToastHelper.showToast("请输入车牌号");
            return false;
        }

        if (smartEt_recording_item1_driverId.getPeopleInfo() == null) {
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        String mDriverId = String.valueOf(smartEt_recording_item1_driverId.getPeopleInfo().personId);
        if (TextUtils.isEmpty(mDriverId)) {
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        String mStewardId = null;
        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_MANUAL) {
            if (smartEt_recording_item1_stewardId.getPeopleInfo() == null) {
                ToastHelper.showToast("请输入乘务员姓名");
                return false;
            }
            mStewardId = String.valueOf(smartEt_recording_item1_stewardId.getPeopleInfo().personId);
            if (TextUtils.isEmpty(mStewardId)) {
                ToastHelper.showToast("请输入乘务员姓名");
                return false;
            }
        }

        MissionType.TaskContentBean taskContentBean = null;
        for (RadioButton radioButton : radioButtonList) {
            if (radioButton.isChecked()) {
                taskContentBean = normalTaskIdMap.get(radioButton);
                break;
            }
        }

        if (taskContentBean == null) {
            ToastHelper.showToast("请选择线路");
            return false;
        }

        String mStartTime = et_recording_item1_start_time.getText().toString().trim();
        if (isEmpty(mStartTime,"请填写预计出发时间")) return false;
        String mEndTime = et_recording_item1_end_time.getText().toString().trim();
        if (isEmpty(mEndTime,"请填写预计到达时间")) return false;
        if (mStartTime.length() != 4 || mEndTime.length() != 4) {
            ToastHelper.showToast("请输入正确的时间");
            return false;
        }
        mListener.onClickNormalMission(currentCategory, taskContentBean.getTaskId(), mVehicleId, mDriverId, mStewardId,mStartTime,mEndTime);
        return true;
    }


    private boolean onClickOperatorEmptyMission() {
        String startTime = et_recording_item2_start_time.getText().toString().trim();
        String endTime = et_recording_item2_end_time.getText().toString().trim();
        String runCount = et_recording_item2_run_count.getText().toString().trim();
        String km = et_recording_item2_km.getText().toString().trim();
        String remarks = et_recording_item2_remarks.getText().toString().trim();

        if (smartEt_recording_item2_vehicleId.getVehicleInfo() == null) {
            ToastHelper.showToast("请输入车牌号");
            return false;
        }
        String mVehicleId = String.valueOf(smartEt_recording_item2_vehicleId.getVehicleInfo().getVehicleId());
        if (TextUtils.isEmpty(mVehicleId)) {
            ToastHelper.showToast("请输入车牌号");
            return false;
        }

        if (smartEt_recording_item2_driverId.getPeopleInfo() == null) {
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        String mDriverId = String.valueOf(smartEt_recording_item2_driverId.getPeopleInfo().personId);
        if (TextUtils.isEmpty(mDriverId)) {
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        String mStewardId = null;
        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_MANUAL) {
            if (smartEt_recording_item2_stewardId.getPeopleInfo() == null) {
                ToastHelper.showToast("请输入乘务员姓名");
                return false;
            }
            mStewardId = String.valueOf(smartEt_recording_item2_stewardId.getPeopleInfo().personId);
            if (TextUtils.isEmpty(mStewardId)) {
                ToastHelper.showToast("请输入乘务员姓名");
                return false;
            }
        }

        if (isEmpty(startTime, "请填写预计出发时间")) return false;
        if (isEmpty(endTime, "请填写预计到达时间")) return false;
        if (isEmpty(runCount, "请填写折算单次")) return false;
        if (isEmpty(km, "请填写空驶里程")) return false;
        if (startTime.length() != 4 || endTime.length() != 4) {
            ToastHelper.showToast("请输入正确的时间");
            return false;
        }
        int selectSpItemPosition = sp_recording_item2_car_task.getSelectedItemPosition();
        int taskId = emptyTaskContent.get(selectSpItemPosition).getTaskId();
        mListener.onClickOperatorEmptyMissionDoConfirm(currentCategory, taskId, mVehicleId, mDriverId, mStewardId, startTime, endTime, runCount, km, remarks);
        return true;
    }

    private boolean onClickOperatorNotEmptyMission() {
        String startTime = et_recording_item3_start_time.getText().toString().trim();
        String endTime = et_recording_item3_end_time.getText().toString().trim();
        String runCount = et_recording_item3_run_count.getText().toString().trim();
        String km = et_recording_item3_km.getText().toString().trim();
        String remarks = et_recording_item3_remarks.getText().toString().trim();

        if (smartEt_recording_item3_vehicleId.getVehicleInfo() == null) {
            ToastHelper.showToast("请输入车牌号");
            return false;
        }
        String mVehicleId = String.valueOf(smartEt_recording_item3_vehicleId.getVehicleInfo().getVehicleId());
        if (TextUtils.isEmpty(mVehicleId)) {
            ToastHelper.showToast("请输入车牌号");
            return false;
        }

        if (smartEt_recording_item3_driverId.getPeopleInfo() == null) {
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        String mDriverId = String.valueOf(smartEt_recording_item3_driverId.getPeopleInfo().personId);
        if (TextUtils.isEmpty(mDriverId)) {
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        String mStewardId = null;
        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_MANUAL) {
            if (smartEt_recording_item3_stewardId.getPeopleInfo() == null) {
                ToastHelper.showToast("请输入乘务员姓名");
                return false;
            }
            mStewardId = String.valueOf(smartEt_recording_item3_stewardId.getPeopleInfo().personId);
            if (TextUtils.isEmpty(mStewardId)) {
                ToastHelper.showToast("请输入乘务员姓名");
                return false;
            }
        }
        if (isEmpty(startTime, "请填写预计出发时间")) return false;
        if (isEmpty(endTime, "请填写预计到达时间")) return false;
        if (isEmpty(runCount, "请填写折算单次")) return false;
        if (isEmpty(km, "请填写空驶里程")) return false;
        if (startTime.length() != 4 || endTime.length() != 4) {
            ToastHelper.showToast("请输入正确的时间");
            return false;
        }
        int selectSpItemPosition = sp_recording_item3_car_task.getSelectedItemPosition();
        int taskId = notEmptyTaskContent.get(selectSpItemPosition).getTaskId();
        mListener.onClickOperatorNotEmptyMissionDoConfirm(currentCategory, taskId, mVehicleId, mDriverId, mStewardId, startTime, endTime, runCount, km, remarks);
        return true;
    }


    private boolean isEmpty(String count, String str) {
        if (TextUtils.isEmpty(count)) {
            ToastHelper.showToast(str);
            return true;
        }
        return false;
    }


    private boolean onClickHelpMission() {
        String mVehicleId = null;
        String mDriverId = null;
        String mStewardId = null;
        String mStartTime = null;
        String mEndTime = null;
        try {
            if (smartEt_recording_item4_vehicleId.getVehicleInfo() == null){
                ToastHelper.showToast("请输入车牌号");
                return false;
            }
            mVehicleId = String.valueOf(smartEt_recording_item4_vehicleId.getVehicleInfo().getVehicleId());
            if (TextUtils.isEmpty(mVehicleId)){
                ToastHelper.showToast("请输入车牌号");
                return false;
            }

            if (smartEt_recording_item4_driverId.getPeopleInfo() == null){
                ToastHelper.showToast("请输入驾驶员");
                return false;
            }
            mDriverId = String.valueOf(smartEt_recording_item4_driverId.getPeopleInfo().personId);
            if (TextUtils.isEmpty(mDriverId)){
                ToastHelper.showToast("请输入驾驶员");
                return false;
            }
            if(mLineParams.getSaleType() == MainPresenter.TYPE_SALE_MANUAL){
                if(smartEt_recording_item4_stewardId.getPeopleInfo() == null){
                    ToastHelper.showToast("请输入乘务员姓名");
                    return false;
                }
                mStewardId = String.valueOf(smartEt_recording_item4_stewardId.getPeopleInfo().personId);
                if(TextUtils.isEmpty(mStewardId)){
                    ToastHelper.showToast("请输入乘务员姓名");
                    return false;
                }
            }

            int lineId = seLine.getLineInfo().lineId;
            if (lineId == 0) {
                return false;
            }

            mStartTime = et_recording_item4_start_time.getText().toString().trim();
            if (isEmpty(mStartTime,"请填写预计出发时间")) return false;
            mEndTime = et_recording_item4_end_time.getText().toString().trim();
            if (isEmpty(mEndTime,"请填写预计到达时间")) return false;
            if (mStartTime.length() != 4 || mEndTime.length() != 4) {
                ToastHelper.showToast("请输入正确的时间");
                return false;
            }

            mListener.onClickHelpMission(currentCategory, lineId, mVehicleId, mDriverId, mStewardId,mStartTime,mEndTime);
        } catch (Exception e) {
            ToastHelper.showToast(e.getMessage());
            return false;
        }
        return true;
    }


    private void showContainerView5() {
        containerView5.setVisibility(View.VISIBLE);
    }

    private void showContainerView4() {
        containerView4.setVisibility(View.VISIBLE);
    }

    private void showContainerView3() {
        containerView3.setVisibility(View.VISIBLE);
    }

    private void showContainerView2() {
        containerView2.setVisibility(View.VISIBLE);
    }

    private void showContainerView1() {
        containerView1.setVisibility(View.VISIBLE);
    }

    private void hideContainerView() {
        containerView1.setVisibility(View.GONE);
        containerView2.setVisibility(View.GONE);
        containerView3.setVisibility(View.GONE);
        containerView4.setVisibility(View.GONE);
        containerView5.setVisibility(View.GONE);
    }


    public interface OnAddRecordingListener {
        void onClickNormalMission(int type, int taskId, String vehicleId, String driverId, String stewardId,String startTime,String endTime);

        void onClickOperatorEmptyMissionDoConfirm(int type, int taskType, String vehicleId, String driverId, String stewardId, String startTime, String endTime, String runCount, String km, String remarks);

        void onClickOperatorNotEmptyMissionDoConfirm(int type, int taskType, String vehicleId, String driverId, String stewardId, String startTime, String endTime, String runCount, String km, String remarks);

        void onClickHelpMission(int type, int taskId, String vehicleId, String driverId, String stewardId,String startTime,String endTime);

    }
}
