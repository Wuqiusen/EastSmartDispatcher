package com.zxw.dispatch.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zxw.data.bean.DepartCar;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

import java.util.List;

/**
 * author：CangJie on 2016/10/9 14:46
 * email：cangjie2016@gmail.com
 *
 */
public class SendCarDialog extends AlertDialog.Builder {
    private Context mContext;
    private AlertDialog show;
    private TextView tv_title;
    private SmartEditText et_car_code;
    private SmartEditText et_driver;
    private SmartEditText et_trainman;
    private EditText et_plan_time;
    private EditText et_interval_time;
    private EditText et_enter_time;
    private Button btn_confirm;
    private View container;

    public SendCarDialog(Context context) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        init(context);
    }

    private void init(Context context) {
        container = View.inflate(context, R.layout.dialog_alter, null);
        tv_title = (TextView) container.findViewById(R.id.tv_title);
        et_car_code = (SmartEditText) container.findViewById(R.id.et_car_code);
        et_driver = (SmartEditText) container.findViewById(R.id.et_driver);
        et_trainman = (SmartEditText) container.findViewById(R.id.et_trainman);
        et_plan_time = (EditText) container.findViewById(R.id.et_plan_time);
        et_interval_time = (EditText) container.findViewById(R.id.et_interval_time);
        et_enter_time = (EditText) container.findViewById(R.id.et_enter_time);
        btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        Button btn_cancel =  (Button) container.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_title.setText("修改信息");
        this.setView(container);
    }

    private void modifyWindowParams(){
        Window dialogWindow = show.getWindow();
        WindowManager m = ((Activity)mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
    }

    public SendCarDialog change(final List<DepartCar> mDatas, final int position, final OnDialogChangeConfirmListener listener){
//        et_car_code.setVehcile(mDatas.get(position).getCode(), mDatas.get(position).ge);
//        et_driver.setPerson(mDatas.get(position).sjName, mDatas.get(position).sjId, SmartEditText.DRIVER);
//        et_trainman.setPerson(mDatas.get(position).scName, mDatas.get(position).scId, SmartEditText.TRAINMAN);
//        initEditTextListener();
//        et_plan_time.setText(mDatas.get(position).getArriveTime());
//        et_interval_time.setText(mDatas.get(position).getSpaceTime());
//        et_enter_time.setText(mDatas.get(position).inTime2);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vehId = et_car_code.getInfoId();
                int driverId = et_driver.getInfoId();
                String trainmanId = "";
                if(et_trainman.getInfoId() != -1){
                    trainmanId = String.valueOf(et_trainman.getInfoId());
                }

                if (vehId == -1) {
                    ToastHelper.showToast("请填写正确的车牌号", mContext);
                    return;
                }
                if (driverId == -1) {
                    ToastHelper.showToast("请填写驾驶员名称", mContext);
                    return;
                }

                String planTime = et_plan_time.getText().toString().trim();
                if (TextUtils.isEmpty(planTime)) {
                    ToastHelper.showToast("请填写计划发车时间", mContext);
                    return;
                }
                if (planTime.length() != 4) {
                    ToastHelper.showToast("请填写正确的计划发车时间", mContext);
                    return;
                }
                String intervalTime = et_interval_time.getText().toString().trim();
                if (TextUtils.isEmpty(intervalTime)) {
                    ToastHelper.showToast("请填写发车间隔时间", mContext);
                    return;
                }
                String enterTime = et_enter_time.getText().toString().trim();
                if (!TextUtils.isEmpty(enterTime))
                    if(enterTime.length() != 4){
                        ToastHelper.showToast("请填写正确的人工进场时间", mContext);
                        return;
                    }

                listener.onDialogChangeConfirm(mDatas.get(position).getId(), vehId, driverId, trainmanId, planTime, Integer.valueOf(intervalTime), enterTime);
                dismiss();
            }
        });
        return this;
    }

    private void initEditTextListener() {
        et_car_code.addQueryCarCodeEditTextListener();
        et_driver.addQueryDriverEditTextListener();
        et_trainman.addQueryTrainManEditTextListener();
    }

    public SendCarDialog create( final OnDialogCreateConfirmListener listener){
        initEditTextListener();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int vehId = et_car_code.getInfoId();
                int driverId = et_driver.getInfoId();
                String trainmanId = "";
                if(et_trainman.getInfoId() != -1){
                    trainmanId = String.valueOf(et_trainman.getInfoId());
                }

                if (vehId == -1) {
                    ToastHelper.showToast("请填写正确的车牌号", mContext);
                    return;
                }
                if (driverId == -1) {
                    ToastHelper.showToast("请填写驾驶员名称", mContext);
                    return;
                }

                String planTime = et_plan_time.getText().toString().trim();
                if (TextUtils.isEmpty(planTime) || planTime.length() != 4) {
                    ToastHelper.showToast("请填写计划发车时间", mContext);
                    return;
                }
                String intervalTime = et_interval_time.getText().toString().trim();
                if (TextUtils.isEmpty(intervalTime)) {
                    ToastHelper.showToast("请填写发车间隔时间", mContext);
                    return;
                }
                String enterTime = et_enter_time.getText().toString().trim();
                if (TextUtils.isEmpty(enterTime) || enterTime.length() != 4) {
                    ToastHelper.showToast("请填写人工进场时间", mContext);
                    return;
                }
                listener.onDialogCreateConfirm(vehId, driverId, trainmanId, planTime, Integer.valueOf(intervalTime), enterTime, null);
                dismiss();
            }
        });
        return this;
    }


    public void dialogShow(){
        show = this.show();
        modifyWindowParams();
    }

    public void dismiss(){
        if(show != null && show.isShowing())
            show.dismiss();
    }

    public interface OnDialogChangeConfirmListener {
        void onDialogChangeConfirm(int opId, int vehId,
                                   int sjId, String scId, String projectTime,
                                   int spaceMin, String inTime2);
    }
    public interface OnDialogCreateConfirmListener {
        void onDialogCreateConfirm(int vehId, int sjId, String scId, String projectTime, int spaceMin, String inTime2, String sortNum);
    }

    public void setOnLoadValueListener(SmartEditText.OnLoadValueListener listener){
        et_car_code.setOnLoadValueListener(listener);
    }
}
