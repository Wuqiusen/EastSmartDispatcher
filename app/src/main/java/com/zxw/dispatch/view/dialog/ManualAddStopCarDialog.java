package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.zxw.data.bean.LineParams;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

/**
 * author：CangJie on 2017/3/2 17:45
 * email：cangjie2016@gmail.com
 */
public class ManualAddStopCarDialog extends AlertDialog.Builder {
    private final Context mContext;
    private final LineParams mLineParams;
    private final OnManualAddStopCarListener mListener;
    private SmartEditText et_car_code;
    private SmartEditText et_driver;
    private SmartEditText et_steward;
    private AlertDialog dialog;

    public ManualAddStopCarDialog(Context context, LineParams lineParams, OnManualAddStopCarListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mLineParams = lineParams;
        this.mListener = listener;
        init(context);

    }

    private void init(Context context) {
        View container = View.inflate(context, R.layout.dialog_manual_add_car, null);
        et_car_code = (SmartEditText) container.findViewById(R.id.et_car_code);
        et_driver = (SmartEditText) container.findViewById(R.id.et_driver);
        et_steward = (SmartEditText) container.findViewById(R.id.et_steward);

        initSmartEditText();

        Button btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) container.findViewById(R.id.btn_cancel);

        // 是否需要显示乘务员输入框
        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO){
            et_steward.setVisibility(View.GONE);
        }else{
            et_steward.setVisibility(View.VISIBLE);
        }
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()){
                    String carId = String.valueOf(et_car_code.getVehicleInfo().getVehicleId());
                    String driverId = String.valueOf(et_driver.getPeopleInfo().personId);
                    String stewardId = String.valueOf(et_steward.getPeopleInfo().personId);
                    mListener.manualAddStopCar(carId, driverId, stewardId);
                    dialog.dismiss();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog = setView(container).show();
    }

    private void initSmartEditText() {
        et_car_code.addQueryCarCodeEditTextListener();
        et_driver.addQueryDriverEditTextListener();
        et_steward.addQueryTrainManEditTextListener();
    }

    public interface OnManualAddStopCarListener {
        void manualAddStopCar(String carId, String driverId, String stewardId);
    }
}
