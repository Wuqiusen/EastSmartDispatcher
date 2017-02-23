package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxw.data.bean.PersonInfo;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.R;
import com.zxw.dispatch.recycler.DialogDoublePeopleAdapter;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2017/2/23 10:29
 * email：cangjie2016@gmail.com
 */
public class AlertNameDialog extends AlertDialog.Builder {
    private final Context mContext;
    private TextView origin_name;
    private RecyclerView rv_name;
    private Button btn_switch;
    private SmartEditText et_fuzzy_query;
    private Button btn_confirm;
    private Button btn_cancel;
    public static final int TYPE_DRIVER = 1, TYPE_STEWARD = 2;
    private int currentType = TYPE_DRIVER;
    private PersonInfo mPersonInfo;
    private OnAlertDriverListener onAlertDriverListener;
    private OnAlertStewardListener onAlertStewardListener;
    private AlertDialog dialog;

    public AlertNameDialog(Context context) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        init(context);
    }

    private void init(Context context) {
        View container = View.inflate(context, R.layout.dialog_alert_name, null);
        origin_name = (TextView) container.findViewById(R.id.origin_name);
        rv_name = (RecyclerView) container.findViewById(R.id.rv_name);
        btn_switch = (Button) container.findViewById(R.id.btn_switch);
        et_fuzzy_query = (SmartEditText) container.findViewById(R.id.et_fuzzy_query);
        btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) container.findViewById(R.id.btn_cancel);
        rv_name.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rv_name.addItemDecoration(new DividerItemDecoration(mContext, R.color.white,
                DividerItemDecoration.HORIZONTAL_LIST));
        btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_fuzzy_query.getVisibility() == View.VISIBLE) {
                    et_fuzzy_query.setVisibility(View.GONE);
                    et_fuzzy_query.clear();
                } else {
                    et_fuzzy_query.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPersonInfo == null && et_fuzzy_query.getPeopleInfo() == null){
                    ToastHelper.showToast("请确定修改信息");
                    return;
                }
                // 如果模糊查询有值时,即采用模糊查询出来的结果
                if (et_fuzzy_query.getPeopleInfo() != null)
                    mPersonInfo = et_fuzzy_query.getPeopleInfo();
                if (currentType == TYPE_DRIVER) {
                    onAlertDriverListener.onAlertDriverListener(mPersonInfo.personId);
                } else if (currentType == TYPE_STEWARD) {
                    onAlertStewardListener.onAlertStewardListener(mPersonInfo.personId);
                }
                dialog.dismiss();
            }
        });
        this.setView(container);
    }

    public void showDriverDialog(int objId, String driverName, OnAlertDriverListener listener) {
        currentType = TYPE_DRIVER;
        et_fuzzy_query.addQueryDriverEditTextListener();
        origin_name.setText(driverName);
        String userId = SpUtils.getCache(mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);
        loadDoublePeople(userId, keyCode, objId);
        this.onAlertDriverListener = listener;
        dialog = show();
    }

    public void showStewardDialog(int objId, String driverName, OnAlertStewardListener listener) {
        currentType = TYPE_STEWARD;
        et_fuzzy_query.addQueryTrainManEditTextListener();
        origin_name.setText(driverName);
        String userId = SpUtils.getCache(mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);
        loadDoublePeople(userId, keyCode, objId);
        this.onAlertStewardListener = listener;
        dialog = show();
    }

    public interface OnAlertDriverListener {
        void onAlertDriverListener(int driverId);
    }

    public interface OnAlertStewardListener {
        void onAlertStewardListener(int stewardId);
    }

    private void loadDoublePeople(String userId, String keyCode, int objId) {
        HttpMethods.getInstance().getPersonDoubleList(new Subscriber<List<PersonInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<PersonInfo> personInfos) {
                DialogDoublePeopleAdapter dialogDoublePeopleAdapter = new DialogDoublePeopleAdapter(personInfos, mContext, new DialogDoublePeopleAdapter.OnSelectedDoublePeopleListener() {
                    @Override
                    public void onSelectedDoublePeopleListener(PersonInfo info) {
                        mPersonInfo = info;
                    }
                });
                rv_name.setAdapter(dialogDoublePeopleAdapter);
            }
        }, userId, keyCode, objId, currentType);
    }

}
