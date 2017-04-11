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
import com.zxw.data.bean.VehicleNumberBean;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.adapter.DragListAdapterForNotOperatorEmpty;
import com.zxw.dispatch.adapter.DragListAdapterForOperatorEmpty;
import com.zxw.dispatch.adapter.MyPagerAdapter;
import com.zxw.dispatch.adapter.PopupAdapter;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.GoneAdapterForNormal;
import com.zxw.dispatch.recycler.GoneAdapterForNotOperatorEmpty;
import com.zxw.dispatch.recycler.GoneAdapterForOperatorEmpty;
import com.zxw.dispatch.recycler.MainAdapter;
import com.zxw.dispatch.recycler.NonMissionTypeAdapter;
import com.zxw.dispatch.recycler.StopEndAdapter;
import com.zxw.dispatch.recycler.StopStayAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.CustomViewPager;
import com.zxw.dispatch.view.DragListView;
import com.zxw.dispatch.view.MyDialog;
import com.zxw.dispatch.view.StartCarView;
import com.zxw.dispatch.view.StopCarView;
import com.zxw.dispatch.view.WaitCarView;
import com.zxw.dispatch.view.dialog.AddRecordingCarTaskDialog;
import com.zxw.dispatch.view.dialog.ManualAddStopCarDialog;
import com.zxw.dispatch.view.dialog.MissionTypeWaitCarDialog;
import com.zxw.dispatch.view.dialog.NoMissionTypeWaitCarDialog;
import com.zxw.dispatch.view.dialog.StopCarEndToStayDialog;
import com.zxw.dispatch.view.dialog.VehicleToScheduleDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zxw.dispatch.R.id.tv_steward_show;


public class MainActivity extends PresenterActivity<MainPresenter> implements MainView, MainAdapter.OnSelectLineListener,
        PopupAdapter.OnPopupWindowListener, View.OnClickListener {


    TextView tvMenuDepart;
    RelativeLayout rlMenuBackground;
    LinearLayout llMenuWaitDepart;
    TextView tvAddRecroding; // 补录
    TextView tvMenuAutomatic;
    TextView tvMenuManual;
    TextView tvMenuWaitCar;
    TextView tvMenuGoneCar;
    TextView tvMenuStopCar;
    LinearLayout llTabVerStartCat;

    ImageView imgOnOff;
    FrameLayout fl_vertical;
    FrameLayout fl_horizontal;
    CustomViewPager vp_horizontal;
//    RecyclerView eStopRV;
    TextView tv_stab1;// 已发车辆
    TextView tv_stab2;
    TextView tv_stab3;
    CustomViewPager vp_start_car;
    TextView tv_wtab1;// 待发车辆
    TextView tv_wtab2;
    TextView tv_wtab3;
    TextView tv_ver_stop_tab1;
    TextView tv_ver_stop_tab2;
    private int stab1_size=0;
    private int stab2_size=0;
    private int stab3_size=0;
    private int wtab1_size=0;
    private int wtab2_size=0;
    private int wtab3_size=0;
    private int ptab1_size=0;
    private int ptab2_size=0;

    private int sCount = 0;
    private int wCount = 0;
    private int tCount = 0;
    CustomViewPager vp_wait_car, vp_stop_car;
    private List<View> startViews = new ArrayList<View>();
    private List<View> waitViews = new ArrayList<View>();
    private List<View> stopViews = new ArrayList<View>();

    private StartCarView mHorStartCarView;
    private WaitCarView mHorWaitCarView;

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
    RecyclerView mGoneRV1, mGoneRV2, mGoneRV3;
    DragListView mSendRV1, mSendRV2, mSendRV3;

    View viewCover;
    @Bind(R.id.vp_main)
    CustomViewPager vpMain;
    TextView tvAutomatic;
    TextView tvManual;
    TextView tvVerWaitCar;
    TextView tvVerStopCar;

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
    private List<View> eViews = new ArrayList<View>();
    private boolean isHaveSendCar = false;
    private boolean isShow = true;
    private boolean isClickWaitCar = true;
    private boolean isPopbg = true;
    private int startCarCount = 0;
    private TextView tv_steward_send;
    private TextView tv_steward_gone;

    private static final int REFRESH = 1;
    private static final int AUTO = 2;
    private static final int HANDLE = 3;
    private static final int SEND_CAR_COUNT = 4;
    private boolean isAuto = false;
    private Timer mTimer = null;
    private long clickTime = 0;
    private int spotId;
    private List<MissionType> mMissionTypes = new ArrayList<>();
    private boolean isGetMissionTypes = false;
    private List<VehicleNumberBean> mSendCarNum;

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
//                        presenter.timeToSend();
                        presenter.checkVehicleCount(spotId);
                        break;
                }
            } catch (Exception e) {
            }
        }
    };
    private TextView mGoneRV1_StewardShow, mGoneRV2_StewardShow, mGoneRV3_StewardShow;
    private TextView mSendRV1_tv_steward_show, mSendRV2_tv_steward_show, mSendRV3_tv_steward_show;
    private StopCarView mHorStopCarView;
    private RecyclerView mStopRV1, mStopRV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        createReceiver();
        initData();
        initView();
        initTabEvent();
        spotId = getIntent().getIntExtra("spotId", -1);
//        presenter.loadLineList(1);
        presenter.loadLineList(spotId);
        presenter.checkStopCar(spotId);
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
        // 操控台
        views.add(initControlDeckView());
        // 线路运行图
        // 排班计划
        views.add(initSchedulingView());
        // 默认视图
        showContentView(views);

    }

    private View initSchedulingView() {
        View view = View.inflate(mContext, R.layout.tab_view_scheduling_plan, null);
        return view;
    }


    private View initControlDeckView(){
        View view = View.inflate(mContext, R.layout.tab_view_control_deck1, null);
        fl_vertical = (FrameLayout) view.findViewById(R.id.fl_vertical);
        fl_horizontal = (FrameLayout) view.findViewById(R.id.fl_horizontal);
        initMenuView(view);
        initVerContentView(view);
        initHorContentView(view);
        return view;
    }


    /**
     * 菜单栏
     * @param view
     */
    private void initMenuView(View view) {
        tvMenuDepart = (TextView) view.findViewById(R.id.tv_menu_depart_car);
        rlMenuBackground = (RelativeLayout) view.findViewById(R.id.rl_menu_background);
        llMenuWaitDepart = (LinearLayout) view.findViewById(R.id.ll_menu_wait_depart);
        tvAddRecroding = (TextView) view.findViewById(R.id.tv_add_recording);
        tvMenuAutomatic = (TextView) view.findViewById(R.id.tv_menu_automatic);
        tvMenuManual = (TextView) view.findViewById(R.id.tv_menu_manual);
        tvMenuWaitCar = (TextView) view.findViewById(R.id.tv_menu_wait_car);
        tvMenuGoneCar = (TextView) view.findViewById(R.id.tv_menu_gone_car);
        llTabVerStartCat = (LinearLayout) view.findViewById(R.id.ll_tab_ver_start_car);

        tvMenuStopCar = (TextView) view.findViewById(R.id.tv_menu_stop_car);
        imgOnOff = (ImageView) view.findViewById(R.id.img_menu_on_off);
        tvAddRecroding.setOnClickListener(this);
        tvMenuWaitCar.setOnClickListener(this);
        tvMenuGoneCar.setOnClickListener(this);
        tvMenuStopCar.setOnClickListener(this);

        tvMenuAutomatic.setOnClickListener(this);
        tvMenuManual.setOnClickListener(this);
//      rlMenuBackground.setOnClickListener(this);
        imgOnOff.setOnClickListener(this);
    }


    private void initVerContentView(View view) {
        /*已发车辆*/
        initVerStartCarViewPager(view);
        /*待发车辆*/
        initVerWaitCarViewPager(view);
        /*停场车辆*/
        initVerStopCarViewPager(view);
        /*固有操作*/
        tvVerWaitCar = (TextView) view.findViewById(R.id.tv_ver_wait_car);
        tvVerStopCar = (TextView) view.findViewById(R.id.tv_ver_stop_car);
        tvAutomatic = (TextView) view.findViewById(R.id.tv_automatic);// 自动发车
        tvManual = (TextView) view.findViewById(R.id.tv_manual);// 手动发车
        tvAutomatic.setOnClickListener(this);
        tvManual.setOnClickListener(this);
    }

    private void initVerStopCarViewPager(View view) {
        tv_ver_stop_tab1 = (TextView) view.findViewById(R.id.tv_ver_stop_tab1);
        tv_ver_stop_tab2 = (TextView) view.findViewById(R.id.tv_ver_stop_tab2);
        tv_ver_stop_tab1.setOnClickListener(this);
        tv_ver_stop_tab2.setOnClickListener(this);
        vp_stop_car = (CustomViewPager) view.findViewById(R.id.vp_stop_car);
        MyPagerAdapter stopAdapter = new MyPagerAdapter(inflateVerStopCarViews(),null);
        vp_stop_car.setAdapter(stopAdapter);
        vp_stop_car.setCurrentItem(0);
        setVerStopCarTabScrollBar(0);
        vp_stop_car.setPagingEnabled(false);
    }

    private List<View> inflateVerStopCarViews() {
        View view_stab1 = View.inflate(mContext,R.layout.item_stop_car,null);
        mStopRV1 = (RecyclerView) view_stab1.findViewById(R.id.rv_menu_stop_car);
        mStopRV1.setLayoutManager(new GridLayoutManager(this,8));
        mStopRV1.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.HORIZONTAL_LIST));

        View view_stab2 = View.inflate(mContext,R.layout.item_stop_car1,null);
        mStopRV2 = (RecyclerView) view_stab2.findViewById(R.id.rv_menu_stop_car);
        mStopRV2.setLayoutManager(new GridLayoutManager(this,8));
        mStopRV2.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.HORIZONTAL_LIST));

        stopViews.add(view_stab1);
        stopViews.add(view_stab2);
        return stopViews;
    }

    private void initVerWaitCarViewPager(View view) {
        tv_wtab1 = (TextView) view.findViewById(R.id.tv_wtab1);
        tv_wtab2 = (TextView) view.findViewById(R.id.tv_wtab2);
        tv_wtab3 = (TextView) view.findViewById(R.id.tv_wtab3);
        tv_wtab1.setOnClickListener(this);
        tv_wtab2.setOnClickListener(this);
        tv_wtab3.setOnClickListener(this);
        tv_steward_send = (TextView) view.findViewById(R.id.tv_steward_send);

        vp_wait_car = (CustomViewPager) view.findViewById(R.id.vp_wait_car);
        MyPagerAdapter wAdapter = new MyPagerAdapter(inflateVerWaitCarViews(),null);
        vp_wait_car.setAdapter(wAdapter);
        vp_wait_car.setCurrentItem(0);
        setVerWaitCarTabScrollBar(0);
        vp_wait_car.setPagingEnabled(false);
    }

    private List<View> inflateVerWaitCarViews() {
        View view_wtab = View.inflate(mContext, R.layout.item_wait_car1,null);
        viewCover = (View) view_wtab.findViewById(R.id.view_cover); 
        mSendRV1 = (DragListView) view_wtab.findViewById(R.id.lv_send_car);
        mSendRV1_tv_steward_show = (TextView) view_wtab.findViewById(R.id.tv_steward_show);
        View view_wtab2 = View.inflate(mContext, R.layout.item_wait_car2,null);
        mSendRV2 = (DragListView) view_wtab2.findViewById(R.id.lv_send_car);
        mSendRV2_tv_steward_show = (TextView) view_wtab2.findViewById(R.id.tv_steward_show);
        View view_wtab3 = View.inflate(mContext, R.layout.item_wait_car2,null);
        mSendRV3 = (DragListView) view_wtab3.findViewById(R.id.lv_send_car);
        mSendRV3_tv_steward_show = (TextView) view_wtab3.findViewById(R.id.tv_steward_show);
        waitViews.add(view_wtab);
        waitViews.add(view_wtab2);
        waitViews.add(view_wtab3);
        return waitViews;
    }

    private View inflateHorStopCarView() {
        mHorStopCarView = new StopCarView(mContext, R.layout.item_hor_vp_stop_car,
                new StopCarView.OnStopCarTabListener() {
                    @Override
                    public void onTabIsClick(int pos) {
                        setVerStopCarViewStyle(pos);
                        setHorStopCarViewStyle(pos);
                    }
                });
        return mHorStopCarView;
    }

    private void setHorStopCarViewStyle(int i) {
        mHorStopCarView.setStopCarCurrentItem(i);
        mHorStopCarView.setStopCarTabScrollBar(i);
    }

    private void setVerStopCarViewStyle(int i) {
        vp_stop_car.setCurrentItem(i);
        setVerStopCarTabScrollBar(i);
    }

    private void initVerStartCarViewPager(View view) {
        tv_stab1 = (TextView) view.findViewById(R.id.tv_stab1);
        tv_stab2 = (TextView) view.findViewById(R.id.tv_stab2);
        tv_stab3 = (TextView) view.findViewById(R.id.tv_stab3);
        tv_stab1.setOnClickListener(this);
        tv_stab2.setOnClickListener(this);
        tv_stab3.setOnClickListener(this);
        tv_steward_gone = (TextView) view.findViewById(R.id.tv_steward_gone);
        vp_start_car = (CustomViewPager) view.findViewById(R.id.vp_start_car);

        MyPagerAdapter sAdapter = new MyPagerAdapter(inflateVerStartViews(),null);
        vp_start_car.setAdapter(sAdapter);
        vp_start_car.setCurrentItem(0);
        setVerStartCarTabScrollBar(0);
        selectAddRecording(true);
        vp_start_car.setPagingEnabled(false);
    }

    private List<View> inflateVerStartViews() {
        View view_stab = View.inflate(mContext, R.layout.item_gone_car1,null);
        mGoneRV1 = (RecyclerView) view_stab.findViewById(R.id.rv_gone_car);
        mGoneRV1_StewardShow = (TextView) view_stab.findViewById(tv_steward_show);
        mGoneRV1.setLayoutManager(new LinearLayoutManager(this));
        mGoneRV1.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        View view_stab2 = View.inflate(mContext, R.layout.item_gone_car2,null);
        mGoneRV2 = (RecyclerView) view_stab2.findViewById(R.id.rv_gone_car);
        mGoneRV2_StewardShow = (TextView) view_stab2.findViewById(tv_steward_show);
        mGoneRV2.setLayoutManager(new LinearLayoutManager(this));
        mGoneRV2.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        View view_stab3 = View.inflate(mContext, R.layout.item_gone_car2,null);
        mGoneRV3 = (RecyclerView) view_stab3.findViewById(R.id.rv_gone_car);
        mGoneRV3_StewardShow = (TextView) view_stab3.findViewById(tv_steward_show);
        mGoneRV3.setLayoutManager(new LinearLayoutManager(this));
        mGoneRV3.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        startViews.add(view_stab);
        startViews.add(view_stab2);
        startViews.add(view_stab3);
        return startViews;
    }

    private View inflateHorWaitViews() {
        mHorWaitCarView = new WaitCarView(mContext, R.layout.item_hor_vp_wait_car,
                new WaitCarView.OnWaitCarTabListener() {
                    @Override
                    public void onTabIsClick(int pos) {
                        selectAddRecording(false);
                        setVerWaitCarViewStyle(pos);
                        setHorWaitCarViewStyle(pos);
                    }
                });
        return mHorWaitCarView;
    }

        private View inflateHorStartCarViews() {
        mHorStartCarView = new StartCarView(mContext, R.layout.item_hor_vp_start_car,
                new StartCarView.OnStartCarTabListener() {
                  @Override
                   public void onTabIsClick(int pos) {
                        selectAddRecording(true);
                        setVerStartCarViewStyle(pos);
                        setHorStartCarViewStyle(pos);
                 }
        });
        return mHorStartCarView;
    }


    private void showContentView(List<View> views) {
        MyPagerAdapter mAdapter = new MyPagerAdapter(views, null);
        vpMain.setAdapter(mAdapter);
        vpMain.setCurrentItem(0);
        vpMain.setPagingEnabled(false);
        setTabBackground(0);
        setTvBackground(1);
    }


    private void initHorContentView(View view) {
        vp_horizontal = (CustomViewPager) view.findViewById(R.id.vp_main_horizontal);
        View start_view = inflateHorStartCarViews();
        View wait_view = inflateHorWaitViews();
        View stop_view = inflateHorStopCarView();
        eViews.add(start_view);
        eViews.add(wait_view);
        eViews.add(stop_view);
        MyPagerAdapter eAdapter = new MyPagerAdapter(eViews, null);
        vp_horizontal.setAdapter(eAdapter);
        vp_horizontal.setCurrentItem(1);
        setScrollBarBackground(1);
        vp_horizontal.setPagingEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 10);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        eStopRV.setLayoutManager(layoutManager);
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
    public void loadSendCarList(DragListAdapter mDragListAdapter) {   // 待发tab1
        isHaveSendCar = false;
        wtab1_size = mDragListAdapter.getCount();
        setWaitCarCount();
        tv_wtab1.setText(showCount(R.string.line_operate,mDragListAdapter.getCount()));

        DragListView.MyDragListener mListener = createMyDragListener();
        mSendRV1.setAdapter(mDragListAdapter);
        mSendRV1.setMyDragListener(mListener);

        mHorWaitCarView.setTab1tWaitCarCount(mDragListAdapter);
        mHorWaitCarView.setAdapterForNormal(mDragListAdapter);
        mHorWaitCarView.setMyDragListener(mListener);

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
    public void loadGoneCarByNormal(GoneAdapterForNormal goneAdapter) { // 已发tab1
        stab1_size = goneAdapter.getCount();
        setStartCarCount();
        tv_stab1.setText(showCount(R.string.line_operate,goneAdapter.getCount()));
        mGoneRV1.setAdapter(goneAdapter);

        mHorStartCarView.setTab1tStartCarCount(goneAdapter);
        mHorStartCarView.setEGoneRVAdapterForNormal(goneAdapter);
    }

    @Override
    public void loadStopStayCarList(List<StopHistory> stopHistories) { // 停场tab1
        ptab1_size = stopHistories.size()-1;
        setStopCarCount();
        tv_ver_stop_tab1.setText(showCount(R.string.stop_stay,ptab1_size));
        StopStayAdapter mAdapter = createStopStayAdapter(stopHistories);
        mStopRV1.setAdapter(mAdapter);

        mHorStopCarView.setTab1tStopCarCount(mAdapter);
        mHorStopCarView.setAdapterForStay(mAdapter);
//        eStopRV.setAdapter(mAdapter);
    }

    private String showCount(int stringRes,int carCount){
        String format= mContext.getResources().getString(stringRes);
        return String.format(format,carCount);
    }


    @Override
    public void loadStopEndCarList(List<StopHistory> stopHistories) { //停场tab2
        ptab2_size = stopHistories.size();
        setStopCarCount();
        tv_ver_stop_tab2.setText(showCount(R.string.stop_end,stopHistories.size()));
        StopEndAdapter mAdapter = createStopEndAdapter(stopHistories);
        mStopRV2.setAdapter(mAdapter);

        mHorStopCarView.setTab2tStopCarCount(mAdapter);
        mHorStopCarView.setAdapterForEnd(mAdapter);

    }

    private StopEndAdapter createStopEndAdapter(List<StopHistory> stopHistories) {
        return new StopEndAdapter(stopHistories, this, new StopEndAdapter.OnClickStopEndCarListListener() {
            @Override
            public void onClickStopEndCarListener(StopHistory stopCar) {
                showEndToStayDialog(stopCar);
            }
        });
    }

    private void showEndToStayDialog(final StopHistory stopCar) {
        new StopCarEndToStayDialog(mContext, new StopCarEndToStayDialog.OnStopCarEndToStayListener() {
            @Override
            public void onConfirm() {
                presenter.stopCarEndToStay(stopCar.id);
            }
        });
    }

    private StopStayAdapter createStopStayAdapter(List<StopHistory> stopHistories) {
        return new StopStayAdapter(stopHistories, this, new StopStayAdapter.OnClickStopCarListListener() {
            @Override
            public void onClickManualButtonListener() {
                showManualAddStopCarDialog();
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
    public void refreshTimeToSendCarNum(List<VehicleNumberBean> sendCarNum) {
        mLineAdapter.setSendCarNum(sendCarNum);

    }

    @Override
    public void nonMissionTypeDialog(NonMissionTypeAdapter adapter) {
        new NoMissionTypeWaitCarDialog(mContext,adapter);
    }




    private void setStartCarCount() {
        sCount = stab1_size+stab2_size+stab3_size;
        String format= mContext.getResources().getString(R.string.start_car_count);
        String startCount = String.format(format,sCount);
        tvMenuDepart.setText(startCount);
        tvMenuGoneCar.setText(startCount);
        DebugLog.i("已发车辆："+startCount);
    }


    private void setWaitCarCount() {
        wCount = wtab1_size+wtab2_size+wtab3_size;
        String format= mContext.getResources().getString(R.string.wait_car_count);
        String waitCount = String.format(format,wCount);
        tvVerWaitCar.setText(waitCount);
        tvMenuWaitCar.setText(waitCount);
        DebugLog.i("待发车辆："+waitCount);
    }


    private void setStopCarCount() {
        tCount= ptab1_size+ptab2_size;
        String format= mContext.getResources().getString(R.string.stop_car_count);
        String stopCount = String.format(format,tCount);
        tvVerStopCar.setText(stopCount);
        tvMenuStopCar.setText(stopCount);
        DebugLog.i("停场车辆："+stopCount);
    }

    @Override
    public void hideStewardName() {
        isShowStewardName(View.GONE);
    }

    @Override
    public void showStewardName() {
        isShowStewardName(View.VISIBLE);
    }

    @Override
    public void onGetAddRecordingTaskNameList(List<MissionType> missionTypes) {
        this.mMissionTypes = missionTypes;
    }


    @Override
    public void loadGoneCarByOperatorEmpty(GoneAdapterForOperatorEmpty goneAdapter) { // 已发tab2
        stab2_size = goneAdapter.getCount();
        setStartCarCount();
        tv_stab2.setText(showCount(R.string.operator_empty,goneAdapter.getCount()));
        mGoneRV2.setAdapter(goneAdapter);

        mHorStartCarView.setTab2tStartCarCount(goneAdapter);
        mHorStartCarView.setEGoneRVAdapterForOperatorEmpty(goneAdapter);
    }

    @Override
    public void loadGoneCarByNotOperatorEmpty(GoneAdapterForNotOperatorEmpty goneAdapter) { // 已发tab3
        stab3_size = goneAdapter.getCount();
        setStartCarCount();
        tv_stab3.setText(showCount(R.string.not_operator_empty,goneAdapter.getCount()));
        mGoneRV3.setAdapter(goneAdapter);

        mHorStartCarView.setTab3tStartCarCount(goneAdapter);
        mHorStartCarView.setEGoneRVAdapterForNotOperatorEmpty(goneAdapter);
    }

    @Override
    public void loadSendCarForOperatorEmpty(DragListAdapterForOperatorEmpty mDragListAdapter) {  // 待发tab2
        wtab2_size = mDragListAdapter.getCount();
        setWaitCarCount();
        tv_wtab2.setText(showCount(R.string.operator_empty,mDragListAdapter.getCount()));
        mSendRV2.setAdapter(mDragListAdapter);

        mHorWaitCarView.setTab2tWaitCarCount(mDragListAdapter);
        mHorWaitCarView.setAdapterForOperatorEmpty(mDragListAdapter);

    }

    @Override
    public void loadSendCarForNotOperatorEmpty(DragListAdapterForNotOperatorEmpty mDragListAdapter) { // 待发tab3
        wtab3_size = mDragListAdapter.getCount();
        setWaitCarCount();
        tv_wtab3.setText(showCount(R.string.not_operator_empty,mDragListAdapter.getCount()));
        mSendRV3.setAdapter(mDragListAdapter);

        mHorWaitCarView.setTab3tWaitCarCount(mDragListAdapter);
        mHorWaitCarView.setAdapterForNotOperatorEmpty(mDragListAdapter);
    }

    private void isShowStewardName(int isVisible) {
        mGoneRV1_StewardShow.setVisibility(isVisible);
        mGoneRV2_StewardShow.setVisibility(isVisible);
        mGoneRV3_StewardShow.setVisibility(isVisible);
        mSendRV1_tv_steward_show.setVisibility(isVisible);
        mSendRV2_tv_steward_show.setVisibility(isVisible);
        mSendRV3_tv_steward_show.setVisibility(isVisible);

        mHorWaitCarView.setStewardSendVisibility(isVisible);
        mHorStartCarView.setStewardGoneVisibility(isVisible);
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
                presenter.stopCarStayToEnd(stopCar.id);
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
        presenter.onAddRecordingCarTaskNameList(line.lineId);
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
                selectAddRecording(true);
                break;
            case 1:
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                if (isPopbg) {
                    llMenuWaitDepart.setVisibility(View.GONE);
                    selectAddRecording(true);
                }else{
                    llMenuWaitDepart.setVisibility(View.VISIBLE);
                    selectAddRecording(false);
                }
                break;
            case 2:
                tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                llMenuWaitDepart.setVisibility(View.GONE);
                selectAddRecording(false);
                break;
        }
    }

    private void setVerStartCarTabScrollBar(int pos){
        switch (pos){
            case 0:
                tv_stab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab3.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_stab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_stab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 1:
                tv_stab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab3.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_stab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_stab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 2:
                tv_stab3.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_stab3.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_stab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_stab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;

        }
    }


    private void setVerWaitCarTabScrollBar(int pos){
        switch (pos){
            case 0:
                tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab3.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_wtab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 1:
                tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab3.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_wtab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 2:
                tv_wtab3.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_wtab3.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;

        }
    }
    private void setVerStopCarTabScrollBar(int pos){
        switch (pos){
            case 0:
                tv_ver_stop_tab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_ver_stop_tab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_ver_stop_tab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_ver_stop_tab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
                break;
            case 1:
                tv_ver_stop_tab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_ver_stop_tab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
                tv_ver_stop_tab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                tv_ver_stop_tab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
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
//            case R.id.rl_menu_background:
            case R.id.img_menu_on_off:
                changeControlDeckView();
                break;
            case R.id.tv_menu_gone_car:
                vp_horizontal.setCurrentItem(0);
                setScrollBarBackground(0);
                isClickWaitCar = false;
                llMenuWaitDepart.setVisibility(View.GONE);
                break;
            case R.id.tv_menu_wait_car:
                vp_horizontal.setCurrentItem(1);
                setScrollBarBackground(1);
                isClickWaitCar = true;
                break;
            case R.id.tv_menu_stop_car:
                vp_horizontal.setCurrentItem(2);
                setScrollBarBackground(2);
                isClickWaitCar = false;
                break;
            // 已发车辆(垂直方向)
            case R.id.tv_stab1:
                setVerStartCarViewStyle(0);
                setHorStartCarViewStyle(0);
                selectAddRecording(true);
                break;
            case R.id.tv_stab2:
                setVerStartCarViewStyle(1);
                setHorStartCarViewStyle(1);
                selectAddRecording(true);
                break;
            case R.id.tv_stab3:
                setVerStartCarViewStyle(2);
                setHorStartCarViewStyle(2);
                selectAddRecording(true);
                break;
            // 待发车辆(水平方向)
            case R.id.tv_wtab1:
                setVerWaitCarViewStyle(0);
                setHorWaitCarViewStyle(0);
                break;
            case R.id.tv_wtab2:
                setVerWaitCarViewStyle(1);
                setHorWaitCarViewStyle(1);
                break;
            case R.id.tv_wtab3:
                setVerWaitCarViewStyle(2);
                setHorWaitCarViewStyle(2);
                break;
            // 停场车辆<垂直方向
            case R.id.tv_ver_stop_tab1:
                setVerStopCarViewStyle(0);
                setHorStopCarViewStyle(0);
                break;
            case R.id.tv_ver_stop_tab2:
                setVerStopCarViewStyle(1);
                setHorStopCarViewStyle(1);
                break;
            case R.id.tv_add_recording:
                    new AddRecordingCarTaskDialog(mContext,mMissionTypes,
                            new AddRecordingCarTaskDialog.OnAddRecordingCarTaskListener() {
                               @Override
                               public void OnAddRecordingCarTask(String type, String taskId, String vehicleId, String driverId,
                                                          String beginTime, String endTime, String runNum, String runEmpMileage) {
                                  presenter.addRecordingCarTask(vehicleId,driverId,type,taskId,runNum,runEmpMileage,beginTime,endTime);
                               }
                    });
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


    private void selectAddRecording(boolean isSelect){
        if (isSelect){
            tvAddRecroding.setVisibility(View.VISIBLE);
        }else{
            tvAddRecroding.setVisibility(View.GONE);
        }
    }

    private void setVerStartCarViewStyle(int i){
        vp_start_car.setCurrentItem(i);
        setVerStartCarTabScrollBar(i);
    }

    private void setHorStartCarViewStyle(int i){
        mHorStartCarView.setStartCarCurrentItem(i);
        mHorStartCarView.setStartCarTabScrollBar(i);
    }


    private void setVerWaitCarViewStyle(int i){
        vp_wait_car.setCurrentItem(i);
        setVerWaitCarTabScrollBar(i);
    }

    private void setHorWaitCarViewStyle(int i){
        mHorWaitCarView.setWaitCarCurrentItem(i);
        mHorWaitCarView.setWaitCarTabScrollBar(i);
    }

    private void setCoverBackground(int isVisible) {
        viewCover.setVisibility(isVisible);
        mHorWaitCarView.setViewCoverVisibility(isVisible);
    }

    private void changeControlDeckView() {
        if (isShow){
            initMenu(isShow);
            fl_horizontal.setVisibility(View.GONE);
            fl_vertical.setVisibility(View.VISIBLE);
            selectAddRecording(true);
            isPopbg = isShow;
            isShow = false;
        }else{
            initMenu(isShow);
            fl_vertical.setVisibility(View.GONE);
            fl_horizontal.setVisibility(View.VISIBLE);
            selectAddRecording(false);
            isPopbg = isShow;
            isShow = true;
        }
    }

    private void initMenu(boolean isShow) {
        if (isShow){
            llMenuWaitDepart.setVisibility(View.GONE);
            tvMenuWaitCar.setVisibility(View.GONE);
            tvMenuGoneCar.setVisibility(View.GONE);
            tvMenuStopCar.setVisibility(View.GONE);
            tvMenuDepart.setVisibility(View.VISIBLE);
            llTabVerStartCat.setVisibility(View.VISIBLE);
            rlMenuBackground.setBackgroundColor(mContext.getResources().getColor(R.color.background_gray5));
            rlMenuBackground.setEnabled(true);
        }else{

            if (isClickWaitCar) {
                llMenuWaitDepart.setVisibility(View.VISIBLE);
            }else{
                llMenuWaitDepart.setVisibility(View.GONE);
            }

            tvMenuDepart.setVisibility(View.GONE);
            llTabVerStartCat.setVisibility(View.GONE);
            tvMenuWaitCar.setVisibility(View.VISIBLE);
            tvMenuGoneCar.setVisibility(View.VISIBLE);
            tvMenuStopCar.setVisibility(View.VISIBLE);
            rlMenuBackground.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rlMenuBackground.setEnabled(false);
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
        presenter.closeTimer();
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
