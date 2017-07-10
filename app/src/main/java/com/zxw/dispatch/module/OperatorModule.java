package com.zxw.dispatch.module;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxw.data.bean.LineParams;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.MyPagerAdapter;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.view.ChildViewPager;
import com.zxw.dispatch.view.CustomViewPager;
import com.zxw.dispatch.view.DragListView;
import com.zxw.dispatch.view.StartCarView;
import com.zxw.dispatch.view.StopCarView;
import com.zxw.dispatch.view.WaitCarView;

import java.util.ArrayList;
import java.util.List;

import static com.zxw.dispatch.R.id.tv_steward_show;

/**
 * author：CangJie on 2017/5/17 16:11
 * email：cangjie2016@gmail.com
 */
public class OperatorModule extends LinearLayout implements View.OnClickListener {

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

    RecyclerView mGoneRV1, mGoneRV2, mGoneRV3;
    DragListView mSendRV1, mSendRV2, mSendRV3;

    View viewCover;
    TextView tvAutomatic;
    TextView tvManual;
    TextView tvVerWaitCar;
    TextView tvVerStopCar;

    private TextView mGoneRV1_StewardShow, mGoneRV2_StewardShow, mGoneRV3_StewardShow;
    private TextView mSendRV1_tv_steward_show, mSendRV2_tv_steward_show, mSendRV3_tv_steward_show;
    private StopCarView mHorStopCarView;
    private RecyclerView mStopRV1, mStopRV2;
    private boolean isShow = false;

    private final Context mContext;
    private int lineId;
    private LineParams mLineParams;
    private OnLoadingListener listener;
    private FrameLayout fl_vertical, fl_horizontal;
    private ChildViewPager vp_stop_car;

    private List<View> startViews = new ArrayList<View>();
    private List<View> waitViews = new ArrayList<View>();
    private List<View> stopViews = new ArrayList<View>();
    private ChildViewPager vp_wait_car;

    private boolean isPopbg = true;
    private StartCarView mHorStartCarView;
    private WaitCarView mHorWaitCarView;

    public OperatorModule(Context context) {
        this(context, null);
    }

    public OperatorModule(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tab_view_control_deck1, this);
        this.mContext = context;

        fl_vertical = (FrameLayout) findViewById(R.id.fl_vertical);
        fl_horizontal = (FrameLayout) findViewById(R.id.fl_horizontal);
        initMenuView(this);
        initVerContentView(this);
        initHorContentView(this);
        // init

    }

    public void initLineParams(int lineId, LineParams params) {
        this.lineId = lineId;
        this.mLineParams = params;
//        loadSchedulePlan(String.valueOf(currentYear), String.valueOf(currentMonth), String.valueOf(currentDay));
    }
        String userId = SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);


    /**
     * 菜单栏
     * @param view
     */
    private void initMenuView(View view) {
        tvMenuDepart = (TextView) view.findViewById(R.id.tv_menu_depart_car);
        rlMenuBackground = (RelativeLayout) view.findViewById(R.id.rl_menu_background);
        llMenuWaitDepart = (LinearLayout) view.findViewById(R.id.ll_menu_wait_depart);
        tvAddRecroding = (TextView) view.findViewById(R.id.tv_add_recording);// 补录
        tvMenuAutomatic = (TextView) view.findViewById(R.id.tv_menu_automatic);// 自动发车
        tvMenuManual = (TextView) view.findViewById(R.id.tv_menu_manual);  // 手动发车
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
        tvAutomatic = (TextView) view.findViewById(R.id.tv_automatic);
        tvManual = (TextView) view.findViewById(R.id.tv_manual);
        tvAutomatic.setOnClickListener(this);
        tvManual.setOnClickListener(this);
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
        mStopRV1.setLayoutManager(new GridLayoutManager(mContext,8));

        View view_stab2 = View.inflate(mContext,R.layout.item_stop_car1,null);
        mStopRV2 = (RecyclerView) view_stab2.findViewById(R.id.rv_menu_stop_car);
        mStopRV2.setLayoutManager(new GridLayoutManager(mContext,8));

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
//      tv_steward_send = (TextView) view.findViewById(R.id.tv_steward_send);
        vp_wait_car = (ChildViewPager) view.findViewById(R.id.vp_wait_car);

        MyPagerAdapter wAdapter = new MyPagerAdapter(inflateVerWaitCarViews(),null);
        vp_wait_car.setAdapter(wAdapter);
        vp_wait_car.setCurrentItem(0);
        setVerWaitCarTabScrollBar(0);
        showMenuAutoDepart(View.VISIBLE);
        vp_wait_car.setPagingEnabled(false);
    }

    public void showMenuAutoDepart(int isVisible){
        llMenuWaitDepart.setVisibility(isVisible);
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

    private void showStopCarView(int i){
        vp_stop_car.setCurrentItem(i);
        mHorStopCarView.setStopCarCurrentItem(i);
        setVerStopCarTabScrollBar(i);
        mHorStopCarView.setStopCarTabScrollBar(i);
    }

    private void showVerAutoDepart(int isVisible) {
        tvManual.setVisibility(isVisible);
        tvAutomatic.setVisibility(isVisible);
    }

    // 水平方向的自动/手动发车
    private void showHorAutoDepart(int isVisible) {
        if (isPopbg){
            llMenuWaitDepart.setVisibility(View.GONE);
        }else{
            llMenuWaitDepart.setVisibility(isVisible);
        }
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
            isPopbg = isShow;
            isShow = false;
        }else{
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


    private void showAddRecordingBtn(int isVisible){
        tvAddRecroding.setVisibility(isVisible);
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
        mGoneRV1 = (RecyclerView) view_stab.findViewById(R.id.rv_gone_car);
        mGoneRV1_StewardShow = (TextView) view_stab.findViewById(tv_steward_show);
        mGoneRV1.setLayoutManager(new LinearLayoutManager(mContext));
        mGoneRV1.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));

        View view_stab2 = View.inflate(mContext, R.layout.item_gone_car2,null);
        mGoneRV2 = (RecyclerView) view_stab2.findViewById(R.id.rv_gone_car);
        mGoneRV2_StewardShow = (TextView) view_stab2.findViewById(tv_steward_show);
        mGoneRV2.setLayoutManager(new LinearLayoutManager(mContext));
        mGoneRV2.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL_LIST));

        View view_stab3 = View.inflate(mContext, R.layout.item_gone_car2,null);
        mGoneRV3 = (RecyclerView) view_stab3.findViewById(R.id.rv_gone_car);
        mGoneRV3_StewardShow = (TextView) view_stab3.findViewById(tv_steward_show);
        mGoneRV3.setLayoutManager(new LinearLayoutManager(mContext));
        mGoneRV3.addItemDecoration(new DividerItemDecoration(mContext,
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

    private void showWaitCarView(int i,int visible) {
        vp_wait_car.setCurrentItem(i);
        setVerWaitCarTabScrollBar(i);
        mHorWaitCarView.setWaitCarCurrentItem(i);
        mHorWaitCarView.setWaitCarTabScrollBar(i);
        showVerAutoDepart(visible);
        showHorAutoDepart(visible);

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

                    }

                    @Override
                    public void onLoadMoreEGone() {

                    }
                });
        return mHorStartCarView;
    }

    private void showStartCarView(int i){
        vp_start_car.setCurrentItem(i);
        setVerStartCarTabScrollBar(i);
        mHorStartCarView.setStartCarCurrentItem(i);
        mHorStartCarView.setStartCarTabScrollBar(i);
        showAddRecordingBtn(View.VISIBLE);
    }


    private void initHorContentView(View view) {
        vp_horizontal = (CustomViewPager) view.findViewById(R.id.vp_main_horizontal);
        View start_view = inflateHorStartCarViews();
        View wait_view = inflateHorWaitViews();
        View stop_view = inflateHorStopCarView();
        List<View> eViews = new ArrayList<View>();
        eViews.add(start_view);
        eViews.add(wait_view);
        eViews.add(stop_view);
        MyPagerAdapter eAdapter = new MyPagerAdapter(eViews, null);
        vp_horizontal.setAdapter(eAdapter);
        vp_horizontal.setCurrentItem(1);
        setScrollBarBackground(1);
        vp_horizontal.setPagingEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 10);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        eStopRV.setLayoutManager(layoutManager);
    }

    public void setOnLoadingListener(OnLoadingListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {{
        switch (v.getId()) {
            case R.id.img_menu_on_off:
                changeControlDeckView();
                break;
            case R.id.tv_menu_gone_car:
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
//                new RecordingCarTaskDialog(mContext, presenter.getLineParams(),presenter.getLineId(), new RecordingCarTaskDialog.OnAddRecordingListener(){
//                    @Override
//                    public void onClickNormalMission(int type, int taskId, String vehicleId, String driverId, String stewardId,String startTime,String endTime) {
//
//                        presenter.addRecordingCarTask(vehicleId,driverId,stewardId,String.valueOf(type),String.valueOf(taskId), null, null, null, startTime, endTime);
//                    }
//                    @Override
//                    public void onClickOperatorEmptyMissionDoConfirm(int type, int taskType, String vehicleId, String driverId, String stewardId, String startTime, String endTime, String runCount, String km, String remarks) {
//                        presenter.addRecordingCarTask(vehicleId,driverId,stewardId,String.valueOf(type),null, String.valueOf(taskType),runCount,km,startTime,endTime);
//                    }
//                    @Override
//                    public void onClickOperatorNotEmptyMissionDoConfirm(int type, int taskType, String vehicleId, String driverId, String stewardId, String startTime, String endTime, String runCount, String km, String remarks) {
//                        presenter.addRecordingCarTask(vehicleId,driverId,stewardId,String.valueOf(type),null, String.valueOf(taskType),runCount,km,startTime,endTime);
//                    }
//
//                    @Override
//                    public void onClickHelpMission(int type, int taskId, String vehicleId, String driverId, String stewardId,String startTime,String endTime) {
//                        presenter.addRecordingCarTask(vehicleId,driverId,stewardId,String.valueOf(type),String.valueOf(taskId), null, null, null,startTime,endTime);
//                    }
//                });
                listener.disPlay("点击补录按钮");

                break;
//            // 自动发车
//            case R.id.tv_automatic:
//            case R.id.tv_menu_automatic:
//                if ((System.currentTimeMillis() - clickTime) > 1000) {
//                    if (!isAuto) {
//                        if (!isHaveSendCar) {
//                            showToast("该线路没有待发车辆", mContext);
//                            return;
//                        }
//                        setTvBackground(2);
//                        setCoverBackground(View.VISIBLE);
//                        //动态注册广播接收器
//                        createReceiver();
//                        presenter.selectAuto();
//                        clickTime = System.currentTimeMillis();
//                        isAuto = true;
//                    }
//                }
//                break;
//            // 手动发车
//            case R.id.tv_manual:
//            case R.id.tv_menu_manual:
//                if ((System.currentTimeMillis() - clickTime) > 1000) {
//                    if (isAuto) {
//                        setTvBackground(1);
//                        setCoverBackground(View.GONE);
//                        presenter.selectManual();
//                        clickTime = System.currentTimeMillis();
//                        isAuto = false;
//                    }
//                }
//                break;
        }
    }

    }

    private void setScrollBarBackground(int pos){
        switch (pos){
            case 0:
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                showMenuAutoDepart(View.GONE);
                showAddRecordingBtn(View.VISIBLE);
                break;
            case 1:
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getDrawable(false));
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tvMenuStopCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
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
                tvMenuWaitCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                tvMenuGoneCar.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                showMenuAutoDepart(View.GONE);
                showAddRecordingBtn(View.GONE);
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

    public interface OnLoadingListener {
        void showLoading();

        void hideLoading();

        void disPlay(String str);
    }

}
