package com.zxw.dispatch.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.MissionType;
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
import com.zxw.dispatch.recycler.NonMissionTypeAdapter;
import com.zxw.dispatch.recycler.StopAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.CustomViewPager;
import com.zxw.dispatch.view.DragListView;
import com.zxw.dispatch.view.MyDialog;
import com.zxw.dispatch.view.dialog.ManualAddStopCarDialog;
import com.zxw.dispatch.view.dialog.MissionTypeWaitCarDialog;
import com.zxw.dispatch.view.dialog.NoMissionTypeWaitCarDialog;
import com.zxw.dispatch.view.dialog.VehicleToScheduleDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends PresenterActivity<MainPresenter> implements MainView, MainAdapter.OnSelectLineListener,
        PopupAdapter.OnPopupWindowListener, View.OnClickListener {

    TextView tvMenuDepart;
    RelativeLayout rlMenuBackground;
    LinearLayout llMenuWaitDepart;
    TextView tvMenuAutomatic;
    TextView tvMenuManual;
    TextView tvMenuWaitCar;
    TextView tvMenuGoneCar;
    TextView tvMenuStopCar;

    ImageView imgOnOff;
    FrameLayout fl_vertical;
    FrameLayout fl_horizontal;
    CustomViewPager vpEMain;
    RecyclerView eGoneRV;
    DragListView eWaitRV;
    RecyclerView eStopRV;
    View viewMenuCover;

    private List<View> eViews = new ArrayList<View>();

    /*导航栏*/
    @Bind(R.id.rl_controller)
    RelativeLayout rlController;
    @Bind(R.id.tv_controller)
    TextView tvController;
//  @Bind(R.id.rl_schedule)
//  RelativeLayout rlSchedule;
//  @Bind(R.id.tv_schedule)
//  TextView tvSchedule;


    /*设置*/
    @Bind(R.id.img_setting)
    ImageView imgSetting;
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
    private ListView lv_popup = null;
    private LinearLayout ll_popupwindow;
    private MsgReceiver msgReceiver;
    private List<View> views = new ArrayList<View>();
    private boolean isHaveSendCar = false;
    private boolean isShow = true;
    private boolean isClickWaitCar = true;
    private boolean isPopbg = true;
    private TextView tv_steward_send;
    private TextView tv_steward_gone;
    private TextView tv_menu_steward_gone;
    private TextView tv_menu_steward_send;
    private static final int REFRESH = 1;
    private static final int AUTO = 2;
    private static final int HANDLE = 3;
    private static final int SEND_CAR_COUNT = 4;
    private boolean isAuto = false;
    private Timer mTimer = null;
    private long clickTime = 0;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case REFRESH:
                        Log.w("onReceive---", "刷新数据");
                        presenter.refreshList();
                        break;
                    case AUTO:
                        Log.w("onReceive---", "自动发车");
                        isAuto = true;
                        setTvBackground(2);
                        setCoverBackground(View.VISIBLE);

                        break;
                    case HANDLE:
                        Log.w("onReceive---", "手动发车");
                        isAuto = false;
                        setTvBackground(1);
                        setCoverBackground(View.GONE);
                        break;
                    case SEND_CAR_COUNT:
                        Log.w("onReceive---", "更新待发车辆数");
                        presenter.timeToSend();
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
        initData();
        initView();
        initTabEvent();
        int spotId = getIntent().getIntExtra("spotId", -1);
        presenter.loadLineList(1);
//        presenter.loadLineList(spotId);
    }


    private void initData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        tvUserName.setText(SpUtils.getCache(mContext, SpUtils.NAME));
        tvDate.setText(formatter.format(curDate));
    }

    private void initView() {
        hideHeadArea();
        hideTitle();
        /*控制台*/
        views.add(initControlDeckView());
        /*线路运行图*/
        /*排班计划*/
        views.add(initSchedulingView());
        /*默认视图*/
        showContentView(views);

    }

    private View initSchedulingView() {
        View view = View.inflate(mContext, R.layout.tab_view_scheduling_plan, null);
        return view;
    }


    private View initControlDeckView(){
        View view = View.inflate(mContext, R.layout.tab_view_control_deck, null);
        fl_vertical = (FrameLayout) view.findViewById(R.id.fl_vertical);
        fl_horizontal = (FrameLayout) view.findViewById(R.id.fl_horizontal);
        initMenuView(view);
        initContentView(view);
        initMenuPagerViews(view);
        return view;
    }

    private void showContentView(List<View> views) {
        MyPagerAdapter mAdapter = new MyPagerAdapter(views, null);
        vpMain.setAdapter(mAdapter);
        vpMain.setCurrentItem(0);
        vpMain.setPagingEnabled(false);
        setTabBackground(0);
        setTvBackground(1);
        mGoneRV.setLayoutManager(new LinearLayoutManager(this));
        mGoneRV.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mStopRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }


    private void initMenuView(View view) {
        tvMenuDepart = (TextView) view.findViewById(R.id.tv_menu_depart_car);
        rlMenuBackground = (RelativeLayout) view.findViewById(R.id.rl_menu_background);
        llMenuWaitDepart = (LinearLayout) view.findViewById(R.id.ll_menu_wait_depart);
        tvMenuAutomatic = (TextView) view.findViewById(R.id.tv_menu_automatic);
        tvMenuManual = (TextView) view.findViewById(R.id.tv_menu_manual);
        tvMenuWaitCar = (TextView) view.findViewById(R.id.tv_menu_wait_car);
        tvMenuGoneCar = (TextView) view.findViewById(R.id.tv_menu_gone_car);
        tvMenuStopCar = (TextView) view.findViewById(R.id.tv_menu_stop_car);
        imgOnOff = (ImageView) view.findViewById(R.id.img_menu_on_off);
        tvMenuWaitCar.setOnClickListener(this);
        tvMenuGoneCar.setOnClickListener(this);
        tvMenuStopCar.setOnClickListener(this);

        tvMenuAutomatic.setOnClickListener(this);
        tvMenuManual.setOnClickListener(this);
        rlMenuBackground.setOnClickListener(this);
        imgOnOff.setOnClickListener(this);
    }

    private void initContentView(View view) {
        mSendRV = (DragListView) view.findViewById(R.id.lv_send_car);
        mGoneRV = (RecyclerView) view.findViewById(R.id.rv_gone_car);
        mStopRV = (RecyclerView) view.findViewById(R.id.rv_stop_car);
        viewCover = (View) view.findViewById(R.id.view_cover);
        tvAutomatic = (TextView) view.findViewById(R.id.tv_automatic);
        tvManual = (TextView) view.findViewById(R.id.tv_manual);
        tv_steward_send = (TextView) view.findViewById(R.id.tv_steward_send);
        tv_steward_gone = (TextView) view.findViewById(R.id.tv_steward_gone);
        tvAutomatic.setOnClickListener(this);
        tvManual.setOnClickListener(this);
    }


    private void initMenuPagerViews(View view) {
        vpEMain = (CustomViewPager) view.findViewById(R.id.vp_main_horizontal);
        View goView = View.inflate(mContext,R.layout.item_gone_car,null);
        eGoneRV = (RecyclerView) goView.findViewById(R.id.rv_menu_gone_car);
        tv_menu_steward_gone = (TextView) goView.findViewById(R.id.tv_menu_steward_gone);
        View waitView = View.inflate(mContext,R.layout.item_wait_car,null);
        eWaitRV = (DragListView) waitView.findViewById(R.id.rv_menu_wait_car);
        viewMenuCover = (View) waitView.findViewById(R.id.view_menu_cover);
        tv_menu_steward_send = (TextView) waitView.findViewById(R.id.tv_menu_steward_send);
        View stopView = View.inflate(mContext,R.layout.item_stop_car,null);
        eStopRV = (RecyclerView) stopView.findViewById(R.id.rv_menu_stop_car);
        eViews.add(goView);
        eViews.add(waitView);
        eViews.add(stopView);

        MyPagerAdapter eAdapter = new MyPagerAdapter(eViews,null);
        vpEMain.setAdapter(eAdapter);
        vpEMain.setCurrentItem(1);
        setScrollBarBackground(1);
        vpEMain.setPagingEnabled(false);
        eGoneRV.setLayoutManager(new LinearLayoutManager(this));
        eGoneRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        GridLayoutManager layoutManager = new GridLayoutManager(this,10);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eStopRV.setLayoutManager(layoutManager);
    }


    private void initTabEvent() {
        /*控制台*/
        rlController.setOnClickListener(this);
        /*线路运行图*/
        /*排班计划*/
        //rlSchedule.setOnClickListener(this);
        /*设置按钮*/
        imgSetting.setOnClickListener(this);
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

    private void refreshSendCarTimer(){
        if (mTimer == null){
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //更新待发车辆数
                    Message obtain = Message.obtain();
                    obtain.what = SEND_CAR_COUNT;
                    handler.sendMessage(obtain);
                }
            },0 , 1000 * 60);
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
        mLineRV.addItemDecoration(new DividerItemDecoration(this, R.color.white,
                DividerItemDecoration.VERTICAL_LIST));

        refreshSendCarTimer();
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
        DragListView.MyDragListener mListener = createMyDragListener();
        mSendRV.setAdapter(mDragListAdapter);
        mSendRV.setMyDragListener(mListener);

        eWaitRV.setAdapter(mDragListAdapter);
        eWaitRV.setMyDragListener(mListener);
        if (mDragListAdapter.getCount() > 0)
            isHaveSendCar = true;
    }

    private DragListView.MyDragListener createMyDragListener() {
        return new DragListView.MyDragListener() {
            @Override
            public void onDragFinish(int srcPositon, int finalPosition) {

            }
        };
    }

    @Override
    public void loadGoneCarList(GoneAdapter goneAdapter) {
        mGoneRV.setAdapter(goneAdapter);
        eGoneRV.setAdapter(goneAdapter);
    }

    @Override
    public void loadStopCarList(List<StopHistory> stopHistories) {
        StopAdapter mAdapter = createStopAdapter(stopHistories);
        mStopRV.setAdapter(mAdapter);
        eStopRV.setAdapter(mAdapter);
    }

    private StopAdapter createStopAdapter(List<StopHistory> stopHistories) {
        return new StopAdapter(stopHistories, this, new StopAdapter.OnClickStopCarListListener() {
            @Override
            public void onClickManualButtonListener() {
                showManualAddStopCarDialog();
//                showVehicleToScheduleDialog(null);
            }

            @Override
            public void onClickStopCarListener(StopHistory stopCar) {
                showVehicleToScheduleDialog(stopCar);
            }
        });
    }


    @Override
    public void showMissionTypeDialog(List<MissionType> missionTypes, final int objId, int type, String taskId, String lineName) {
        new MissionTypeWaitCarDialog(mContext, missionTypes, objId, type, taskId,
                lineName, new MissionTypeWaitCarDialog.OnChangeMissionTypeListener() {
            @Override
            public void changeMissionType(final int objId, int type, String taskId) {
                presenter.changeMissionType(objId, type, taskId);
            }
        });

    }

    @Override
    public void refreshTimeToSendCarNum(List<Integer> sendCarNum) {
        mLineAdapter.setSendCarNum(sendCarNum);

    }

    @Override
    public void nonMissionTypeDialog(NonMissionTypeAdapter adapter) {
            new NoMissionTypeWaitCarDialog(mContext,adapter);
    }

    @Override
    public void hideStewardName() {
        isShowStewardName(View.GONE);
    }

    @Override
    public void showStewardName() {
        isShowStewardName(View.VISIBLE);
    }

    private void isShowStewardName(int isVisible) {
        tv_steward_send.setVisibility(isVisible);
        tv_steward_gone.setVisibility(isVisible);
        tv_menu_steward_send.setVisibility(isVisible);
        tv_menu_steward_gone.setVisibility(isVisible);
    }

    private void showVehicleToScheduleDialog(final StopHistory stopCar) {
        new VehicleToScheduleDialog(mContext, stopCar, new VehicleToScheduleDialog.OnClickListener() {
            @Override
            public void onClickNormalMission(int type, int taskId) {
                presenter.stopCarMission(stopCar, type, String.valueOf(taskId),null, null, null, null, null);
            }

            @Override
            public void onClickOperatorEmptyMission(int type, int taskType, String beginTime, String endTime, String runNum, String runEmpMileage) {
                presenter.stopCarMission(stopCar, type, null, String.valueOf(taskType), beginTime, endTime, runNum, runEmpMileage);
            }

            @Override
            public void onClickOperatorNotEmptyMission(int type, int taskType, String beginTime, String endTime, String runNum, String runEmpMileage) {
                presenter.stopCarMission(stopCar, type, null, String.valueOf(taskType), beginTime, endTime, runNum, runEmpMileage);
            }

            @Override
            public void onClickHelpMission(int type, int taskId) {
                presenter.stopCarMission(stopCar, type, String.valueOf(taskId),null, null, null, null, null);
            }

            @Override
            public void onOffDuty() {

            }
        }, presenter.getLineId());
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

    private void setTabBackground(int tabPosition) {
        switch (tabPosition) {
            case 0:
                rlController.setBackground(getDrawable(true));
                tvController.setTextColor(mContext.getResources().getColor(R.color.background_bg_blue));
//              rlSchedule.setBackgroundColor(mContext.getResources().getColor(R.color.background_deep_blue));
//              tvSchedule.setTextColor(mContext.getResources().getColor(R.color.font_gray));
                break;
            case 1:
                break;
            case 2:
//              rlSchedule.setBackground(getDrawable(true));
//              tvSchedule.setTextColor(mContext.getResources().getColor(R.color.background_bg_blue));
//              rlController.setBackgroundColor(mContext.getResources().getColor(R.color.background_deep_blue));
//              tvController.setTextColor(mContext.getResources().getColor(R.color.font_gray));
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    private void setScrollBarBackground(int pos){
        switch (pos){
            case 0:
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                llMenuWaitDepart.setVisibility(View.GONE);
                break;
            case 1:
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                if (isPopbg) {
                    llMenuWaitDepart.setVisibility(View.GONE);
                }else{
                    llMenuWaitDepart.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                llMenuWaitDepart.setVisibility(View.GONE);
                break;
        }
    }

    private Drawable getDrawable(boolean isTab) {
        if (isTab) {
            return mContext.getResources().getDrawable(R.drawable.tab_white_rectangle);
        }
        return mContext.getResources().getDrawable(R.drawable.line_blue_height);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_controller:
                vpMain.setCurrentItem(0);
                setTabBackground(0);
                break;
//          case R.id.rl_schedule:
//              vpMain.setCurrentItem(2);
//              setTabBackground(2);
//              break;
            case R.id.img_setting:
                showPopupWindow();
                break;
            case R.id.rl_menu_background:
                DebugLog.e("111click menu-------");
            case R.id.img_menu_on_off:
                DebugLog.e("2222click menu-------");
                changeControlDeckView();
                break;
            case R.id.tv_menu_gone_car:
                vpEMain.setCurrentItem(0);
                setScrollBarBackground(0);
                isClickWaitCar = false;
                break;
            case R.id.tv_menu_wait_car:
                vpEMain.setCurrentItem(1);
                setScrollBarBackground(1);
                isClickWaitCar = true;
                break;
            case R.id.tv_menu_stop_car:
                vpEMain.setCurrentItem(2);
                setScrollBarBackground(2);
                isClickWaitCar = false;
                break;

            // 自动发车
            case R.id.tv_automatic:
            case R.id.tv_menu_automatic:
                if ((System.currentTimeMillis() - clickTime) > 1000) {
                    if (!isAuto) {
                        if (!isHaveSendCar) {
                            ToastHelper.showToast("该线路没有待发车辆", mContext);
                            return;
                        }
                        setTvBackground(2);
                        setCoverBackground(View.VISIBLE);
                        //动态注册广播接收器
                        createReceiver();
                        presenter.selectAuto();
                        clickTime = System.currentTimeMillis();
                        isAuto = true;
                    }
                }
                break;
            // 手动发车
            case R.id.tv_manual:
            case R.id.tv_menu_manual:
                if ((System.currentTimeMillis() - clickTime) > 1000) {
                    if (isAuto) {
                        setTvBackground(1);
                        setCoverBackground(View.GONE);
                        presenter.selectManual();
                        clickTime = System.currentTimeMillis();
                        isAuto = false;
                    }
                }
                break;


        }
    }

    private void setCoverBackground(int isVisible) {
        viewCover.setVisibility(isVisible);
        viewMenuCover.setVisibility(isVisible);
    }

    private void changeControlDeckView() {
        initMenu(isShow);
        if (isShow){
            DebugLog.e("click menu-------");

            fl_vertical.setVisibility(View.GONE);
            fl_horizontal.setVisibility(View.VISIBLE);
            isShow = false;
            isPopbg = isShow;
        }else{
//            initMenu(isShow);
            DebugLog.e("close menu-------");
            fl_horizontal.setVisibility(View.GONE);
            fl_vertical.setVisibility(View.VISIBLE);
            isShow = true;
            isPopbg = isShow;
        }
    }

    private void initMenu(boolean isShow) {
        if (isShow){

            if (isClickWaitCar) {
                llMenuWaitDepart.setVisibility(View.VISIBLE);
            }else{
                llMenuWaitDepart.setVisibility(View.GONE);
            }

            tvMenuDepart.setVisibility(View.GONE);
            tvMenuWaitCar.setVisibility(View.VISIBLE);
            tvMenuGoneCar.setVisibility(View.VISIBLE);
            tvMenuStopCar.setVisibility(View.VISIBLE);
            rlMenuBackground.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rlMenuBackground.setEnabled(false);
        }else{
            llMenuWaitDepart.setVisibility(View.GONE);
            tvMenuWaitCar.setVisibility(View.GONE);
            tvMenuGoneCar.setVisibility(View.GONE);
            tvMenuStopCar.setVisibility(View.GONE);
            tvMenuDepart.setVisibility(View.VISIBLE);
            rlMenuBackground.setBackgroundColor(mContext.getResources().getColor(R.color.background_gray5));
            rlMenuBackground.setEnabled(true);

        }
    }


    private void showPopupWindow() {
        if (mPopupWindow == null || !mPopupWindow.isShowing()) {
            initPopupWindow();
        } else {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    private void initPopupWindow() {
        List<String> list = new ArrayList<>();
        View popView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_popupwindow, null);
        ll_popupwindow = (LinearLayout) popView.findViewById(R.id.ll_popupwindow);
        lv_popup = (ListView) popView.findViewById(R.id.lv_popup);
        if (isPopbg){
            ll_popupwindow.setBackgroundColor(mContext.getResources().getColor(R.color.background_gray5));
        }else{
            ll_popupwindow.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        list.add(getResources().getString(R.string.datum_update));
        list.add(getResources().getString(R.string.password_update));
        list.add(getResources().getString(R.string.login_out));
        popupAdapter = new PopupAdapter(mContext, list, this);
        lv_popup.setAdapter(popupAdapter);
        mPopupWindow = new PopupWindow(popView, 360, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new PaintDrawable());
        mPopupWindow.showAsDropDown(rlSetting,300,12);
    }

    @Override
    public void onPopupListener(int position) {
        switch (position) {
            case 0:
                ToastHelper.showToast("修改资料", mContext);
                break;
            case 1:
                ToastHelper.showToast("密码修改", mContext);
                break;
            case 2:
                isSureLoginOut();
                break;

        }
    }

    private void setTvBackground(int poi) {
        if (poi == 1) {
            tvManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_select_style));
            tvAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_normal_style));
            tvManual.setTextColor(getResources().getColor(R.color.white));
            tvAutomatic.setTextColor(getResources().getColor(R.color.font_black));

            tvMenuManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_select_style));
            tvMenuAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_normal_style));
            tvMenuManual.setTextColor(getResources().getColor(R.color.white));
            tvMenuAutomatic.setTextColor(getResources().getColor(R.color.font_black));

        } else {
            tvManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_normal_style));
            tvAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_select_style));
            tvManual.setTextColor(getResources().getColor(R.color.font_black));
            tvAutomatic.setTextColor(getResources().getColor(R.color.white));

            tvMenuManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_normal_style));
            tvMenuAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_select_style));
            tvMenuManual.setTextColor(getResources().getColor(R.color.font_black));
            tvMenuAutomatic.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(msgReceiver);
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroy();
    }

    private void isSureLoginOut() {
        final MyDialog outDialog = new MyDialog(MainActivity.this, "提示", "确定要退出系统？", MyDialog.HAVEBUTTON);
        outDialog.show();
        outDialog.setCancelable(false);
        outDialog.ButtonQuery(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
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
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
