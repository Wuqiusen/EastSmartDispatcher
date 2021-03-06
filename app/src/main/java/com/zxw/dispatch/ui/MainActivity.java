
package com.zxw.dispatch.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.RunningCarBean;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.bean.VehicleNumberBean;
import com.zxw.dispatch.Constants;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.adapter.DragListAdapterForNotOperatorEmpty;
import com.zxw.dispatch.adapter.DragListAdapterForOperatorEmpty;
import com.zxw.dispatch.adapter.MyPagerAdapter;
import com.zxw.dispatch.adapter.PopupAdapter;
import com.zxw.dispatch.module.ScheduleModule;
import com.zxw.dispatch.presenter.BasePresenter;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.GoneAdapterForNotOperatorEmpty;
import com.zxw.dispatch.recycler.GoneAdapterForOperatorEmpty;
import com.zxw.dispatch.recycler.MainAdapter;
import com.zxw.dispatch.recycler.NonMissionTypeAdapter;
import com.zxw.dispatch.recycler.StopEndAdapter;
import com.zxw.dispatch.recycler.StopStayAdapter;
import com.zxw.dispatch.recycler.viewHolder.GoneForNormalViewHolder;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.utils.CreateRecyclerView;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.view.ChildViewPager;
import com.zxw.dispatch.view.CustomViewPager;
import com.zxw.dispatch.view.DragListView;
import com.zxw.dispatch.view.LineRunMapView;
import com.zxw.dispatch.view.MyDialog;
import com.zxw.dispatch.view.StartCarView;
import com.zxw.dispatch.view.StopCarView;
import com.zxw.dispatch.view.WaitCarView;
import com.zxw.dispatch.view.WorkLoadView;
import com.zxw.dispatch.view.dialog.ManualAddStopCarDialog;
import com.zxw.dispatch.view.dialog.MissionTypeWaitCarDialog;
import com.zxw.dispatch.view.dialog.NoMissionTypeWaitCarDialog;
import com.zxw.dispatch.view.dialog.RecordingCarTaskDialog;
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
import butterknife.OnClick;

import static com.zxw.dispatch.R.id.tv_steward_show;
import static com.zxw.dispatch.utils.ToastHelper.showToast;


public class MainActivity extends PresenterActivity<MainPresenter> implements MainView, MainAdapter.OnSelectLineListener,
        PopupAdapter.OnPopupWindowListener, View.OnClickListener, ScheduleModule.OnLoadingListener,
        SwipeRefreshLayout.OnRefreshListener , RecyclerArrayAdapter.OnLoadMoreListener{


    TextView tvMenuDepart;
    RelativeLayout rlMenuBackground;
//    LinearLayout llMenuWaitDepart;
    TextView tvAddRecroding; // 补录
//    TextView tvMenuAutomatic;
//    TextView tvMenuManual;
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
    ChildViewPager vp_start_car;
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
    ChildViewPager vp_wait_car, vp_stop_car;
    private List<View> startViews = new ArrayList<View>();
    private List<View> waitViews = new ArrayList<View>();
    private List<View> stopViews = new ArrayList<View>();

    private StartCarView mHorStartCarView;
    private WaitCarView mHorWaitCarView;
    // 标题栏
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_system_date)
    TextView tvDate;
    // 操控台
    @Bind(R.id.tv_controller)
    TextView tvController;
    // 排班计划
    @Bind(R.id.tv_schedule)
    TextView tvSchedule;
    // 工作量审核
    @Bind(R.id.tv_work_load)
    TextView tvWorkLoad;
    // 线路运行图
    @Bind(R.id.tv_line_run_map)
    TextView tvLineRunMap;


    // 设置
    @Bind(R.id.img_setting)
    ImageView imgSetting;
    // 设置
    @Bind(R.id.iv_call)
    ImageView iv_call;


    @Bind(R.id.rv_line)
    RecyclerView mLineRV;
    EasyRecyclerView mGoneRV1;
    RecyclerView  mGoneRV2, mGoneRV3;
    DragListView mSendRV1, mSendRV2, mSendRV3;
    private RecyclerArrayAdapter mGoneRV1Adapter;
    private int goneRv1PageNo = 1;
    private int eGoneRv1PageNo = 1;
    private LineParams mLineParams;


    View viewCover;
    @Bind(R.id.vp_main)
    CustomViewPager vpMain;
    TextView tvVerWaitCar;
    TextView tvVerStopCar;


    private MainAdapter mLineAdapter;
    private PopupAdapter popupAdapter;
    private PopupWindow mPopupWindow = null;
    private ListView lv_popup = null;
    private LinearLayout ll_popupwindow;
    private List<View> views = new ArrayList<View>();
    private List<View> eViews = new ArrayList<View>();
    private boolean isShow = false;
    private boolean isPopbg = true;

    private static final int SEND_CAR_COUNT = 4;
    private Timer mTimer = null;
    private int spotId;

    private int showingLineId;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case SEND_CAR_COUNT:
                        Log.w("onReceive---", "更新待发车辆数");
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
    private ScheduleModule scheduleModule;
//    private TextView tv_date;
//    private int currentYear;
//    private int currentMonth;
//    private int currentDay;
    private WorkLoadView mWorkLoadView;
    private LineRunMapView mLineRunMapView;
    private Bundle mSavedInstanceState;
    private StopStayAdapter stopStayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        this.mSavedInstanceState = savedInstanceState;
        ButterKnife.bind(this);
        initData();
        initView();
        initTabEvent();
        spotId = getIntent().getIntExtra("spotId", -1);
        presenter.loadLineList(spotId);
//        presenter.checkStopCar();
    }


    private void initData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        tvDate.setText(formatter.format(curDate));

        BitmapDrawable bitmap = (BitmapDrawable) mContext.getResources().getDrawable(R.mipmap.my_icon);
        bitmap.setBounds(0,0,60,60);
        tvUserName.setText(SpUtils.getCache(mContext, SpUtils.NAME));
        tvUserName.setCompoundDrawables(bitmap,null,null,null);



    }

    private void initView() {
        hideHeadArea();
        hideTitle();
        // 操控台
        views.add(initControlDeckView());
        // 排班计划
        scheduleModule = new ScheduleModule(mContext);
        scheduleModule.setOnLoadingListener(this);
        views.add(scheduleModule);
        // 工作量审核
        views.add(initWorkLoadVerifyView());
        // 线路运行图（新）
        views.add(initLineRunMapView());
        // 显示默认视图
        showContentView(views);
    }

    private View initLineRunMapView() {
        mLineRunMapView = new LineRunMapView(mContext,presenter, mSavedInstanceState);
        return  mLineRunMapView;
    }

    private View initWorkLoadVerifyView() {
        mWorkLoadView = new WorkLoadView(mContext, new WorkLoadView.OnListener() {
            @Override
            public void onClickCalendar(String date) {

            }

            @Override
            public void onClickToReport() {

            }

            @Override
            public void onEditTextChanged(String str, int type) {

            }
            @Override
            public void showLoading() {
                DebugLog.w("showloading");
                MainActivity.this.showLoading();
            }

            @Override
            public void hideLoading() {
                MainActivity.this.hideLoading();
            }

        });
        return mWorkLoadView;
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
        tvAddRecroding = (TextView) view.findViewById(R.id.tv_add_recording);// 补录
//        llMenuWaitDepart = (LinearLayout) view.findViewById(R.id.ll_menu_wait_depart);
//        tvMenuAutomatic = (TextView) view.findViewById(R.id.tv_menu_automatic);// 自动发车
//        tvMenuManual = (TextView) view.findViewById(R.id.tv_menu_manual);  // 手动发车
        tvMenuWaitCar = (TextView) view.findViewById(R.id.tv_menu_wait_car);
        tvMenuGoneCar = (TextView) view.findViewById(R.id.tv_menu_gone_car);
        llTabVerStartCat = (LinearLayout) view.findViewById(R.id.ll_tab_ver_start_car);

        tvMenuStopCar = (TextView) view.findViewById(R.id.tv_menu_stop_car);
        imgOnOff = (ImageView) view.findViewById(R.id.img_menu_on_off);
        tvAddRecroding.setOnClickListener(this);
        tvMenuWaitCar.setOnClickListener(this);
        tvMenuGoneCar.setOnClickListener(this);
        tvMenuStopCar.setOnClickListener(this);

//        tvMenuAutomatic.setOnClickListener(this);
//        tvMenuManual.setOnClickListener(this);
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
//        tvAutomatic = (TextView) view.findViewById(R.id.tv_automatic);
//        tvManual = (TextView) view.findViewById(R.id.tv_manual);
//        tvAutomatic.setOnClickListener(this);
//        tvManual.setOnClickListener(this);
    }

    private void initVerStopCarViewPager(View view) {
        tv_ver_stop_tab1 = (TextView) view.findViewById(R.id.tv_ver_stop_tab1);
        tv_ver_stop_tab2 = (TextView) view.findViewById(R.id.tv_ver_stop_tab2);
        tv_ver_stop_tab1.setOnClickListener(this);
        tv_ver_stop_tab2.setOnClickListener(this);
        vp_stop_car = (ChildViewPager) view.findViewById(R.id.vp_stop_car);

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

        View view_stab2 = View.inflate(mContext,R.layout.item_stop_car1,null);
        mStopRV2 = (RecyclerView) view_stab2.findViewById(R.id.rv_menu_stop_car);
        mStopRV2.setLayoutManager(new GridLayoutManager(this,8));

        stopViews.add(view_stab1);
        stopViews.add(view_stab2);
        return stopViews;
    }

    private void initVerWaitCarViewPager(View view) {
        tv_wtab1 = (TextView) view.findViewById(R.id.tv_wtab1);
        tv_wtab2 = (TextView) view.findViewById(R.id.tv_wtab2);
        tv_wtab3 = (TextView) view.findViewById(R.id.tv_wtab3);
        view.findViewById(R.id.btn_group_message).setOnClickListener(this);
        tv_wtab1.setOnClickListener(this);
        tv_wtab2.setOnClickListener(this);
        tv_wtab3.setOnClickListener(this);
        vp_wait_car = (ChildViewPager) view.findViewById(R.id.vp_wait_car);

        MyPagerAdapter wAdapter = new MyPagerAdapter(inflateVerWaitCarViews(),null);
        vp_wait_car.setAdapter(wAdapter);
        vp_wait_car.setCurrentItem(0);
        setVerWaitCarTabScrollBar(0);
        showMenuAutoDepart(View.VISIBLE);
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
                        showAddRecordingBtn(View.GONE);
                        showMenuAutoDepart(View.GONE);
                        showStopCarView(pos);
                    }
                });
        return mHorStopCarView;
    }



    private void initVerStartCarViewPager(View view) {
        tv_stab1 = (TextView) view.findViewById(R.id.tv_stab1);
        tv_stab2 = (TextView) view.findViewById(R.id.tv_stab2);
        tv_stab3 = (TextView) view.findViewById(R.id.tv_stab3);
        tv_stab1.setOnClickListener(this);
        tv_stab2.setOnClickListener(this);
        tv_stab3.setOnClickListener(this);
        vp_start_car = (ChildViewPager) view.findViewById(R.id.vp_start_car);

        MyPagerAdapter sAdapter = new MyPagerAdapter(inflateVerStartViews(),null);
        vp_start_car.setAdapter(sAdapter);
        vp_start_car.setCurrentItem(0);
        setVerStartCarTabScrollBar(0);
        showAddRecordingBtn(View.VISIBLE);
        vp_start_car.setPagingEnabled(false);
    }

    private List<View> inflateVerStartViews() {
        View view_stab = View.inflate(mContext, R.layout.item_gone_car1,null);
        mGoneRV1 = (EasyRecyclerView) view_stab.findViewById(R.id.rv_gone_car);
        mGoneRV1_StewardShow = (TextView) view_stab.findViewById(tv_steward_show);
        mGoneRV1Adapter = new RecyclerArrayAdapter(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new GoneForNormalViewHolder(parent, mLineParams, presenter);
            }
        };
        new CreateRecyclerView().CreateRecyclerView(mContext, mGoneRV1, mGoneRV1Adapter, this);
        mGoneRV1.setRefreshListener(this);

//        mGoneRV1.setLayoutManager(new LinearLayoutManager(this));
//        mGoneRV1.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
//        mGoneRV1Adapter = new RecyclerArrayAdapter() {
//            @Override
//            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
//                return new GoneForNormalViewHolder(parent,);
//            }
//
//        };
//        new CreateRecyclerView().CreateRecyclerView(mContext, mGoneRV1, arrayAdapter, this);
//        mGoneRV1.setRefreshListener(this);

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
                    public void onTabIsClick(int pos,int isVisible) {
                        showAddRecordingBtn(View.GONE);
                        showWaitCarView(pos,isVisible);
                    }
                });
        return mHorWaitCarView;
    }

        private View inflateHorStartCarViews() {
        mHorStartCarView = new StartCarView(mContext, R.layout.item_hor_vp_start_car,
                new StartCarView.OnStartCarTabListener() {
                  @Override
                   public void onTabIsClick(int pos) {
                        showStartCarView(pos);
                 }

                    @Override
                    public void onRefreshEGone() {
                        eGoneRv1PageNo = 1;
                        presenter.loadGoneCarByNormal(eGoneRv1PageNo);

                    }

                    @Override
                    public void onLoadMoreEGone() {
                        if (eGoneRv1PageNo * Constants.PAGE_SIZE >= stab1_size) {
                                mHorStartCarView.geteGoneRV1Adapter().stopMore();
                        } else {
                            eGoneRv1PageNo++;
                            presenter.loadGoneCarByNormal(eGoneRv1PageNo);
                        }
                    }
                });
            mHorStartCarView.setEGoneRVAdapterForNormal(presenter);
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
    }

    private void initTabEvent() {
         /*控制台*/
        tvController.setOnClickListener(this);
         /*排班计划*/
        tvSchedule.setOnClickListener(this);
        /*工作量*/
        tvWorkLoad.setOnClickListener(this);
        /*线路运行图*/
        tvLineRunMap.setOnClickListener(this);
        /*设置按钮*/
        imgSetting.setOnClickListener(this);
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
        presenter.checkStopCar();
        mLineRV.setLayoutManager(new LinearLayoutManager(this));
        mLineAdapter = new MainAdapter(lineList, this, this);
        mLineRV.setAdapter(mLineAdapter);
        mLineRV.addItemDecoration(new DividerItemDecoration(this, R.color.white,
                DividerItemDecoration.VERTICAL_LIST));

        refreshSendCarTimer();
    }

    @Override
    public void reLogin() {
//        //取消自动发车
//        if (isAuto) {
//            presenter.closeService();
//            isAuto = false;
//        }
//        SpUtils.logOut(MyApplication.mContext);
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();
    }

    @Override
    public void loadSendCarList(DragListAdapter mDragListAdapter) {
        wtab1_size = mDragListAdapter.getCount();
        setWaitCarCount();
        tv_wtab1.setText(showCount(R.string.line_operate,mDragListAdapter.getCount()));

        DragListView.MyDragListener mListener = createMyDragListener();
        mSendRV1.setAdapter(mDragListAdapter);
        mSendRV1.setMyDragListener(mListener);

        mHorWaitCarView.setTab1tWaitCarCount(mDragListAdapter);
        mHorWaitCarView.setAdapterForNormal(mDragListAdapter);
        mHorWaitCarView.setMyDragListener(mListener);

    }

    private DragListView.MyDragListener createMyDragListener() {
        return new DragListView.MyDragListener() {
            @Override
            public void onDragFinish(int srcPositon, int finalPosition) {

            }
        };
    }


    @Override
    public void loadGoneCarByNormal(List<SendHistory> sendHistories, int count) {
        stab1_size = count;
        setStartCarCount();
        tv_stab1.setText(showCount(R.string.line_operate,count));  //mGoneRV1

        mHorStartCarView.setTab1tStartCarCount(count);
        if (isShow){
            if (eGoneRv1PageNo == 1)
                mHorStartCarView.geteGoneRV1Adapter().clear();
            mHorStartCarView.addGoneRV1Data(sendHistories);
        }else {
            if (goneRv1PageNo == 1)mGoneRV1Adapter.clear();
            mGoneRV1Adapter.addAll(sendHistories);
        }

    }

    @Override
    public void loadStopStayCarList(List<StopHistory> stopHistories) {
        ptab1_size = stopHistories.size()-1;
        setStopCarCount();
        tv_ver_stop_tab1.setText(showCount(R.string.stop_stay,ptab1_size));
        stopStayAdapter = createStopStayAdapter(stopHistories);
        mStopRV1.setAdapter(stopStayAdapter);

        mHorStopCarView.setTab1tStopCarCount(stopStayAdapter);
        mHorStopCarView.setAdapterForStay(stopStayAdapter);
//      eStopRV.setAdapter(mAdapter);
    }

    private String showCount(int stringRes,int carCount){
        String format= mContext.getResources().getString(stringRes);
        return String.format(format,carCount);
    }


    @Override
    public void loadStopEndCarList(List<StopHistory> stopHistories) {
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

           @Override
           public void onLongClickStopCarListener(String objId) {
               presenter.vehicleStopRemove(objId);
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
    public void setSaleType(LineParams lineParams) {
        this.mLineParams = lineParams;
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
    }

    private void setWaitCarCount() {
        wCount = wtab1_size+wtab2_size+wtab3_size;
        String format= mContext.getResources().getString(R.string.wait_car_count);
        String waitCount = String.format(format,wCount);
        tvVerWaitCar.setText(waitCount);
        tvMenuWaitCar.setText(waitCount);
    }

    private void setStopCarCount() {
        tCount= ptab1_size+ptab2_size;
        String format= mContext.getResources().getString(R.string.stop_car_count);
        String stopCount = String.format(format,tCount);
        tvVerStopCar.setText(stopCount);
        tvMenuStopCar.setText(stopCount);
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
    }


    @Override
    public void loadGoneCarByOperatorEmpty(GoneAdapterForOperatorEmpty goneAdapter) {
        stab2_size = goneAdapter.getCount();
        setStartCarCount();
        tv_stab2.setText(showCount(R.string.operator_empty,goneAdapter.getCount()));
        mGoneRV2.setAdapter(goneAdapter);
        mHorStartCarView.setTab2tStartCarCount(goneAdapter);
        mHorStartCarView.setEGoneRVAdapterForOperatorEmpty(goneAdapter);
    }

    @Override
    public void loadGoneCarByNotOperatorEmpty(GoneAdapterForNotOperatorEmpty goneAdapter) {
        stab3_size = goneAdapter.getCount();
        setStartCarCount();
        tv_stab3.setText(showCount(R.string.not_operator_empty,goneAdapter.getCount()));
        mGoneRV3.setAdapter(goneAdapter);
        mHorStartCarView.setTab3tStartCarCount(goneAdapter);
        mHorStartCarView.setEGoneRVAdapterForNotOperatorEmpty(goneAdapter);
    }

    @Override
    public void loadSendCarForOperatorEmpty(DragListAdapterForOperatorEmpty mDragListAdapter) {
        wtab2_size = mDragListAdapter.getCount();
        setWaitCarCount();
        tv_wtab2.setText(showCount(R.string.operator_empty,mDragListAdapter.getCount()));
        mSendRV2.setAdapter(mDragListAdapter);
        mHorWaitCarView.setTab2tWaitCarCount(mDragListAdapter);
        mHorWaitCarView.setAdapterForOperatorEmpty(mDragListAdapter);

    }

    @Override
    public void loadSendCarForNotOperatorEmpty(DragListAdapterForNotOperatorEmpty mDragListAdapter) {
        wtab3_size = mDragListAdapter.getCount();
        setWaitCarCount();
        tv_wtab3.setText(showCount(R.string.not_operator_empty,mDragListAdapter.getCount()));
        mSendRV3.setAdapter(mDragListAdapter);
        mHorWaitCarView.setTab3tWaitCarCount(mDragListAdapter);
        mHorWaitCarView.setAdapterForNotOperatorEmpty(mDragListAdapter);
    }

    @Override
    public void onSelectLine() {
        loadData(vpMain.getCurrentItem());
    }
    //根据当前所在页面加载数据
    private void loadData(int page){
        switch (page){
            case 0:
                if (isShow){
                    eGoneRv1PageNo = 1;
                }else {
                    goneRv1PageNo = 1;
                }
                presenter.refreshList();
                break;
            case 1:
                // 排班计划
                scheduleModule.initLineParams(presenter.getLineId(), presenter.getLineParams());
                break;
            case 2:
                // 工作量审核
                mWorkLoadView.initLineParams(presenter.getLineId(), presenter.getLineParams());
                break;
            case 3:
                // 2).切换线路id，重新访问车辆列表
                mLineRunMapView.initLineParams(presenter.getLineId());
                break;
        }
    }

    @Override
    public void sendSuccessRunCarCodeList(List<RunningCarBean> runCarList) {
        DebugLog.e("start draw markers at map:"+"---");
        mLineRunMapView.refreshRunMapView(runCarList);
    }

    @Override
    public void sendFailedRunCarCodeList() {
        mLineRunMapView.noRefreshRunMapView();
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


    private boolean isCreateDialog = false;
    private VehicleToScheduleDialog vehicleToScheduleDialog;
    private void showVehicleToScheduleDialog(final StopHistory stopCar) {
       if (!isCreateDialog)
        createVehicleToScheduleDialog(stopCar);
    }

    private void createVehicleToScheduleDialog(final StopHistory stopCar) {
        isCreateDialog = true;
        vehicleToScheduleDialog = new VehicleToScheduleDialog(mContext, stopCar, new VehicleToScheduleDialog.OnClickListener() {
            @Override
            public void onClickNormalMission(int type, int taskId, BasePresenter.LoadDataStatus loadDataStatus) {
                presenter.stopCarMission(stopCar, type, String.valueOf(taskId),null, null, null, null, null,null, loadDataStatus);
            }

            @Override
            public void onClickOperatorEmptyMission(int type, int taskType, String beginTime,
                                                    String endTime, String runNum, String runEmpMileage,String remarks, BasePresenter.LoadDataStatus loadDataStatus) {
                presenter.stopCarMission(stopCar, type, null, String.valueOf(taskType), beginTime, endTime, runNum, runEmpMileage,remarks, loadDataStatus);
            }

            @Override
            public void onClickOperatorNotEmptyMission(int type, int taskType, String beginTime,
                                                       String endTime, String runNum, String runEmpMileage,String remarks, BasePresenter.LoadDataStatus loadDataStatus) {
                presenter.stopCarMission(stopCar, type, null, String.valueOf(taskType), beginTime, endTime, runNum, runEmpMileage,remarks, loadDataStatus);
            }

            @Override
            public void onClickHelpMission(int type, int taskId, BasePresenter.LoadDataStatus loadDataStatus) {
                presenter.stopCarMission(stopCar, type, String.valueOf(taskId),null, null, null, null, null,null, loadDataStatus);
            }

            @Override
            public void onOffDuty(BasePresenter.LoadDataStatus loadDataStatus) {
                presenter.stopCarStayToEnd(stopCar.id, loadDataStatus);
            }
            @Override
            public void onDismiss() {
                isCreateDialog = false;
            }

            @Override
            public void showDialog(AlertDialog alertDialog) {
                if (showingLineId != vehicleToScheduleDialog.getLineId()){
                    vehicleToScheduleDialog.setDismiss();
                }
            }

        }, presenter.getLineId());
    }


    private void showManualAddStopCarDialog() {
        new ManualAddStopCarDialog(mContext, presenter.getLineParams(),presenter.getLineId(), new ManualAddStopCarDialog.OnManualAddStopCarListener() {
            @Override
            public void manualAddStopCar(String carId, String driverId, String stewardId, BasePresenter.LoadDataStatus loadDataStatus) {
                presenter.manualAddStopCar(carId, driverId, stewardId, loadDataStatus);
            }
        });
    }

    @Override
    public void onSelectLine(Line line) {
        if (stopStayAdapter != null)
        stopStayAdapter.refreshData();
        //传递线路id查询是否该线路已开启自动发车
        Intent intent = new Intent("com.zxw.dispatch.service.RECEIVER");
        intent.putExtra("type", "getData");
        intent.putExtra("lineKey", line.lineId);
        sendBroadcast(intent);
        if (vehicleToScheduleDialog != null && line.lineId != vehicleToScheduleDialog.getLineId()){
            vehicleToScheduleDialog.setDismiss();
        }
        showingLineId = line.lineId;
        presenter.onSelectLine(line);
        presenter.onAddRecordingCarTaskNameList(line.lineId);
//        loadData(vpMain.getCurrentItem());
//        vpMain.setCurrentItem(0);
//        setTabBackground(0);
    }

    /**
     * 监听Back键按下事件
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭当前Activity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mLineRunMapView.clearSubscribe();
        mLineRunMapView.clearMap();
        mLineRunMapView.clearSelectVehicleCode();
        DebugLog.e("super.onBackPressed()会自动调用finish()方法,关闭当前Activity.-----");
    }

    private void setTabBackground(int tabPosition) {
        setNormalTabStyle();
        switch (tabPosition) {
            case 0:
                tvController.setTextColor(mContext.getResources().getColor(R.color.background_bg_blue));
                tvController.setBackground(mContext.getResources().getDrawable(R.drawable.tab_white_rectangle));
                break;
            case 1:
                tvSchedule.setTextColor(mContext.getResources().getColor(R.color.background_bg_blue));
                tvSchedule.setBackground(mContext.getResources().getDrawable(R.drawable.tab_white_rectangle));
                break;
            case 2:
                tvWorkLoad.setTextColor(mContext.getResources().getColor(R.color.background_bg_blue));
                tvWorkLoad.setBackground(mContext.getResources().getDrawable(R.drawable.tab_white_rectangle));
                break;
            case 3:
                tvLineRunMap.setTextColor(mContext.getResources().getColor(R.color.background_bg_blue));
                tvLineRunMap.setBackground(mContext.getResources().getDrawable(R.drawable.tab_white_rectangle));
                break;
            case 4:
                break;
        }
    }

    private void setScrollBarBackground(int pos){
        setScrollBarNormalStyle();
        switch (pos){
            case 0:
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                showMenuAutoDepart(View.GONE);
                showAddRecordingBtn(View.VISIBLE);
                break;
            case 1:
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                if (isPopbg) {
                    showMenuAutoDepart(View.GONE);
                    showAddRecordingBtn(View.VISIBLE);
                }else{
                    showMenuAutoDepart(View.VISIBLE);
                    showAddRecordingBtn(View.GONE);
                }
                break;
            case 2:
                tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                showMenuAutoDepart(View.GONE);
                showAddRecordingBtn(View.GONE);
                break;
        }
    }


    private void setVerStartCarTabScrollBar(int pos){
        setStartCarTabNormalStyle();
        switch (pos){
            case 0:
                tv_stab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 1:
                tv_stab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 2:
                tv_stab3.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_stab3.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;

        }
    }


    private void setVerWaitCarTabScrollBar(int pos){
        setWaitCarTabNormalStyle();
        switch (pos){
            case 0:
                tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 1:
                tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 2:
                tv_wtab3.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_wtab3.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
        }
    }

    private void setVerStopCarTabScrollBar(int pos){
        setTopCarTabNormalStyle();
        switch (pos){
            case 0:
                tv_ver_stop_tab1.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_ver_stop_tab1.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 1:
                tv_ver_stop_tab2.setTextColor(mContext.getResources().getColor(R.color.white));
                tv_ver_stop_tab2.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
        }
    }


    private void setScrollBarNormalStyle() {
        tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
    }

    private void setTopCarTabNormalStyle() {
        tv_ver_stop_tab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_ver_stop_tab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_ver_stop_tab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
        tv_ver_stop_tab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
    }


    private void setNormalTabStyle() {
        tvController.setTextColor(mContext.getResources().getColor(R.color.font_gray));
        tvSchedule.setTextColor(mContext.getResources().getColor(R.color.font_gray));
        tvWorkLoad.setTextColor(mContext.getResources().getColor(R.color.font_gray));
        tvLineRunMap.setTextColor(mContext.getResources().getColor(R.color.font_gray));
        tvController.setBackgroundColor(mContext.getResources().getColor(R.color.background_deep_blue));
        tvSchedule.setBackgroundColor(mContext.getResources().getColor(R.color.background_deep_blue));
        tvWorkLoad.setBackgroundColor(mContext.getResources().getColor(R.color.background_deep_blue));
        tvLineRunMap.setBackgroundColor(mContext.getResources().getColor(R.color.background_deep_blue));

    }


    private void setStartCarTabNormalStyle() {
        tv_stab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_stab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_stab3.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_stab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
        tv_stab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
        tv_stab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
    }


    private void setWaitCarTabNormalStyle() {
        tv_wtab1.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_wtab2.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_wtab3.setTextColor(mContext.getResources().getColor(R.color.font_black));
        tv_wtab1.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
        tv_wtab2.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
        tv_wtab3.setBackground(mContext.getResources().getDrawable(R.drawable.whitebtn_dialog_deep_style));
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
            case R.id.tv_controller:
               loadData(0);
                 vpMain.setCurrentItem(0);
                 setTabBackground(0);
                 break;
            case R.id.tv_schedule:
                // 排班计划
                loadData(1);
                vpMain.setCurrentItem(1);
                setTabBackground(1);
//                scheduleModule.loadSchedulePlan();
                break;
            case R.id.tv_work_load:
                // 工作量审核
                loadData(2);
                vpMain.setCurrentItem(2);
                setTabBackground(2);
                break;
            case R.id.tv_line_run_map:
                // 2).切换线路id，重新访问车辆列表
                loadData(3);
                vpMain.setCurrentItem(3);
                setTabBackground(3);
                ///presenter.loadRunningCarCodeList();////
                break;
            case R.id.img_setting:
                showPopupWindow();
                break;
            case R.id.img_menu_on_off:
                changeControlDeckView();
                break;
            case R.id.tv_menu_gone_car:
                eGoneRv1PageNo = 1;
                presenter.loadGoneCarByNormal(eGoneRv1PageNo);
                vp_horizontal.setCurrentItem(0);
                setScrollBarBackground(0);
                showMenuAutoDepart(View.GONE);
                break;
            case R.id.tv_menu_wait_car:
                vp_horizontal.setCurrentItem(1);
                setScrollBarBackground(1);
                break;
            case R.id.tv_menu_stop_car:
                vp_horizontal.setCurrentItem(2);
                setScrollBarBackground(2);
                break;
            // 已发车辆(垂直方向)
            case R.id.tv_stab1:
                showStartCarView(0);
                break;
            case R.id.tv_stab2:
                showStartCarView(1);
                break;
            case R.id.tv_stab3:
                showStartCarView(2);
                break;
            // 待发车辆(垂直方向)
            case R.id.tv_wtab1:
                showWaitCarView(0,View.VISIBLE);
                break;
            case R.id.tv_wtab2:
                showWaitCarView(1,View.INVISIBLE);
                break;
            case R.id.tv_wtab3:
                showWaitCarView(2,View.INVISIBLE);
                break;
            // 停场车辆(垂直方向)
            case R.id.tv_ver_stop_tab1:
                showStopCarView(0);
                break;
            case R.id.tv_ver_stop_tab2:
                showStopCarView(1);
                break;
            case R.id.tv_add_recording:

                // 新的1:
                new RecordingCarTaskDialog(mContext, presenter.getLineParams(),presenter.getLineId(), new RecordingCarTaskDialog.OnAddRecordingListener(){
                    @Override
                    public void onClickNormalMission(int type, int taskId, String vehicleId, String driverId, String stewardId, String startTime, String endTime, BasePresenter.LoadDataStatus loadDataStatus) {

                        presenter.addRecordingCarTask(vehicleId,driverId,stewardId,String.valueOf(type),String.valueOf(taskId), null, null, null, startTime, endTime, loadDataStatus);
                    }
                    @Override
                    public void onClickOperatorEmptyMissionDoConfirm(int type, int taskType, String vehicleId, String driverId, String stewardId,
                                                                     String startTime, String endTime, String runCount, String km, String remarks, BasePresenter.LoadDataStatus loadDataStatus) {
                        presenter.addRecordingCarTask(vehicleId,driverId,stewardId,String.valueOf(type),null, String.valueOf(taskType),runCount,km,startTime,endTime, loadDataStatus);
                    }
                    @Override
                    public void onClickOperatorNotEmptyMissionDoConfirm(int type, int taskType, String vehicleId, String driverId, String stewardId,
                                                                        String startTime, String endTime, String runCount, String km, String remarks, BasePresenter.LoadDataStatus loadDataStatus) {
                        presenter.addRecordingCarTask(vehicleId,driverId,stewardId,String.valueOf(type),null, String.valueOf(taskType),runCount,km,startTime,endTime, loadDataStatus);
                    }

                    @Override
                    public void onClickHelpMission(int type, int taskId, String vehicleId, String driverId, String stewardId,String startTime,String endTime, BasePresenter.LoadDataStatus loadDataStatus) {
                        presenter.addRecordingCarTask(vehicleId,driverId,stewardId,String.valueOf(type),String.valueOf(taskId), null, null, null,startTime,endTime, loadDataStatus);
                    }
                });

                break;
            case R.id.btn_group_message:
                Intent intent = new Intent(this, GroupMessageActivity.class);
                intent.putExtra("lineId", presenter.getLineId());
                startActivity(intent);
                break;
        }
    }

    @OnClick(R.id.iv_call)
    public void call(){
        startActivity(new Intent(MainActivity.this, CallLoginActivity.class));
    }

    private void showAddRecordingBtn(int isVisible){
        tvAddRecroding.setVisibility(isVisible);
    }


    private void showStartCarView(int i){
        vp_start_car.setCurrentItem(i);
        setVerStartCarTabScrollBar(i);
        mHorStartCarView.setStartCarCurrentItem(i);
        mHorStartCarView.setStartCarTabScrollBar(i);
        showAddRecordingBtn(View.VISIBLE);
    }

    private void showWaitCarView(int i,int visible) {
        vp_wait_car.setCurrentItem(i);
        setVerWaitCarTabScrollBar(i);
        mHorWaitCarView.setWaitCarCurrentItem(i);
        mHorWaitCarView.setWaitCarTabScrollBar(i);
//        showHorAutoDepart(visible);

    }

    private void showStopCarView(int i){
        vp_stop_car.setCurrentItem(i);
        mHorStopCarView.setStopCarCurrentItem(i);
        setVerStopCarTabScrollBar(i);
        mHorStopCarView.setStopCarTabScrollBar(i);
    }

//    private void showVerAutoDepart(int isVisible) {
//        tvManual.setVisibility(isVisible);
//        tvAutomatic.setVisibility(isVisible);
//    }
//
//    // 水平方向的自动/手动发车
//    private void showHorAutoDepart(int isVisible) {
//        if (isPopbg){
//            llMenuWaitDepart.setVisibility(View.GONE);
//        }else{
//            llMenuWaitDepart.setVisibility(isVisible);
//        }
//    }

    private void setCoverBackground(int isVisible) {
        viewCover.setVisibility(isVisible);
        mHorWaitCarView.setViewCoverVisibility(isVisible);
    }

    private void changeControlDeckView() {
        if (isShow){
            initMenu(isShow);
            fl_horizontal.setVisibility(View.GONE);
            fl_vertical.setVisibility(View.VISIBLE);
            isPopbg = isShow;
            isShow = false;
        }else{
            mHorStartCarView.setSaleType(presenter.getLineParams());
            initMenu(isShow);
            fl_vertical.setVisibility(View.GONE);
            fl_horizontal.setVisibility(View.VISIBLE);
            isPopbg = isShow;
            isShow = true;
        }

    }

    private void initMenu(boolean isShow) {
        if (isShow){
            tvMenuWaitCar.setVisibility(View.GONE);
            tvMenuGoneCar.setVisibility(View.GONE);
            tvMenuStopCar.setVisibility(View.GONE);
            tvMenuDepart.setVisibility(View.VISIBLE);
            llTabVerStartCat.setVisibility(View.VISIBLE);
            rlMenuBackground.setBackgroundColor(mContext.getResources().getColor(R.color.background_gray5));
            rlMenuBackground.setEnabled(true);
            showAddRecordingBtn(View.VISIBLE);
            showMenuAutoDepart(View.GONE);
        }else{
            tvMenuDepart.setVisibility(View.GONE);
            llTabVerStartCat.setVisibility(View.GONE);
            tvMenuWaitCar.setVisibility(View.VISIBLE);
            tvMenuGoneCar.setVisibility(View.VISIBLE);
            tvMenuStopCar.setVisibility(View.VISIBLE);
            rlMenuBackground.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rlMenuBackground.setEnabled(false);
            showAddRecordingBtn(View.GONE);
            showMenuAutoDepart(View.GONE);
        }
    }

    public void showMenuAutoDepart(int isVisible){
//        llMenuWaitDepart.setVisibility(isVisible);
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
        mPopupWindow.showAsDropDown(imgSetting,300,6);
    }

    @Override
    public void onPopupListener(int position) {
        switch (position) {
            case 0:
                showToast("修改资料", mContext);
                break;
            case 1:
                showToast("密码修改", mContext);
                break;
            case 2:
                isSureLoginOut();
                break;

        }
    }

    private void setTvBackground(int poi) {
//        if (poi == 1) {
//            tvManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_select_style));
//            tvAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_normal_style));
//            tvManual.setTextColor(getResources().getColor(R.color.white));
//            tvAutomatic.setTextColor(getResources().getColor(R.color.font_black));
//
//            tvMenuManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_select_style));
//            tvMenuAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_normal_style));
//            tvMenuManual.setTextColor(getResources().getColor(R.color.white));
//            tvMenuAutomatic.setTextColor(getResources().getColor(R.color.font_black));
//
//        } else {
//            tvManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_normal_style));
//            tvAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_select_style));
//            tvManual.setTextColor(getResources().getColor(R.color.font_black));
//            tvAutomatic.setTextColor(getResources().getColor(R.color.white));
//
//            tvMenuManual.setBackground(getResources().getDrawable(R.drawable.tv_manual_normal_style));
//            tvMenuAutomatic.setBackground(getResources().getDrawable(R.drawable.tv_automatic_select_style));
//            tvMenuManual.setTextColor(getResources().getColor(R.color.font_black));
//            tvMenuAutomatic.setTextColor(getResources().getColor(R.color.white));
//        }
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        presenter.closeTimer();
        super.onDestroy();
        mLineRunMapView.onMapDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLineRunMapView.mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLineRunMapView.mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLineRunMapView.mLocationClient.stopLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLineRunMapView.mMapView.onSaveInstanceState(outState);
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
        SpUtils.logOut(mContext);
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRefresh() {
        mGoneRV1Adapter.clear();
        goneRv1PageNo = 1;
        presenter.loadGoneCarByNormal(goneRv1PageNo);

    }

    @Override
    public void onLoadMore() {
        if (goneRv1PageNo * Constants.PAGE_SIZE >= stab1_size) {
            mGoneRV1Adapter.stopMore();
        } else {
            goneRv1PageNo++;
            presenter.loadGoneCarByNormal(goneRv1PageNo);
        }
    }




}
