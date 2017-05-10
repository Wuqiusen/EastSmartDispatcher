package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.MySpinnerAdapter;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.RecordingCarAdapter;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

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
    private RecyclerView rv_recording_car;
    private RadioButton rb_normal, rb_operator_empty, rb_operator_not_empty, rb_help, rb_off_duty;
    private LinearLayout containerView1,containerView2,containerView3,containerView4;
    private TextView containerView5;
    private Spinner sp_recording_item2_car_task,sp_recording_item3_car_task;
    private EditText et_recording_item2_start_time,et_recording_item2_end_time,et_recording_item2_run_count
                     ,et_recording_item2_km,et_recording_item2_remarks;
    private EditText et_recording_item3_start_time,et_recording_item3_end_time,et_recording_item3_run_count
            ,et_recording_item3_km,et_recording_item3_remarks;
    private SmartEditText smartEt_recording_item1_vehicleId,smartEt_recording_item1_driverId,smartEt_recording_item1_stewardId;
    private SmartEditText smartEt_recording_item2_vehicleId,smartEt_recording_item2_driverId,smartEt_recording_item_stewardId;// 车牌号、驾驶员
    private SmartEditText smartEt_recording_item3_vehicleId,smartEt_recording_item3_driverId;

    private static MissionType.TaskContentBean mTaskContentBean;
    private SmartEditText seLine;

    private List<MissionType.TaskContentBean> emptyTaskContent,notEmptyTaskContent;
    private int currentCategory = 1;
    private StopHistory mStopCar;
    private OnAddRecordingListener mListener;
    private final int mLineId;
    private AlertDialog dialog;



    public RecordingCarTaskDialog(Context context, int lineId,OnAddRecordingListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
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

        rv_recording_car = (RecyclerView) container.findViewById(R.id.rv_recording_car);
        containerView1 = (LinearLayout) container.findViewById(R.id.item1);
        containerView2 = (LinearLayout) container.findViewById(R.id.item2);
        containerView3 = (LinearLayout) container.findViewById(R.id.item3);
        containerView4 = (LinearLayout) container.findViewById(R.id.item4);
        containerView5 = (TextView) container.findViewById(R.id.item5);
        seLine = (SmartEditText) container.findViewById(R.id.se_line);
        seLine.addQueryLineEditTextListener(mLineId + "");

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
                            dialog.dismiss();
                        dialog.dismiss();
                        break;
                    case 2:
                        // 营运空驶
                        if (onClickOperatorEmptyMission())
                            dialog.dismiss();
                        break;
                    case 3:
                        // 非营运空驶
                        if (onClickOperatorNotEmptyMission())
                            dialog.dismiss();
                        break;
                    case 4:
                        if (onClickHelpMission())
                            dialog.dismiss();
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


    private void recordingNormalView(MissionType missionType,LinearLayout item1) {
        RecyclerView rv_recording_item1_car = (RecyclerView) item1.findViewById(R.id.item1_rv_recording_car);
        smartEt_recording_item1_vehicleId = (SmartEditText) item1.findViewById(R.id.smartEt_recording_item1_vehicleId);
        smartEt_recording_item1_vehicleId.addQueryCarCodeEditTextListener();
        smartEt_recording_item1_driverId = (SmartEditText) item1.findViewById(R.id.smartEt_recording_item1_driverId);
        smartEt_recording_item1_driverId.addQueryDriverEditTextListener();
        smartEt_recording_item1_stewardId = (SmartEditText) item1.findViewById(R.id.smartEt_recording_item1_stewardId);
        smartEt_recording_item1_driverId.addQueryStewardEditTextListener();

        final List<MissionType.TaskContentBean> taskContents = missionType.getTaskContent();
        RecordingCarAdapter adapter = new RecordingCarAdapter(taskContents, mLineId + "", mContext
                , new RecordingCarAdapter.OnSelectMissionListener() {
            @Override
            public void onSelectMission(MissionType.TaskContentBean missionType) {
                mTaskContentBean = missionType;
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(mContext,4);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_recording_item1_car.setLayoutManager(layoutManager);
        rv_recording_item1_car.setAdapter(adapter);
        rv_recording_item1_car.addItemDecoration(new DividerItemDecoration(mContext,R.color.white,
                DividerItemDecoration.VERTICAL_LIST));
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
        smartEt_recording_item_stewardId = (SmartEditText) item2.findViewById(R.id.smartEt_recording_item_stewardId);
        smartEt_recording_item_stewardId.addQueryStewardEditTextListener();

        MissionType emptyMissionType = missionTypes.get(1);
        emptyTaskContent= emptyMissionType.getTaskContent();

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
        MissionType noEmptyMissionType = missionTypes.get(2);
        notEmptyTaskContent = noEmptyMissionType.getTaskContent();

// 建立Adapter并且绑定数据源
        MySpinnerAdapter notOperatorEmptyAdapter = new MySpinnerAdapter(mContext, notEmptyTaskContent);
        sp_recording_item3_car_task.setAdapter(notOperatorEmptyAdapter);
    }



    private boolean onClickOperatorEmptyMission() {
        //String vehicleId = smartEt_recording_item2_vehicleId.getText().toString().trim();
        //String driverId = smartEt_recording_item2_driverId.getText().toString().trim();
        String startTime = et_recording_item2_start_time.getText().toString().trim();
        String endTime = et_recording_item2_end_time.getText().toString().trim();
        String runCount = et_recording_item2_run_count.getText().toString().trim();
        String km = et_recording_item2_km.getText().toString().trim();
        String remarks = et_recording_item2_remarks.getText().toString().trim();

        if (smartEt_recording_item2_vehicleId.getVehicleInfo() == null){
            ToastHelper.showToast("请输入车牌号");
            return false;
        }
        String mVehicleId = String.valueOf(smartEt_recording_item2_vehicleId.getVehicleInfo().getVehicleId());
        if (TextUtils.isEmpty(mVehicleId)){
            ToastHelper.showToast("请输入车牌号");
            return false;
        }

        if (smartEt_recording_item2_driverId.getPeopleInfo() == null){
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        String mDriverId = String.valueOf(smartEt_recording_item2_driverId.getPeopleInfo().personId);
        if (TextUtils.isEmpty(mDriverId)){
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }

        if (isEmpty(startTime,"请填写预计出发时间")) return false;
        if (isEmpty(endTime,"请填写预计到达时间"))return false;
        if (isEmpty(runCount,"请填写折算单次"))return false;
        if (isEmpty(km,"请填写空驶里程"))return false;
        if (isEmpty(remarks,"请填写备注"))return false;
        if (startTime.length() != 4 || endTime.length() != 4){
            ToastHelper.showToast("请输入正确的时间");
            return false;
        }
        int selectSpItemPosition = sp_recording_item2_car_task.getSelectedItemPosition();
        int taskId = emptyTaskContent.get(selectSpItemPosition).getTaskId();
        mListener.onClickOperatorEmptyMissionDoConfirm(currentCategory,taskId,mVehicleId,mDriverId,startTime,endTime,runCount,km,remarks);
        return true;
    }

    private boolean onClickOperatorNotEmptyMission() {
        //String vehicleId = smartEt_recording_item3_vehicleId.getText().toString().trim();
        //String driverId = smartEt_recording_item3_driverId.getText().toString().trim();
        String startTime = et_recording_item3_start_time.getText().toString().trim();
        String endTime = et_recording_item3_end_time.getText().toString().trim();
        String runCount = et_recording_item3_run_count.getText().toString().trim();
        String km = et_recording_item3_km.getText().toString().trim();
        String remarks = et_recording_item3_remarks.getText().toString().trim();

        if (smartEt_recording_item3_vehicleId.getVehicleInfo() == null){
            ToastHelper.showToast("请输入车牌号");
            return false;
        }
        String mVehicleId = String.valueOf(smartEt_recording_item3_vehicleId.getVehicleInfo().getVehicleId());
        if (TextUtils.isEmpty(mVehicleId)){
            ToastHelper.showToast("请输入车牌号");
            return false;
        }

        if (smartEt_recording_item3_driverId.getPeopleInfo() == null){
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        String mDriverId = String.valueOf(smartEt_recording_item3_driverId.getPeopleInfo().personId);
        if (TextUtils.isEmpty(mDriverId)){
            ToastHelper.showToast("请输入驾驶员");
            return false;
        }
        if (isEmpty(startTime,"请填写预计出发时间")) return false;
        if (isEmpty(endTime,"请填写预计到达时间"))return false;
        if (isEmpty(runCount,"请填写折算单次"))return false;
        if (isEmpty(km,"请填写空驶里程"))return false;
        if (isEmpty(remarks,"请填写备注"))return false;
        if (startTime.length() != 4 || endTime.length() != 4){
            ToastHelper.showToast("请输入正确的时间");
            return false;
        }
        int selectSpItemPosition = sp_recording_item3_car_task.getSelectedItemPosition();
        int taskId = notEmptyTaskContent.get(selectSpItemPosition).getTaskId();
        mListener.onClickOperatorNotEmptyMissionDoConfirm(currentCategory,taskId,mVehicleId,mDriverId,startTime,endTime,runCount,km,remarks);
        return true;
    }


    private boolean isEmpty(String count, String str) {
        if (TextUtils.isEmpty(count)) {
            ToastHelper.showToast(str);
            return true;
        }
        return false;
    }


    private boolean onClickNormalMission() {
        if (mTaskContentBean == null) {
            ToastHelper.showToast("请选择线路");
            return false;
        }
        mListener.onClickNormalMission(currentCategory, mTaskContentBean.getTaskId());

        return true;
    }

    private boolean onClickHelpMission() {
        try {
            int lineId = seLine.getLineInfo().lineId;
            if (lineId == 0) {
                return false;
            }
            mListener.onClickHelpMission(currentCategory, lineId);
        } catch (Exception e) {
            ToastHelper.showToast(e.getMessage());
            return false;
        }
        return true;
    }


    private boolean onClickOffDutyMission() {
        if (dialog != null && dialog.isShowing()) {
            mListener.onOffDuty();
            return true;
        }
        return false;
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



    public interface OnAddRecordingListener{
        void onClickNormalMission(int currentCategory,int taskId);
        
        void onClickOperatorEmptyMissionDoConfirm(int currentCategory, int taskId, String vehicleId, String driverId, String startTime, String endTime, String runCount, String km, String remarks);

        void onClickOperatorNotEmptyMissionDoConfirm(int currentCategory, int taskId, String vehicleId, String driverId, String startTime, String endTime, String runCount, String km, String remarks);

        void onClickHelpMission(int currentCategory, int lineId);

        void onOffDuty();
    }
}
