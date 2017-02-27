package com.zxw.dispatch.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import com.zxw.dispatch.R;

import java.util.Calendar;


/**
 * author MXQ
 * create at 2017/2/24 15:11
 * email: 1299242483@qq.com
 */
public class TimePlanPickerDialog extends AlertDialog{

    private Context mContext;
    private AlertDialog sDialog;
    private NumberPicker np_hour,np_minute;
    private Button btn_confirm,btn_cancel;
    private Calendar mDate;
    private int mHour,mMinute;
    private String sHour = null;
    private String sMinute = null;
    private OnTimePickerListener mListener;
    private View container;



    public TimePlanPickerDialog(Context context,OnTimePickerListener mListener) {
        super(context);
        this.mContext = context;
        this.mListener = mListener;
        initData();
    }

    private void initData() {
        mDate = Calendar.getInstance();
        mHour = mDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mDate.get(Calendar.MINUTE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_update_plan_time);
        np_hour = (NumberPicker) findViewById(R.id.np_hour);
        np_hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_hour.setMinValue(1);
        np_hour.setValue(mHour);
        np_hour.setMaxValue(24);
        np_hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sHour = String.valueOf(newVal);
            }
        });

        np_minute = (NumberPicker) findViewById(R.id.np_minute);
        np_minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_minute.setMinValue(0);
        np_minute.setValue(mMinute);
        np_minute.setMaxValue(59);
        np_minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
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
                   mListener.onTimePicker(sHour,sMinute);
                   dismiss();
            }
        });
    }




    public interface OnTimePickerListener{
        void onTimePicker(String sHour,String sMinute);
    }



}