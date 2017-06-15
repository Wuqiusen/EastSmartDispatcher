package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.ToastHelper;

/**
 * author MXQ
 * create at 2017/5/5 11:25
 * email: 1299242483@qq.com
 */
public class StartCarRemarkDialog extends AlertDialog.Builder {
    private Context mContext;
    private EditText etRemarksOnce;
    private EditText etRemarksMileage;
    private EditText etRemarksEmptyMileage;
    private EditText etRemarks;
    private Button btn_confirm;
    private Button btn_cancel;

    private int mObjId;
    private int mStatus;
    private String mRemark;
    private AlertDialog mDialog;
    private OnStartCarRemarkListener mListener;

    public StartCarRemarkDialog(Context context, int objId, int status, String remark, OnStartCarRemarkListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mObjId = objId;
        this.mStatus = status;
        this.mRemark = remark;
        this.mListener = listener;
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context,R.layout.view_start_car_remarks_dialog,null);
        etRemarksOnce = (EditText) view.findViewById(R.id.et_remarks_once);//单次
        etRemarksMileage = (EditText) view.findViewById(R.id.et_remarks_mileage);//营运里程
        etRemarksEmptyMileage = (EditText) view.findViewById(R.id.et_remarks_empty_mileage);//空驶里程
        etRemarks = (EditText) view.findViewById(R.id.et_remarks);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        if (mRemark != null && !TextUtils.isEmpty(mRemark)){
            etRemarks.setText(mRemark);
            etRemarks.setSelection(mRemark.length());
        }

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String runNum = etRemarksOnce.getText().toString().trim();
                    if (TextUtils.isEmpty(runNum)){
                        ToastHelper.showToast("请输入实跑单次");
                        return;
                    }
                    String runMileage = etRemarksMileage.getText().toString().trim();
                    if (TextUtils.isEmpty(runMileage)){
                        ToastHelper.showToast("请输入营运里程");
                        return;
                    }
                    String runEmpMileage = etRemarksEmptyMileage.getText().toString().trim();
                    if (TextUtils.isEmpty(runEmpMileage)){
                        ToastHelper.showToast("请输入空驶里程");
                        return;
                    }
                    String remarks = etRemarks.getText().toString().trim();
                if (remarks.length() > 100){

                    ToastHelper.showToast("备注字数过多，当前字数为："  + remarks.length());
                    return;
                }

                    int mStatus = 2;
                    // 异常
                    mListener.goneCarAbNormalRemarks(mObjId,mStatus,remarks,Integer.parseInt(runNum)
                            ,Double.parseDouble(runMileage),Double.parseDouble(runEmpMileage));
                mDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog != null && mDialog.isShowing()){
                    mDialog.dismiss();
                }
            }
        });

        mDialog = setView(view).show();
        mDialog.setCancelable(false);
    }

    public interface OnStartCarRemarkListener{
        void goneCarNormalRemarks(int objId,int status,String remarks);

        void goneCarAbNormalRemarks(int mObjId, int mStatus, String remarks, int runNum, double runMileage, double runEmpMileage);
    }
}
