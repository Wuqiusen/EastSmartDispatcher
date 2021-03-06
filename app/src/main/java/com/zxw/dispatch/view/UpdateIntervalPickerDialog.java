package com.zxw.dispatch.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.BasePresenter;

/**
 * author MXQ
 * create at 2017/3/6 15:01
 * email: 1299242483@qq.com
 */
public class UpdateIntervalPickerDialog  extends AlertDialog {

    private Context mContext;
    private NumberPicker np_space_name;
    private Button btn_confirm, btn_cancel;
    private String sMinute = null;
    private OnTimePickerListener mListener;
    private int mCurMinute;
    private BasePresenter.LoadDataStatus loadDataStatus;


    public UpdateIntervalPickerDialog(Context context, int curMinute, OnTimePickerListener mListener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mListener = mListener;
        this.mCurMinute = curMinute;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_update_car_interval_dialog);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        np_space_name = (NumberPicker) findViewById(R.id.np_space_name);
        np_space_name.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_space_name.setMinValue(0);
        np_space_name.setMaxValue(59);

        np_space_name.setValue(mCurMinute);
        np_space_name.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sMinute = String.valueOf(newVal);
            }
        });

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirm.setClickable(false);
                if (TextUtils.isEmpty(sMinute)) {
                    sMinute = String.valueOf(mCurMinute);
                }
                loadDataStatus = new BasePresenter.LoadDataStatus() {
                    @Override
                    public void OnLoadDataFinish() {
                        btn_confirm.setClickable(true);
                    }
                };
                mListener.onTimePicker(sMinute, loadDataStatus);
                dismiss();
            }
        });
    }


    public interface OnTimePickerListener {
        void onTimePicker(String sMinute, BasePresenter.LoadDataStatus loadDataStatus);
    }
}

