package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.R;

/**
 * author MXQ
 * create at 2017/3/10 11:33
 * email: 1299242483@qq.com
 */
public class VehicleToScheduleDialog extends AlertDialog.Builder{
    private Context mContext;
    private StopHistory mStopCar;
    private TextView tv_carCode;
    private Button btn_confirm;
    private Button btn_cancel;
    private AlertDialog dialog;
    private OnClickListener mListener;

    public VehicleToScheduleDialog(Context context,StopHistory stopCar,OnClickListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mStopCar = stopCar;
        this.mListener = listener;
        init();
    }

    private void init() {
        View  container= View.inflate(mContext,R.layout.dialog_stop_car,null);
        tv_carCode = (TextView) container.findViewById(R.id.tv_carCode);
        tv_carCode.setText("请确认是否将"+mStopCar.code+"添加到待发车辆列表中?");
        btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()){
                    mListener.onClick();
                    dialog.dismiss();
                }
            }
        });
        btn_cancel = (Button) container.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog = setView(container).show();
    }

    public interface OnClickListener{
         void onClick();
    }
}
