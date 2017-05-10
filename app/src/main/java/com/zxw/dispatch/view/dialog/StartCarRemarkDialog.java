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
    private RadioButton rbNormal;
    private RadioButton rbAbnormal;
    private LinearLayout llAbnormalContent;
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
        rbNormal = (RadioButton) view.findViewById(R.id.rb_normal);
        rbAbnormal = (RadioButton) view.findViewById(R.id.rb_abnormal);
        llAbnormalContent = (LinearLayout) view.findViewById(R.id.ll_abnormal_content);
        etRemarksOnce = (EditText) view.findViewById(R.id.et_remarks_once);//单次
        etRemarksMileage = (EditText) view.findViewById(R.id.et_remarks_mileage);//里程
        etRemarksEmptyMileage = (EditText) view.findViewById(R.id.et_remarks_empty_mileage);//空驶里程
        etRemarks = (EditText) view.findViewById(R.id.et_remarks);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        if (mRemark != null && !TextUtils.isEmpty(mRemark)){
            etRemarks.setText(mRemark);
            etRemarks.setSelection(mRemark.length());
        }
        if (mStatus == 1){
            rbNormal.setChecked(true);
            llAbnormalContent.setVisibility(View.GONE);
        }else {
            rbAbnormal.setChecked(true);
            llAbnormalContent.setVisibility(View.VISIBLE);
            // 以下暂无显示: 单次、里程、空驶里程的旧信息
        }

        rbNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAbnormalContent.setVisibility(View.GONE);
            }
        });
        rbAbnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAbnormalContent.setVisibility(View.VISIBLE);
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mStatus = 1;
                if (rbAbnormal.isChecked()) {
                    if (TextUtils.isEmpty(etRemarksOnce.getText().toString().trim())){
                        ToastHelper.showToast("请输入实跑单次");
                        return;
                    }
                    if (TextUtils.isEmpty(etRemarksMileage.getText().toString().trim())){
                        ToastHelper.showToast("请输入里程");
                        return;
                    }
                    if (TextUtils.isEmpty(etRemarksEmptyMileage.getText().toString().trim())){
                        ToastHelper.showToast("请输入空驶里程");
                        return;
                    }
                    if (TextUtils.isEmpty(etRemarks.getText().toString().trim())){
                        ToastHelper.showToast("请备注线路运营的具体情况");
                        return;
                    }
                    mStatus = 2;
                    mListener.goneCarRemarks(mObjId,mStatus,etRemarks.getText().toString()); // 暂无填单次、里程、空驶里程
                }

                if (rbNormal.isChecked()){
                    if (TextUtils.isEmpty(etRemarks.getText().toString().trim())){
                        ToastHelper.showToast("请备注线路运营的具体情况");
                        return;
                    }
                    mStatus = 1;
                    mListener.goneCarRemarks(mObjId,mStatus,etRemarks.getText().toString());
                }

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
        void goneCarRemarks(int objId,int status,String remarks);
    }
}
