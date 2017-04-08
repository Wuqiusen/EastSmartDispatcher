package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.zxw.dispatch.R;

/**
 * author：CangJie on 2017/4/7 20:36
 * email：cangjie2016@gmail.com
 */
public class StopCarEndToStayDialog  extends AlertDialog.Builder {
    private final Context mContext;
    private final OnStopCarEndToStayListener mListener;
    private AlertDialog mDialog;

    public StopCarEndToStayDialog(Context context, OnStopCarEndToStayListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mListener = listener;
        initView();
    }

    private void initView() {
        View container= View.inflate(mContext,R.layout.dialog_stop_car_end_to_stay,null);
        container.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialog == null || !mDialog.isShowing())
                    return;
                mListener.onConfirm();
                mDialog.dismiss();
            }
        });
        container.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialog == null || !mDialog.isShowing())
                    return;
                mDialog.dismiss();
            }
        });
        mDialog = setView(container).show();
    }

    public interface OnStopCarEndToStayListener{
        void onConfirm();
    }
}
