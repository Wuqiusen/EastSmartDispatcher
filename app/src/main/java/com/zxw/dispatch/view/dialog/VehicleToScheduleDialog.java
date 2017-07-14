package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.LogUtil;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.MySpinnerAdapter;
import com.zxw.dispatch.presenter.BasePresenter;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;

import rx.Subscriber;

import static com.zxw.dispatch.R.id.sp_stop_car_item;

/**
 * author MXQ
 * create at 2017/3/10 11:33
 * email: 1299242483@qq.com
 */
public class VehicleToScheduleDialog extends AlertDialog.Builder {

    private final int lineId;
    private Context mContext;
    private StopHistory mStopCar;
    //    private TextView tv_carCode;
    public static  AlertDialog dialog = null;
    private OnClickListener mListener;
    private RadioGroup rg_stop_car_item1;
    private RadioButton rb_normal, rb_operator_empty, rb_operator_not_empty, rb_help, rb_off_duty;
    private int currentCategory = 1;
    private EditText et_stop_car_item_start_time2, et_stop_car_item_start_time3;
    private EditText et_stop_car_item_end_time2, et_stop_car_item_end_time3;
    private EditText et_stop_car_item_count2, et_stop_car_item_count3;
    private EditText et_stop_car_item_km2, et_stop_car_item_km3,et_stop_car_item_remarks2,et_stop_car_item_remarks3;
    private HashMap<RadioButton, MissionType.TaskContentBean> normalTaskIdMap;
    private SmartEditText seLine;
    private List<RadioButton> radioButtonList;

    private LinearLayout containerView1;
    private LinearLayout containerView2;
    private LinearLayout containerView3;
    private LinearLayout containerView4;
    private List<MissionType.TaskContentBean> taskContents3;
    private List<MissionType.TaskContentBean> taskContents2;
    private Spinner sp_stop_car_item2;
    private Spinner sp_stop_car_item3;
    private TextView containerView5;
    private BasePresenter.LoadDataStatus loadDataStatus;
    private Button btn_confirm;

    public VehicleToScheduleDialog(Context context, StopHistory stopCar, OnClickListener listener, int lineId) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mStopCar = stopCar;
        this.mListener = listener;
        this.lineId = lineId;
        loadMissionTypeByRemote();
    }

    public AlertDialog getDialog(){
        return dialog;
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
                SpUtils.getCache(mContext, SpUtils.KEYCODE), lineId + "");
    }

    private void init(List<MissionType> missionTypes) {
        View container = View.inflate(mContext, R.layout.dialog_stop_car, null);
        TextView tv_carCode = (TextView) container.findViewById(R.id.tv_carCode);
        tv_carCode.setText("车辆任务 （" + mStopCar.code + "）");
        RadioGroup rg_category = (RadioGroup) container.findViewById(R.id.rg_category);
        rb_normal = (RadioButton) container.findViewById(R.id.rb_normal);
        rb_operator_empty = (RadioButton) container.findViewById(R.id.rb_operator_empty);
        rb_operator_not_empty = (RadioButton) container.findViewById(R.id.rb_operator_not_empty);
        rb_help = (RadioButton) container.findViewById(R.id.rb_help);
        rb_off_duty = (RadioButton) container.findViewById(R.id.rb_off_duty);
        btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        rg_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideContainerView();
                clearNormalMissionRadioButtonStatus();
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
                }
            }
        });
        rg_stop_car_item1 = (RadioGroup) container.findViewById(R.id.rg_stop_car_item1);
        containerView1 = (LinearLayout) container.findViewById(R.id.item1);
        containerView2 = (LinearLayout) container.findViewById(R.id.item2);
        containerView3 = (LinearLayout) container.findViewById(R.id.item3);
        containerView4 = (LinearLayout) container.findViewById(R.id.item4);
        containerView5 = (TextView) container.findViewById(R.id.item5);
        seLine = (SmartEditText) container.findViewById(R.id.se_line);
        seLine.addQueryLineEditTextListener(lineId + "");

        for (MissionType missionType : missionTypes) {
            switch (missionType.getType()) {
                case 1:
                    generateNormalView(missionType, rg_stop_car_item1);
                    break;
                case 2:
                    generateOperatorEmptyView(missionType, containerView2);
                    break;
                case 3:
                    generateOperatorNotEmptyView(missionType, containerView3);
                    break;
                case 4:

                    break;
            }
        }
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null || !dialog.isShowing())
                    return;

                btn_confirm.setClickable(false);
                loadDataStatus = new BasePresenter.LoadDataStatus() {
                    @Override
                    public void OnLoadDataFinish() {
                        btn_confirm.setClickable(true);
                    }
                };
                switch (currentCategory) {
                    case 1:
                        if (onClickNormalMission())
                            dialog.dismiss();
                        else
                            btn_confirm.setClickable(true);
                        break;
                    case 2:
                        if (onClickOperatorEmptyMission())
                            dialog.dismiss();
                        else
                            btn_confirm.setClickable(true);
                        break;
                    case 3:
                        if (onClickOperatorNotEmptyMission())
                            dialog.dismiss();
                        else
                            btn_confirm.setClickable(true);
                        break;
                    case 4:
                        if (onClickHelpMission())
                            dialog.dismiss();
                        else
                            btn_confirm.setClickable(true);
                        break;
                    case 5:
                        if (onClickOffDutyMission())
                            dialog.dismiss();
                        else
                            btn_confirm.setClickable(true);
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mListener != null)
                mListener.onDismiss();
            }
        });
    }



    private void clearNormalMissionRadioButtonStatus() {
        rg_stop_car_item1.clearCheck();
    }

    private boolean onClickOffDutyMission() {
        if (dialog != null && dialog.isShowing()) {
            mListener.onOffDuty(loadDataStatus);
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


    private boolean onClickHelpMission() {
        try {
            int lineId = seLine.getLineInfo().lineId;
            if (lineId == 0) {
                return false;
            }
            mListener.onClickHelpMission(currentCategory, lineId, loadDataStatus);
        } catch (Exception e) {
            ToastHelper.showToast(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean onClickOperatorNotEmptyMission() {
        String beginTime = et_stop_car_item_start_time3.getText().toString().trim();
        String endTime = et_stop_car_item_end_time3.getText().toString().trim();
        String count = et_stop_car_item_count3.getText().toString().trim();
        String km = et_stop_car_item_km3.getText().toString().trim();
        String remarks = et_stop_car_item_remarks3.getText().toString().trim();
        if (isEmpty(beginTime, "请填写预计开始时间")) return false;
        if (isEmpty(endTime, "请填写预计结束时间")) return false;
        if (isEmpty(count, "请填写折算单次")) return false;
        if (isEmpty(km, "请填写里程")) return false;
        if (isEmpty(remarks,"请填写备注")) return false;
        if (beginTime.length() != 4 || endTime.length() != 4){
            ToastHelper.showToast("请输入正确的时间");
            return false;
        }
        int selectedItemPosition = sp_stop_car_item3.getSelectedItemPosition();
        int taskId = taskContents3.get(selectedItemPosition).getTaskId();
        mListener.onClickOperatorNotEmptyMission(currentCategory, taskId, beginTime, endTime, count, km,remarks, loadDataStatus);
        return true;
    }

    private boolean onClickOperatorEmptyMission() {
        String beginTime = et_stop_car_item_start_time2.getText().toString().trim();
        String endTime = et_stop_car_item_end_time2.getText().toString().trim();
        String count = et_stop_car_item_count2.getText().toString().trim();
        String km = et_stop_car_item_km2.getText().toString().trim();
        String remarks = et_stop_car_item_remarks2.getText().toString().trim();
        if (isEmpty(beginTime, "请填写预计开始时间")) return false;
        if (isEmpty(endTime, "请填写预计结束时间")) return false;
        if (isEmpty(count, "请填写折算单次")) return false;
        if (isEmpty(km, "请填写里程")) return false;
        if (isEmpty(remarks,"请填写备注")) return false;
        if (beginTime.length() != 4 || endTime.length() != 4){
            ToastHelper.showToast("请输入正确的时间");
            return false;
        }
        int selectedItemPosition = sp_stop_car_item2.getSelectedItemPosition();
        int taskId = taskContents2.get(selectedItemPosition).getTaskId();
        mListener.onClickOperatorEmptyMission(currentCategory, taskId, beginTime, endTime, count, km,remarks, loadDataStatus);
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
        mListener.onClickNormalMission(currentCategory, taskContentBean.getTaskId(), loadDataStatus);
        return true;
    }

    private void generateOperatorNotEmptyView(MissionType missionType, LinearLayout item3) {
        sp_stop_car_item3 = (Spinner) item3.findViewById(sp_stop_car_item);
        et_stop_car_item_start_time3 = (EditText) item3.findViewById(R.id.et_stop_car_item_start_time);
        et_stop_car_item_end_time3 = (EditText) item3.findViewById(R.id.et_stop_car_item_end_time);
        et_stop_car_item_count3 = (EditText) item3.findViewById(R.id.et_stop_car_item_count);
        et_stop_car_item_km3 = (EditText) item3.findViewById(R.id.et_stop_car_item_km);
        et_stop_car_item_remarks3 = (EditText) item3.findViewById(R.id.et_stop_car_item_remarks);
        et_stop_car_item_remarks3.setHint(mContext.getResources().getString(R.string.hint_not_operate_empty_dialog));
        taskContents3 = missionType.getTaskContent();

        MySpinnerAdapter notOperatorEmptyAdapter = new MySpinnerAdapter(mContext, taskContents3);
        sp_stop_car_item3.setAdapter(notOperatorEmptyAdapter);
    }

    private void generateOperatorEmptyView(MissionType missionType, LinearLayout item2) {
        sp_stop_car_item2 = (Spinner) item2.findViewById(sp_stop_car_item);
        et_stop_car_item_start_time2 = (EditText) item2.findViewById(R.id.et_stop_car_item_start_time);
        et_stop_car_item_end_time2 = (EditText) item2.findViewById(R.id.et_stop_car_item_end_time);
        et_stop_car_item_count2 = (EditText) item2.findViewById(R.id.et_stop_car_item_count);
        et_stop_car_item_km2 = (EditText) item2.findViewById(R.id.et_stop_car_item_km);
        et_stop_car_item_remarks2 = (EditText) item2.findViewById(R.id.et_stop_car_item_remarks);
        et_stop_car_item_remarks2.setHint(mContext.getResources().getString(R.string.hint_operate_empty_dialog));
        taskContents2 = missionType.getTaskContent();
// 建立Adapter并且绑定数据源

        MySpinnerAdapter notOperatorEmptyAdapter = new MySpinnerAdapter(mContext, taskContents2);
        sp_stop_car_item2.setAdapter(notOperatorEmptyAdapter);
    }

    private void generateNormalView(MissionType missionType, final RadioGroup rg_stop_car_item1) {
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
            if (lineId == taskContents.get(i).getTaskId())
                rg_stop_car_item1.check(radioButton.getId());
            radioButtonList.add(radioButton);
        }
    }

    public interface OnClickListener {
        void onClickNormalMission(int type, int taskId, BasePresenter.LoadDataStatus loadDataStatus);

        void onClickOperatorEmptyMission(int type, int taskType, String beginTime, String endTime, String runNum, String runEmpMileage,String remarks, BasePresenter.LoadDataStatus loadDataStatus);

        void onClickOperatorNotEmptyMission(int type, int taskType, String beginTime, String endTime, String runNum, String runEmpMileage,String remarks, BasePresenter.LoadDataStatus loadDataStatus);

        void onClickHelpMission(int type, int taskId, BasePresenter.LoadDataStatus loadDataStatus);

        void onOffDuty(BasePresenter.LoadDataStatus loadDataStatus);

        void onDismiss();

    }
}
