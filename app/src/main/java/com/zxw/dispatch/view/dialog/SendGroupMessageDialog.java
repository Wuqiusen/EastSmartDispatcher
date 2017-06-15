package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zxw.data.bean.LineParams;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

/**
 * author：CangJie on 2017/3/2 17:45
 * email：cangjie2016@gmail.com
 *
 */
public class SendGroupMessageDialog extends AlertDialog.Builder {
    private final Context mContext;
    private final OnSendGroupMessageDialogListener mListener;
    private AlertDialog dialog;
    private EditText et_message;

    public SendGroupMessageDialog(Context context, OnSendGroupMessageDialogListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mListener = listener;
        init(context);
    }

    private void init(Context context) {
        View container = View.inflate(context, R.layout.dialog_send_group_message, null);
        et_message = (EditText) container.findViewById(R.id.et_message);

        Button btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) container.findViewById(R.id.btn_cancel);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()){
                    String message = et_message.getText().toString().trim();
                    if (TextUtils.isEmpty(message)){
                        ToastHelper.showToast("请输入文字");
                        return;
                    }
                    if (mListener != null)
                        mListener.sendGroupMessage(message);
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
        dialog.setCancelable(false);
    }

    public interface OnSendGroupMessageDialogListener {
        void sendGroupMessage(String message);
    }
}
