package com.zxw.dispatch.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.adapter.MyPagerAdapter;
import com.zxw.dispatch.adapter.PopupAdapter;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.GoneAdapter;
import com.zxw.dispatch.recycler.MainAdapter;
import com.zxw.dispatch.recycler.StopAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.CustomViewPager;
import com.zxw.dispatch.view.DragListView;
import com.zxw.dispatch.view.MyDialog;
import com.zxw.dispatch.view.dialog.ManualAddStopCarDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends PresenterActivity<MainPresenter> implements MainView, MainAdapter.OnSelectLineListener,
        PopupAdapter.OnPopupWindowListener,View.OnClickListener{

    @Bind(R.id.img_setting)
    ImageView imgSetting;
    @Bind(R.id.img_login_out)
    ImageView imgLoginOut;
    // 控制台
    @Bind(R.id.tv_controller)
    TextView tvController;
    @Bind(R.id.line_control)
    View lineControl;
    // 排班计划
    @Bind(R.id.tv_schedule)
    TextView tvSchedule;
    @Bind(R.id.line_schedule)
    View lineSchedule;

    @Bind(R.id.rl_setting)
    RelativeLayout rlSetting;
    @Bind(R.id.rv_line)
    RecyclerView mLineRV;
    RecyclerView mGoneRV;
    RecyclerView mStopRV;
    DragListView mSendRV;
    View viewCover;
    @Bind(R.id.vp_main)
    CustomViewPager vpMain;
    TextView tvAutomatic;
    TextView tvManual;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_system_date)
    TextView tvDate;
    private MainAdapter mLineAdapter;
    private PopupAdapter popupAdapter;
    private AlertDialog mManualStopDialog, mStopCarDialog;
    private PopupWindow mPopupWindow = null;
    private ListView lv_popup =null;
    private MsgReceiver msgReceiver;
    private List<View> views = new ArrayList<View>();
    private boolean isHaveSendCar = false;
    private boolean isVisibleGoneCar = false;
    private TextView tv_steward_send;
    private TextView tv_steward_gone;
    private TextView tvAlreadyIssued;
    private LinearLayout llAlreadyIssuedCar;
    private static final int REFRESH = 1;
    private static final int AUTO = 2;
    private static final int HANDLE = 3;
    private boolean isAuto = false;

    private long clickTime = 0;

    Handler handler =  new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case REFRESH:
                        Log.w("onReceive---", "刷新数据");
                        //刷新数据
                        presenter.refreshList();
                        break;
                    case AUTO:
                        Log.w("onReceive---", "自动发车");
                        isAuto = true;
                        setTvBackground(2);
                        viewCover.setVisibility(View.VISIBLE);
                        break;
                    case HANDLE:
                        Log.w("onReceive---", "手动发车");
                        isAuto = false;
                        setTvBackground(1);
                        viewCover.setVisibility(View.GONE);
                        break;
                }
            } catch (Exception e) {
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        createReceiver();
        initView();
        int spotId = getIntent().getIntExtra("spotId", -1);
        presenter.loadLineList(spotId);
    }

    private void initView() {
        hideHeadArea();
        hideTitle();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        tvUserName.setText(SpUtils.getCache(mContext, SpUtils.NAME));
        tvDate.setText(formatter.format(curDate));
        // 控制台
        View view = View.inflate(mContext, R.layout.tab_view_control_deck, null);
        mSendRV = (DragListView) view.findViewById(R.id.lv_send_car);
        mGoneRV = (RecyclerView) view.findViewById(R.id.rv_gone_car);
        mStopRV = (RecyclerView) view.findViewById(R.id.rv_stop_car);
        viewCover = (View) view.findViewById(R.id.view_cover);
        tvAutomatic = (TextView) view.findViewById(R.id.tv_automatic);
        tvManual = (TextView) view.findViewById(R.id.tv_manual);
        tv_steward_send = (TextView) view.findViewById(R.id.tv_steward_send);
        tv_steward_gone = (TextView) view.findViewById(R.id.tv_steward_gone);
        tvAlreadyIssued = (TextView) view.findViewById(R.id.tv_already_issued_car);
        llAlreadyIssuedCar = (LinearLayout)view.findViewById(R.id.ll_already_issued_car);
        tvAutomatic.setOnClickListener(this);
        tvManual.setOnClickListener(this);
        tvController.setOnClickListener(this);
        tvAlreadyIssued.setOnClickListener(this);
        views.add(view);

        // 排班计划
        View schedulingView = View.inflate(mContext,R.layout.tab_view_scheduling_plan,null);
        views.add(schedulingView);
        tvSchedule.setOnClickListener(this);
        // 设置按钮
        imgSetting.setOnClickListener(this);

        imgLoginOut.setOnClickListener(this);

        MyPagerAdapter mAdapter = new MyPagerAdapter(views, null);
        vpMain.setAdapter(mAdapter);
        vpMain.setCurrentItem(0);
        vpMain.setPagingEnabled(false);
        setLineBackground(0);
        setTvBackground(1);
        mGoneRV.setLayoutManager(new LinearLayoutManager(this));
        mGoneRV.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mStopRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void createReceiver() {
        //动态注册广播接收器
        if (msgReceiver == null) {
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
        mLineRV.addItemDecoration(new DividerItemDecoration(this,R.color.white,
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
        isHaveSendCar = false;
        mSendRV.setAdapter(mDragListAdapter);
        mSendRV.setMyDragListener(new DragListView.MyDragListener() {
            @Override
            public void onDragFinish(int srcPositon, int finalPosition) {

            }
        });
        if (mDragListAdapter.getCount() > 0)
            isHaveSendCar = true;
    }

    @Override
    public void loadGoneCarList(GoneAdapter goneAdapter) {
        mGoneRV.setAdapter(goneAdapter);
    }

    @Override
    public void loadStopCarList(List<StopHistory> stopHistories) {
        mStopRV.setAdapter(new StopAdapter(stopHistories, this, new StopAdapter.OnClickStopCarListListener() {
            @Override
            public void onClickManualButtonListener() {
                showManualAddStopCarDialog();
            }

            @Override
            public void onClickStopCarListener(StopHistory stopCar) {
                showVehicleToScheduleDialog(stopCar);
            }
        }));
    }

    @Override
    public void hideStewardName() {
        tv_steward_send.setVisibility(View.GONE);
        tv_steward_gone.setVisibility(View.GONE);
    }

    @Override
    public void showStewardName() {
        tv_steward_send.setVisibility(View.VISIBLE);
        tv_steward_gone.setVisibility(View.VISIBLE);
    }

    private void showVehicleToScheduleDialog(final StopHistory stopCar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View container = View.inflate(mContext, R.layout.dialog_stop_car, null);
        Button btn_confirm = (Button) container.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) container.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStopCarDialog != null && mStopCarDialog.isShowing()){
                    presenter.vehicleToSchedule(stopCar);
                    mStopCarDialog.dismiss();
                }
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

    private void showManualAddStopCarDialog() {
        new ManualAddStopCarDialog(mContext, presenter.getLineParams(), new ManualAddStopCarDialog.OnManualAddStopCarListener() {
            @Override
            public void manualAddStopCar(String carId, String driverId, String stewardId) {
                presenter.manualAddStopCar(carId, driverId, stewardId);
            }
        });
    }

    @Override
    public void onSelectLine(Line line) {
        //传递线路id查询是否该线路已开启自动发车
        Intent intent = new Intent("com.zxw.dispatch.service.RECEIVER");
        intent.putExtra("type", "getData");
        intent.putExtra("lineKey", line.lineId);
        sendBroadcast(intent);
        presenter.onSelectLine(line);
    }

    private void setLineBackground(int tabPosition) {
        switch (tabPosition){
            case 0:
                lineControl.setVisibility(View.VISIBLE);
                lineControl.setBackgroundColor(mContext.getResources().getColor(R.color.scroll_bar_bg));
                lineSchedule.setVisibility(View.INVISIBLE);
                break;
            case 1:
                lineSchedule.setVisibility(View.VISIBLE);
                lineSchedule.setBackgroundColor(mContext.getResources().getColor(R.color.scroll_bar_bg));
                lineControl.setVisibility(View.INVISIBLE);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_controller:
                vpMain.setCurrentItem(0);
                setLineBackground(0);
                break;
            case R.id.tv_schedule:
                vpMain.setCurrentItem(1);
                setLineBackground(1);
                break;
            case R.id.img_setting:
                showPopupWindow();
                break;
            case R.id.img_login_out:
                isSureLoginOut();
                break;
            case R.id.tv_automatic:
                if ((System.currentTimeMillis() - clickTime) > 1000) {
                    if (!isAuto){
                        if (!isHaveSendCar){
                            ToastHelper.showToast("该线路没有待发车辆", mContext);
                            return;
                        }
                        setTvBackground(2);
                        viewCover.setVisibility(View.VISIBLE);
                        //动态注册广播接收器
                        createReceiver();
                        presenter.selectAuto();
                        clickTime = System.currentTimeMillis();
                        isAuto = true;
                    }
                }
                break;
            case R.id.tv_manual:
                if ((System.currentTimeMillis() - clickTime) > 1000) {
                    if (isAuto){
                        setTvBackground(1);
                        viewCover.setVisibility(View.GONE);
                        presenter.selectManual();
                        clickTime = System.currentTimeMillis();
                        isAuto = false;
                    }
                }
                break;
            case R.id.tv_already_issued_car:
                if (isVisibleGoneCar){
                    isVisibleGoneCar = false;
                    llAlreadyIssuedCar.setVisibility(View.VISIBLE);
                }else{
                    isVisibleGoneCar = true;
                    llAlreadyIssuedCar.setVisibility(View.GONE);
                }

        }
    }

    private void showPopupWindow() {
        if (mPopupWindow == null || !mPopupWindow.isShowing()){
              initPopupWindow();
        }else{
              mPopupWindow.dismiss();
              mPopupWindow = null;
        }
    }

    private void initPopupWindow() {
        List<String> list = new ArrayList<>();
        View popView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_popupwindow,null);
        lv_popup = (ListView) popView.findViewById(R.id.lv_popup);
        list.add("修改资料");
        list.add("密码修改");
        list.add("退出");
        popupAdapter = new PopupAdapter(mContext,list,this);
        lv_popup.setAdapter(popupAdapter);
        mPopupWindow = new PopupWindow(popView, 400, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new PaintDrawable());
        mPopupWindow.showAsDropDown(rlSetting,300,4);
    }

    @Override
    public void onPopupListener(int position) {
        switch (position){
            case 0:
                ToastHelper.showToast("修改资料",mContext);
                break;
            case 1:
                ToastHelper.showToast("密码修改",mContext);
                break;
            case 2:
                isSureLoginOut();
                break;

        }
    }

    private void setTvBackground(int poi){
        if (poi == 1){
            tvManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_select_style));
            tvAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_normal_style));
            tvManual.setTextColor(getResources().getColor(R.color.white));
            tvAutomatic.setTextColor(getResources().getColor(R.color.font_black));
        } else {
            tvManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_normal_style));
            tvAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_select_style));
            tvManual.setTextColor(getResources().getColor(R.color.font_black));
            tvAutomatic.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(msgReceiver);
        super.onDestroy();
    }

    private void isSureLoginOut() {
        final MyDialog outDialog = new MyDialog(MainActivity.this,"提示","确定要退出系统？",MyDialog.HAVEBUTTON);
        outDialog.show();
        outDialog.setCancelable(false);
        outDialog.ButtonQuery(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 outDialog.dismiss();
                 doLoginOut();
            }
        });
        outDialog.ButtonCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 outDialog.dismiss();
            }
        });
    }

    private void doLoginOut() {
          Intent intent = new Intent(MainActivity.this,LoginActivity.class);
          SpUtils.logOut(mContext);
          startActivity(intent);
          finish();
    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w("onReceive---", "Main广播已接收");
            Message obtain = Message.obtain();
            if (TextUtils.equals(intent.getStringExtra("type"), "getData")) {
                Log.w("onReceive---", "更新发车状态");
                if (intent.getBooleanExtra("isAuto", false)) {
                    obtain.what = AUTO;
                    handler.sendMessage(obtain);
                } else {
                    obtain.what = HANDLE;
                    handler.sendMessage(obtain);
                }
            } else {
                Log.w("onReceive---", "刷新数据");
                //刷新数据
                obtain.what = REFRESH;
                handler.sendMessage(obtain);
            }
        }

    }

}
