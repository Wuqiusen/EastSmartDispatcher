package com.zxw.dispatch.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zxw.data.bean.Line;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.DepartPresenter;
import com.zxw.dispatch.presenter.view.DepartView;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.view.DragListAdapter;
import com.zxw.dispatch.view.DragListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author：CangJie on 2016/9/21 11:13
 * email：cangjie2016@gmail.com
 */
public class DepartActivity extends PresenterActivity<DepartPresenter> implements DepartView {
    @Bind(R.id.lv)
    DragListView mDragListView;
    @Bind(R.id.ll_loading)
    LinearLayout llLoading;
    private Line.LineStation station;
    private int lineId;
    private String lineName;
    private MsgReceiver msgReceiver;

    @Override
    protected DepartPresenter createPresenter() {

        station = (Line.LineStation) getIntent().getSerializableExtra("station");
        lineId = getIntent().getIntExtra("lineId", -1);
        lineName = getIntent().getStringExtra("lineName");
        return new DepartPresenter(this, this, lineId, station.stationId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        ButterKnife.bind(this);
        showTitle(station.stationName);
        showBackButton();

        showRightImageButton(R.drawable.pb_ebus_tag, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        showInfoBar(lineName);
        showRadioGroup(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //动态注册广播接收器
                if (msgReceiver == null){
                    msgReceiver = new MsgReceiver();
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("com.zxw.dispatch.MSG_RECEIVER");
                    registerReceiver(msgReceiver, intentFilter);
                }
                presenter.selectAuto();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.selectManual();
            }
        });
        presenter.loadCarData();
    }

    @OnClick(R.id.btn_add)
    public void add() {
        presenter.add();
    }

    private void showDialog() {
        Intent intent = new Intent(this, DialogActivity.class);
        intent.putExtra("lineId", lineId);
        intent.putExtra("lineName", lineName);
        intent.putExtra("station", station);
        startActivity(intent);
    }

    @Override
    public void loadLine(DragListAdapter adapter) {

        mDragListView.setAdapter(adapter);
        mDragListView.setMyDragListener(new DragListView.MyDragListener() {
            @Override
            public void onDragFinish(int srcPositon, int finalPosition) {

            }
        });
    }


    @Override
    public void showCtrlCarLoading() {
        llLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCtrlCarLoading() {
        llLoading.setVisibility(View.GONE);
    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新数据
            presenter.loadCarData();
        }

    }

    @Override
    protected void onDestroy() {
        if (msgReceiver != null)
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }
}
