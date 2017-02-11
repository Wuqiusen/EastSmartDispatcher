package com.zxw.dispatch.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.MainAdapter;
import com.zxw.dispatch.recycler.SendAdapter;
import com.zxw.dispatch.recycler.StopAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.view.DragListAdapter;
import com.zxw.dispatch.view.DragListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends PresenterActivity<MainPresenter> implements MainView, MainAdapter.OnSelectLineListener {

    @Bind(R.id.rv_line)
    RecyclerView mLineRV;
    @Bind(R.id.rv_gone_car)
    RecyclerView mGoneRV;
    @Bind(R.id.rv_stop_car)
    RecyclerView mStopRV;
    @Bind(R.id.lv_send_car)
    DragListView mSendRV;
    private MainAdapter mLineAdapter;
    private AlertDialog mManualStopDialog, mStopCarDialog;
    private MsgReceiver msgReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        creatRexeiver();
        initView();
        presenter.loadLineList();
    }

    private void initView() {
        hideHeadArea();
        hideTitle();
    }

    private void creatRexeiver(){
        //动态注册广播接收器
        if (msgReceiver == null){
            msgReceiver = new MsgReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.zxw.dispatch.MSG_RECEIVER");
            registerReceiver(msgReceiver, intentFilter);
        }
    }


    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(MainActivity.this, this);
    }

    @Override
    public void loadLine(List<Line> lineList) {
        mLineRV.setLayoutManager(new LinearLayoutManager(this));
        mLineAdapter = new MainAdapter(lineList, this, this);
        mLineRV.setAdapter(mLineAdapter);
        mLineRV.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }


    @Override
    public void reLogin() {
        SpUtils.logOut(MyApplication.mContext);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void loadSendCarList(DragListAdapter mDragListAdapter) {
        mSendRV.setAdapter(mDragListAdapter);
        mSendRV.setMyDragListener(new DragListView.MyDragListener() {
            @Override
            public void onDragFinish(int srcPositon, int finalPosition) {

            }
        });
    }

    @Override
    public void loadGoneCarList(List<SendHistory> sendHistories) {
        mGoneRV.setLayoutManager(new LinearLayoutManager(this));
        mGoneRV.setAdapter(new SendAdapter(sendHistories, this));
        mGoneRV.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void loadStopCarList(List<StopHistory> stopHistories) {
        mStopRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mStopRV.setAdapter(new StopAdapter(stopHistories, this, new StopAdapter.OnClickStopCarListListener() {
            @Override
            public void onClickManualButtonListener() {
                showManualStopDialog();
            }

            @Override
            public void onClickStopCarListener(StopHistory stopCar) {
                showStopCarDialog();
            }
        }));
        mStopRV.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL_LIST));
    }

    private void showStopCarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View container = View.inflate(mContext, R.layout.dialog_stop_car, null);
        Button btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) container.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStopCarDialog != null && mStopCarDialog.isShowing())
                    mStopCarDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStopCarDialog != null && mStopCarDialog.isShowing())
                    mStopCarDialog.dismiss();
            }
        });
        mStopCarDialog = builder.setView(container).show();
    }

    private void showManualStopDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View container = View.inflate(mContext, R.layout.dialog_manual, null);
        EditText et_car_code = (EditText) container.findViewById(R.id.et_car_code);
        Button btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) container.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mManualStopDialog != null && mManualStopDialog.isShowing())
                    mManualStopDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mManualStopDialog != null && mManualStopDialog.isShowing())
                    mManualStopDialog.dismiss();
            }
        });
        mManualStopDialog = builder.setView(container).show();
    }

    @Override
    public void onSelectLine(Line line) {
        presenter.onSelectLine(line);
    }

    @OnClick({R.id.rb_main_automatic, R.id.rb_main_manual})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_main_automatic:
                //动态注册广播接收器
                if (msgReceiver == null){
                    msgReceiver = new MsgReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("com.zxw.dispatch.MSG_RECEIVER");
                    registerReceiver(msgReceiver, intentFilter);
                }
                presenter.selectAuto();
                break;
            case R.id.rb_main_manual:
                presenter.selectManual();
                break;
        }
    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新数据
            presenter.refreshList();
        }

    }
}
