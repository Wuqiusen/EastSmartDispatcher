package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.zxw.data.bean.StopCarCodeBean;
import com.zxw.data.http.HttpInterfaces;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.BasePresenter;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.WaitCarCodeUpdateAdapter;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;

import java.util.List;

import rx.Subscriber;

/**
 * author MXQ
 * create at 2017/4/28 09:21
 * email: 1299242483@qq.com
 */
public class UpdateWaitCarCodeDialog extends AlertDialog.Builder implements View.OnClickListener{
    private Context mContext;
    private RecyclerView rv_car_code;
    private Button btn_confirm;
    private Button btn_cancel;
    private AlertDialog dialog;
    private OnListener mListener;
    private StopCarCodeBean mStopCarCodeBean;
    private int mLineId;
    private BasePresenter.LoadDataStatus loadDataStatus;


    public UpdateWaitCarCodeDialog(Context context,int lineId,OnListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mLineId = lineId;
        this.mListener = listener;
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context,R.layout.dialog_update_wait_car_code,null);
        rv_car_code = (RecyclerView) view.findViewById(R.id.rv_car_code);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        loadStopCarCodeList();
        btn_cancel.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);

        dialog = setView(view).show();
        dialog.setCancelable(true);

    }



    // 获取停场车辆
    public void loadStopCarCodeList() {
        HttpMethods.getInstance().getStopCarList(new Subscriber<List<StopCarCodeBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastHelper.showToast(e.getMessage());
            }

            @Override
            public void onNext(List<StopCarCodeBean> stopCarCodeBeen) {
                WaitCarCodeUpdateAdapter mAdapter = new WaitCarCodeUpdateAdapter(mContext, stopCarCodeBeen, new WaitCarCodeUpdateAdapter.OnListener() {
                    @Override
                    public void onUpdateWaitCarCode(StopCarCodeBean bean) {
                        mStopCarCodeBean = bean;

                    }
                });
                GridLayoutManager layoutManager = new GridLayoutManager(mContext,4);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_car_code.setLayoutManager(layoutManager);
                rv_car_code.setHasFixedSize(true);
                rv_car_code.setAdapter(mAdapter);
                rv_car_code.addItemDecoration(new DividerItemDecoration(mContext,R.color.white
                        ,DividerItemDecoration.VERTICAL_LIST));

            }
        },SpUtils.getCache(mContext,SpUtils.USER_ID),SpUtils.getCache(mContext,SpUtils.KEYCODE),mLineId+"");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                if (mStopCarCodeBean == null){
                    ToastHelper.showToast("请选择更改的车辆号!");
                    return;
                }
                btn_confirm.setClickable(false);
                loadDataStatus = new BasePresenter.LoadDataStatus() {
                    @Override
                    public void OnLoadDataFinish() {
                        btn_confirm.setClickable(true);
                    }
                };
                mListener.onConfirmChangeCarCode(mStopCarCodeBean, loadDataStatus);
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                if (dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
                break;
        }
    }


    public interface OnListener{
        void onConfirmChangeCarCode(StopCarCodeBean bean, BasePresenter.LoadDataStatus loadDataStatus);
    }
}
