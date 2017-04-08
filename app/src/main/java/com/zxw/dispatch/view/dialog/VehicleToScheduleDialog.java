package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
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
import com.zxw.dispatch.adapter.CommonAdapter;
import com.zxw.dispatch.adapter.ViewHolder;
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
 * create at 2017/3/10 11:33
 * email: 1299242483@qq.com
 */
public class VehicleToScheduleDialog extends AlertDialog.Builder{
    private final int lineId;
    private Context mContext;
    private StopHistory mStopCar;
//    private TextView tv_carCode;
    private AlertDialog dialog;
    private OnClickListener mListener;
    private RadioGroup rg_stop_car_item1;
    private RadioGroup rg_category;
    private RadioButton rb_normal,rb_operator_empty, rb_operator_not_empty, rb_help;
    private int currentCategory = 1;
    private int taskType2, taskType3;
    private boolean isFirstChooseTaskType2 = true, isFirstChooseTaskType3 = true;
    private EditText et_stop_car_item_start_time2, et_stop_car_item_start_time3;
    private EditText et_stop_car_item_end_time2, et_stop_car_item_end_time3;
    private EditText et_stop_car_item_count2, et_stop_car_item_count3;
    private EditText et_stop_car_item_km2, et_stop_car_item_km3;
    private HashMap<RadioButton, MissionType.TaskContentBean> normalTaskIdMap;
    private SmartEditText seLine;
    private List<RadioButton> radioButtonList;

    View.OnFocusChangeListener operatorEmptyFocusListener2 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                chooseCategoryRadioButton(2);
        }
    };
    View.OnFocusChangeListener operatorEmptyFocusListener3 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                chooseCategoryRadioButton(3);
        }
    };

    public VehicleToScheduleDialog(Context context,StopHistory stopCar,OnClickListener listener, int lineId) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mStopCar = stopCar;
        this.mListener = listener;
        this.lineId = lineId;
        loadMissionTypeByRemote();////////
//        init();
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
                SpUtils.getCache(mContext, SpUtils.KEYCODE), lineId + "");
    }

    private void init(List<MissionType> missionTypes) {
        View  container= View.inflate(mContext,R.layout.dialog_stop_car,null);
        TextView tv_carCode = (TextView) container.findViewById(R.id.tv_carCode);
        tv_carCode.setText("车辆任务 （"+mStopCar.code+"）");
        rg_category = (RadioGroup) container.findViewById(R.id.rg_category);
        rb_normal = (RadioButton) container.findViewById(R.id.rb_normal);
        rb_operator_empty = (RadioButton) container.findViewById(R.id.rb_operator_empty);
        rb_operator_not_empty = (RadioButton) container.findViewById(R.id.rb_operator_not_empty);
        rb_help = (RadioButton) container.findViewById(R.id.rb_help);
        rg_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_normal:
                        currentCategory = 1;
                        break;
                    case R.id.rb_operator_empty:
                        currentCategory = 2;
                        break;
                    case R.id.rb_operator_not_empty:
                        currentCategory = 3;
                        break;
                    case R.id.rb_help:
                        currentCategory = 4;
                        break;
                }
            }
        });
        rg_stop_car_item1 = (RadioGroup) container.findViewById(R.id.rg_stop_car_item1);
        LinearLayout item2 = (LinearLayout) container.findViewById(R.id.item2);
        LinearLayout item3 = (LinearLayout) container.findViewById(R.id.item3);
        seLine = (SmartEditText) container.findViewById(R.id.se_line);
        seLine.addQueryLineEditTextListener(lineId + "");
        seLine.setOnLoadValueListener(new SmartEditText.OnLoadValueListener() {
            @Override
            public void onLoadValue() {
                chooseCategoryRadioButton(4);
            }
        });

        for (MissionType missionType : missionTypes){
            switch (missionType.getType()){
                case 1:
                    generateNormalView(missionType, rg_stop_car_item1);
                    break;
                case 2:
                    generateOperatorEmptyView(missionType, item2);
                    break;
                case 3:
                    generateOperatorNotEmptyView(missionType, item3);
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
                switch (currentCategory){
                    case 1:
                        if(onClickNormalMission())
                            dialog.dismiss();
                        break;
                    case 2:
                        if(onClickOperatorEmptyMission())
                        dialog.dismiss();
                        break;
                    case 3:
                        if(onClickOperatorNotEmptyMission())
                        dialog.dismiss();
                        break;
                    case 4:
                        if(onClickHelpMission())
                        dialog.dismiss();
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
        container.findViewById(R.id.btn_off_duty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    mListener.onOffDuty();
                    dialog.dismiss();
                }
            }
        });
        dialog = setView(container).show();
    }

    private boolean onClickHelpMission() {
        try{
            int lineId = seLine.getLineInfo().lineId;
            if (lineId == 0) {
                return false;
            }
            mListener.onClickHelpMission(currentCategory, lineId);
        }catch (Exception e){
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
        if (isEmpty(beginTime, "请填写预计开始时间")) return false;
        if (isEmpty(endTime, "请填写预计结束时间")) return false;
        if (isEmpty(count, "请填写折算单次")) return false;
        if (isEmpty(km, "请填写里程")) return false;
        mListener.onClickOperatorNotEmptyMission(currentCategory, taskType3, beginTime, endTime, count, km);
        return true;
    }

    private boolean onClickOperatorEmptyMission() {
        String beginTime = et_stop_car_item_start_time2.getText().toString().trim();
        String endTime = et_stop_car_item_end_time2.getText().toString().trim();
        String count = et_stop_car_item_count2.getText().toString().trim();
        String km = et_stop_car_item_km2.getText().toString().trim();
        if (isEmpty(beginTime, "请填写预计开始时间")) return false;
        if (isEmpty(endTime, "请填写预计结束时间")) return false;
        if (isEmpty(count, "请填写折算单次")) return false;
        if (isEmpty(km, "请填写里程")) return false;
        mListener.onClickOperatorEmptyMission(currentCategory, taskType2, beginTime, endTime, count, km);
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
        for (RadioButton radioButton : radioButtonList){
            if (radioButton.isChecked()){
                taskContentBean = normalTaskIdMap.get(radioButton);
                break;
            }
        }
        if (taskContentBean == null){
            ToastHelper.showToast("请选择线路");
            return false;
        }
        mListener.onClickNormalMission(currentCategory, taskContentBean.getTaskId());
        return true;
    }

    private void generateOperatorNotEmptyView(MissionType missionType, LinearLayout item3) {
        Spinner sp_stop_car_item3 = (Spinner) item3.findViewById(R.id.sp_stop_car_item);
        et_stop_car_item_start_time3 = (EditText) item3.findViewById(R.id.et_stop_car_item_start_time);
        et_stop_car_item_end_time3 = (EditText) item3.findViewById(R.id.et_stop_car_item_end_time);
        et_stop_car_item_count3 = (EditText) item3.findViewById(R.id.et_stop_car_item_count);
        et_stop_car_item_km3 = (EditText) item3.findViewById(R.id.et_stop_car_item_km);
        et_stop_car_item_start_time3.setOnFocusChangeListener(operatorEmptyFocusListener3);
        et_stop_car_item_end_time3.setOnFocusChangeListener(operatorEmptyFocusListener3);
        et_stop_car_item_count3.setOnFocusChangeListener(operatorEmptyFocusListener3);
        et_stop_car_item_km3.setOnFocusChangeListener(operatorEmptyFocusListener3);
        final List<MissionType.TaskContentBean> taskContents = missionType.getTaskContent();
// 建立Adapter并且绑定数据源
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(mContext, R.layout.dialog_stop_car_spinner_item, taskContents);
//绑定 Adapter到控件
        sp_stop_car_item3.setAdapter(spinnerAdapter);
        sp_stop_car_item3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(!isFirstChooseTaskType3)
                    chooseCategoryRadioButton(3);
                if(isFirstChooseTaskType3)
                    isFirstChooseTaskType3 = false;
                taskType3 = taskContents.get(pos).getTaskId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void generateOperatorEmptyView(MissionType missionType, LinearLayout item2) {
        Spinner sp_stop_car_item2 = (Spinner) item2.findViewById(R.id.sp_stop_car_item);
        et_stop_car_item_start_time2 = (EditText) item2.findViewById(R.id.et_stop_car_item_start_time);
        et_stop_car_item_end_time2 = (EditText) item2.findViewById(R.id.et_stop_car_item_end_time);
        et_stop_car_item_count2 = (EditText) item2.findViewById(R.id.et_stop_car_item_count);
        et_stop_car_item_km2 = (EditText) item2.findViewById(R.id.et_stop_car_item_km);
        et_stop_car_item_start_time2.setOnFocusChangeListener(operatorEmptyFocusListener2);
        et_stop_car_item_end_time2.setOnFocusChangeListener(operatorEmptyFocusListener2);
        et_stop_car_item_count2.setOnFocusChangeListener(operatorEmptyFocusListener2);
        et_stop_car_item_km2.setOnFocusChangeListener(operatorEmptyFocusListener2);
        final List<MissionType.TaskContentBean> taskContents = missionType.getTaskContent();
// 建立Adapter并且绑定数据源
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(mContext, R.layout.dialog_stop_car_spinner_item, taskContents);
//绑定 Adapter到控件
        sp_stop_car_item2.setAdapter(spinnerAdapter);
        sp_stop_car_item2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                if(!isFirstChooseTaskType2)
                    chooseCategoryRadioButton(2);
                if(isFirstChooseTaskType2)
                    isFirstChooseTaskType2 = false;
                taskType2 = taskContents.get(pos).getTaskId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void chooseCategoryRadioButton(int position) {
        switch (position){
            case 1:
                rb_normal.setChecked(true);
                break;
            case 2:
                rb_operator_empty.setChecked(true);
                clearNormalRadioButtonFlag();
                break;
            case 3:
                rb_operator_not_empty.setChecked(true);
                clearNormalRadioButtonFlag();
                break;
            case 4:
                rb_help.setChecked(true);
                clearNormalRadioButtonFlag();
                break;
        }
    }

    private void clearNormalRadioButtonFlag(){
//        for(RadioButton radioButton : radioButtonList)
//            radioButton.setChecked(false);
    }


    private void generateNormalView(MissionType missionType, RadioGroup rg_stop_car_item1) {
        if(normalTaskIdMap == null){
            normalTaskIdMap = new HashMap<>();
        }else{
            normalTaskIdMap.clear();
        }
        if (radioButtonList == null){
            radioButtonList = new ArrayList<>();
        }else{
            radioButtonList.clear();
        }
        final List<MissionType.TaskContentBean> taskContents = missionType.getTaskContent();
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < taskContents.size(); i++){
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setText(taskContents.get(i).getTaskName());
            normalTaskIdMap.put(radioButton, taskContents.get(i));
            radioButtonList.add(radioButton);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    chooseCategoryRadioButton(1);
                }
            });
            rg_stop_car_item1.addView(radioButton, i, layoutParams);
        }
    }

    public interface OnClickListener{
         void onClickNormalMission(int type, int taskId);
         void onClickOperatorEmptyMission(int type, int taskType, String beginTime, String endTime, String runNum, String runEmpMileage);
         void onClickOperatorNotEmptyMission(int type, int taskType, String beginTime, String endTime, String runNum, String runEmpMileage);
         void onClickHelpMission(int type, int taskId);
         void onOffDuty();
    }

    public class SpinnerAdapter extends CommonAdapter<MissionType.TaskContentBean>{

        public SpinnerAdapter(Context context, int layoutId, List<MissionType.TaskContentBean> items) {
            super(context, layoutId, items);
        }

        @Override
        protected void convert(ViewHolder holder, int position, MissionType.TaskContentBean item) {
            TextView tv_item = holder.getView(R.id.tv_item);
            tv_item.setText(item.getTaskName());
        }
    }
}
