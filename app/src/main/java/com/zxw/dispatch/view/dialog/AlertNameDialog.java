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
import com.zxw.dispatch.utils.DebugLog;
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
    private TextView dialog_name;
    private TextView person_name;
    private TextView driver_name;
    private RecyclerView rv_name;
    private TextView tv_switch;
    private SmartEditText et_fuzzy_query;
    private Button btn_confirm;
    private Button btn_cancel;
    public static final int TYPE_DRIVER = 1, TYPE_STEWARD = 2;
    private int currentType = TYPE_DRIVER;
    private PersonInfo mPersonInfo;
    private OnAlertDriverListener onAlertDriverListener;
    private OnAlertStewardListener onAlertStewardListener;
    private AlertDialog dialog;
    private int mIsSingleClass;
    private String mPeopleName = null;

    public AlertNameDialog(Context context,int isSingleClass) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mIsSingleClass = isSingleClass;
        init(context);
    }

    private void init(Context context) {
        View container = View.inflate(context, R.layout.dialog_alert_name, null);
        dialog_name = (TextView) container.findViewById(R.id.dialog_name);
        person_name = (TextView) container.findViewById(R.id.person_name);
        driver_name = (TextView) container.findViewById(R.id.driver_name);
        rv_name = (RecyclerView) container.findViewById(R.id.rv_name);
        tv_switch = (TextView) container.findViewById(R.id.tv_switch);
        et_fuzzy_query = (SmartEditText) container.findViewById(R.id.et_fuzzy_query);
        btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) container.findViewById(R.id.btn_cancel);

        rv_name.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rv_name.addItemDecoration(new DividerItemDecoration(mContext, R.color.white,
                DividerItemDecoration.HORIZONTAL_LIST));

        tv_switch.setOnClickListener(new View.OnClickListener() {
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
        mPeopleName = driverName;
        et_fuzzy_query.addQueryDriverEditTextListener();
        dialog_name.setText("驾驶员修改");
        person_name.setText("驾驶员:");
        DebugLog.i(person_name+"");
        String userId = SpUtils.getCache(mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);
        if (mIsSingleClass == 0){
            rv_name.setVisibility(View.GONE);
            driver_name.setText(driverName);
            driver_name.setVisibility(View.VISIBLE);
        }else{
            rv_name.setVisibility(View.VISIBLE);
            driver_name.setVisibility(View.GONE);
        }
        loadDoublePeople(userId, keyCode, objId);
        this.onAlertDriverListener = listener;
        dialog = show();
    }

    public void showStewardDialog(int objId, String stewardName, OnAlertStewardListener listener) {
        currentType = TYPE_STEWARD;
        mPeopleName = stewardName;
        et_fuzzy_query.addQueryTrainManEditTextListener();
        dialog_name.setText("乘务员修改");
        person_name.setText("乘务员:");
        String userId = SpUtils.getCache(mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(mContext, SpUtils.KEYCODE);
        if (mIsSingleClass == 0){
            rv_name.setVisibility(View.GONE);
            driver_name.setText(stewardName);
            driver_name.setVisibility(View.VISIBLE);
        }else{
            rv_name.setVisibility(View.VISIBLE);
            driver_name.setVisibility(View.GONE);
        }
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

                DialogDoublePeopleAdapter dialogDoublePeopleAdapter = new DialogDoublePeopleAdapter(personInfos,mPeopleName, mContext, new DialogDoublePeopleAdapter.OnSelectedDoublePeopleListener() {
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
