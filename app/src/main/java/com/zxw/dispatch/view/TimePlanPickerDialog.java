package com.zxw.dispatch.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import com.zxw.dispatch.R;


/**
 * author MXQ
 * create at 2017/2/24 15:11
 * email: 1299242483@qq.com
 */
public class TimePlanPickerDialog extends AlertDialog {

    private Context mContext;
    private AlertDialog sDialog;
    private NumberPicker np_hour,np_minute;
    private Button btn_confirm,btn_cancel;
    private String sHour = null;
    private String sMinute = null;
    private int currentHour;
    private int currentMinute;
    private OnTimePickerListener mListener;
    private String mVehTime;



    public TimePlanPickerDialog(Context context,String vehTime,OnTimePickerListener mListener) {
        super(context);
        this.mContext = context;
        this.mListener = mListener;
        this.mVehTime = vehTime;
        currentHour = Integer.parseInt(mVehTime.substring(0,2));
        currentMinute =  Integer.parseInt(mVehTime.substring(2,4));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_update_plan_time);
        np_hour = (NumberPicker) findViewById(R.id.np_hour);
        np_hour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_hour.setMinValue(1);
        np_hour.setMaxValue(24);

        np_hour.setValue(currentHour);
        np_hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sHour = String.valueOf(newVal);
                if (sHour == null) {
                    sHour = String.valueOf(Integer.parseInt(mVehTime.substring(0,2)));
                }
                if (sHour.trim().length() == 1) {
                    sHour = "0"+ sHour;
                }
            }
        });

        np_minute = (NumberPicker) findViewById(R.id.np_minute);
        np_minute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np_minute.setMinValue(0);
        np_minute.setMaxValue(59);

        np_minute.setValue(currentMinute);
        np_minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sMinute = String.valueOf(newVal);
                if (sMinute.trim().length() == 1) {
                    sMinute = "0"+ sMinute;
                }

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
                if (sHour == null) {
                    sHour = String.valueOf(Integer.parseInt(mVehTime.substring(0,2)));
                }
                if (sMinute == null) {
                    sMinute = String.valueOf(Integer.parseInt(mVehTime.substring(2,4)));
                }
                mListener.onTimePicker(sHour,sMinute);
                dismiss();
            }
        });
    }




    public interface OnTimePickerListener{
        void onTimePicker(String sHour,String sMinute);
    }



}