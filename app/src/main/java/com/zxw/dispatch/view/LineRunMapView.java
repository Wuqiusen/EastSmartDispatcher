
package com.zxw.dispatch.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.zxw.data.bean.RunningCarBean;
import com.zxw.data.http.HttpGPsRequest;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.recycler.VehicleCodeListAdapter;
import com.zxw.dispatch.utils.DebugLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static java.util.Collections.swap;


public class LineRunMapView extends LinearLayout implements View.OnClickListener, LocationSource, AMapLocationListener
        , AMap.OnMapClickListener, AMap.OnMapLoadedListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {

    private Context mContext;
    private MainPresenter mPresenter;
    private AMap mMap;
    public MapView mMapView;
    private SlidingDrawer mSlidingDrawer;
    private ImageView mImgHandle;
    private TextView mRefreshBtn;
    private EditText mEtVehicleCode;
    private RecyclerView mRv;
    private LinearLayout llQueryTab;
    private TextView tvAll;
    private TextView tvOnLine;
    private TextView tvNoOnLine;
    private TextView tvLoadMsg;
    private RelativeLayout relAnimationLoading;
    private LinearLayout llCarList;
    private Marker mCurrentMarker;
    public AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private Subscription runCarSubscription;
    private VehicleCodeListAdapter mRvAdapter;
    private List<Marker> mMarkerList = new ArrayList<>();
    private LatLngBounds.Builder mBuilder = new LatLngBounds.Builder();
    private List<RunningCarBean> mList = new ArrayList<>();
    private List<RunningCarBean> mOnAllList = new ArrayList<>();
    private List<RunningCarBean> mOnLineList = new ArrayList<>();
    private List<RunningCarBean> mOnNoLineList = new ArrayList<>();
    private static boolean isDrawFinish = false;
    public static String mSelectedVehicleCode = null;
    private String mCurrentMarkerTitle = null;
    private String isClickTab = "0";
    private static int mI = 0;
    private  Bundle mSavedInstanceState;


    public LineRunMapView(Context context, MainPresenter presenter, Bundle savedInstanceState) {
        super(context);
        this.mContext = context;
        this.mSavedInstanceState = savedInstanceState;
        this.mPresenter = presenter;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_line_run_map_new, this);
        initView();
        initEvent();
        initMap();
    }


    private void initView() {
        mSlidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        mImgHandle = (ImageView) findViewById(R.id.iv_handle);
        llCarList = (LinearLayout) findViewById(R.id.ll_car_list);
        llQueryTab = (LinearLayout) findViewById(R.id.ll_query_tab);
        tvAll = (TextView) findViewById(R.id.tv_query_all);
        tvOnLine = (TextView) findViewById(R.id.tv_on_line);
        tvNoOnLine = (TextView) findViewById(R.id.tv_no_on_line);
        relAnimationLoading = (RelativeLayout) findViewById(R.id.rel_run_map_loading);
        tvLoadMsg = (TextView) findViewById(R.id.tv_load_msg);

        mRefreshBtn = (TextView) findViewById(R.id.tv_refresh_vehicleCode);
        mEtVehicleCode = (EditText) findViewById(R.id.et_query_vehicleCode);
        mRv = (RecyclerView) findViewById(R.id.rv_vehicleCode_list);
        relAnimationLoading.getBackground().setAlpha(230);
        llCarList.getBackground().setAlpha(230);
        llQueryTab.getBackground().setAlpha(230);
        mEtVehicleCode.getBackground().setAlpha(230);
        mRefreshBtn.getBackground().setAlpha(230);
        mRv.getBackground().setAlpha(230);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(layoutManager);

    }


    private void initEvent() {
        mSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                mImgHandle.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_close_station));
            }
        });

        mSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                mImgHandle.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_open_station));
            }
        });

    }



    private void initMap() {
        mMapView = (MapView) findViewById(R.id.mv_map2d);
        mMapView.onCreate(mSavedInstanceState);
        mMap = mMapView.getMap();
        mMap.setTrafficEnabled(true);
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        if (mMap != null) {
            mMap.setLocationSource(this);// 设置定位监听
            mMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            mMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            mMap.setMyLocationStyle(myLocationStyle);
            mMap.setOnMapLoadedListener(this);
            mMap.setOnMapClickListener(this);
        }
    }

    private void setNormalTabBg(int c1, int c2) {
        int white = mContext.getResources().getColor(c1);
        int black = mContext.getResources().getColor(c2);
        tvAll.setTextColor(black);
        tvOnLine.setTextColor(black);
        tvNoOnLine.setTextColor(black);
        tvAll.setBackgroundColor(white);
        tvOnLine.setBackgroundColor(white);
        tvNoOnLine.setBackgroundColor(white);
    }

    private void setTabBackground(int tabPosition) {
        setNormalTabBg(R.color.font_white, R.color.font_black);
        llQueryTab.setVisibility(View.VISIBLE);
        switch (tabPosition) {
            case 0:
                tvAll.setTextColor(mContext.getResources().getColor(R.color.font_blue2));
                tvAll.setBackground(mContext.getResources().getDrawable(R.drawable.tab_blue_rectangle));
                break;
            case 1:
                tvNoOnLine.setTextColor(mContext.getResources().getColor(R.color.font_blue2));
                tvNoOnLine.setBackground(mContext.getResources().getDrawable(R.drawable.tab_blue_rectangle));
                break;
            case 2:
                tvOnLine.setTextColor(mContext.getResources().getColor(R.color.font_blue2));
                tvOnLine.setBackground(mContext.getResources().getDrawable(R.drawable.tab_blue_rectangle));
                break;
        }
    }


    private void clearEtVehicle() {
        String etStr = mEtVehicleCode.getText().toString().trim();
        if (!TextUtils.isEmpty(etStr)) {
            mEtVehicleCode.setText(null);
        }
    }

    public void initLineParams(int lineId) {
        setOnRefreshListener(lineId);
        mPresenter.loadRunningCarCodeList(lineId);
    }


    public void setOnRefreshListener(final int lineId) {
        mRefreshBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMap();
                clearEtVehicle();
                showLoading(View.VISIBLE, View.GONE, View.GONE);
                mPresenter.loadRunningCarCodeList(lineId);
            }
        });
    }


    public void noRefreshRunMapView() {
        showLoading(View.GONE, View.VISIBLE, View.GONE);
        initBaseView(View.VISIBLE, false);
    }

    public void refreshRunMapView(List<RunningCarBean> list) {
        clearSubscribe();
        drawMarkerList(list);
    }


    private void drawMarkerList(final List<RunningCarBean> list) {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(list);
        mCurrentMarkerTitle = null;
        mSelectedVehicleCode = null;
        setTabListener();
        runCarSubscription = Observable.interval(0, 30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                        showLoading(View.VISIBLE, View.GONE, View.GONE);

                        clearAdapter();

                        clearList();

                        clearMap();

                        clearEtVehicle();

                        initBaseView(View.VISIBLE, false);

                        mI = 0;
                        isDrawFinish = false;
                        for (int i = 0; i < mList.size(); i++) {
                            refreshMarkerLocation(i, mList.get(i));
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

    }


    public void clearSelectVehicleCode() {
        mSelectedVehicleCode = null;
        mCurrentMarkerTitle = null;
    }

    public void clearMap() {
        if (mMarkerList != null && mMarkerList.size() > 0) {
            mMarkerList.clear();
            mMap.clear();
        }
        if (mBuilder != null) {
            mBuilder = null;
        }

        mBuilder = new LatLngBounds.Builder();
    }

    private void clearAdapter() {
        if (mRvAdapter != null) {
            mRvAdapter.clearListener();
            mRvAdapter = null;
        }
    }

    private void clearList() {
        if (mOnAllList != null && mOnAllList.size() > 0) {
            mOnAllList.clear();
        }
        if (mOnLineList != null && mOnLineList.size() > 0) {
            mOnLineList.clear();
        }
        if (mOnNoLineList != null && mOnNoLineList.size() > 0) {
            mOnNoLineList.clear();
        }

    }

    private void initBaseView(int visible, boolean enabled) {
        showEtVehicleCode(visible);
        showRefreshBtn(visible);
        setTabStyle(enabled);
    }

    private void setTabStyle(boolean enabled) {
        setTabEnabled(enabled);
        setTabSize();
        setTabBackground(0);
    }

    private void showLoading(int v1, int v2, int v3) {
        relAnimationLoading.setVisibility(v1);
        tvLoadMsg.setVisibility(v2);
        mRv.setVisibility(v3);
    }

    private void setTabEnabled(boolean enabled) {
        tvAll.setEnabled(enabled);
        tvOnLine.setEnabled(enabled);
        tvNoOnLine.setEnabled(enabled);
    }

    private void showEtVehicleCode(int visible) {
        mEtVehicleCode.setVisibility(visible);
    }

    private void showRefreshBtn(int visible) {
        mRefreshBtn.setVisibility(visible);
    }


    private LatLng converterGps(HttpGPsRequest.GpsBaseBean gpsBean) {
        LatLng latLng = null;
        try {
            // 转Gps坐标系
            CoordinateConverter converter = new CoordinateConverter(mContext);
            // CoordType.GPS 待转换坐标类型
            converter.from(CoordinateConverter.CoordType.GPS);
            // sourceLatLng待转换坐标点 LatLng类型
            DPoint dPoint = new DPoint(gpsBean.gps.latitude, gpsBean.gps.longitude);
            converter.coord(dPoint);
            // 执行转换操作
            DPoint desLatLng = converter.convert();
            latLng = new LatLng(desLatLng.getLatitude(), desLatLng.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latLng;
    }


    private void refreshMarkerLocation(final int i, final RunningCarBean bean) {
        final String vehicleCode = bean.getVehicleCode();
        HttpGPsRequest.getInstance().gpsByVehCode(new Subscriber<HttpGPsRequest.GpsBaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                refreshMarkerLocation(i, bean);
            }

            @Override
            public void onNext(HttpGPsRequest.GpsBaseBean gpsBean) {
                try {
                    if (gpsBean.success) {
                        LatLng latLng = converterGps(gpsBean);
                        Marker marker = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                                .position(latLng)
                                .title(bean.getDriverName() + "," + bean.getVehicleCode())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_real_time_position))
                                .draggable(true)
                        );
                        marker.setVisible(false);
                        marker.hideInfoWindow();
                        setMarkerListener();

                        if (TextUtils.isEmpty(mCurrentMarkerTitle)) {
                            if (i == 0){
                                // 若定位始终失败，重新刷新车辆列表的时候，以为i=0为中心
                                mMap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                            }
                            marker.setVisible(true);
                        } else {
                            if (mCurrentMarkerTitle.equals(marker.getTitle())) {
                                marker.setVisible(true);
                                marker.showInfoWindow();
                                mMap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                            }
                        }

                        mMarkerList.add(marker);
                        mBuilder.include(latLng);

                        bean.setPoi(mI);

                        long mit = getMilliMinute(gpsBean);
                        String type = getRunCarBeanType(mit);
                        bean.setMilliMinute(mit);
                        bean.setType(type);

                        splitIntoDiffList(type, bean);

                        mOnAllList.add(bean);

                        mI++;
                        if (mI > 0 && mI == mList.size()) {

                            changeToDiffDescList();

                            if (mList != null && mList.size() > 0) {
                                mList.clear();
                            }

                            mList.addAll(mOnAllList);

                            setAdapter(mOnAllList);

                            isDrawFinish = true;

                            initBaseView(View.VISIBLE, true);

                            setEtVehicleListener();

                            showLoading(View.GONE, View.GONE, View.VISIBLE);

                            shownSlidingDrawer();

                        }
                    } else {
                        DebugLog.e("gps:failed:" + i + "___" + "---");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, vehicleCode);
    }

    private void changeToDiffDescList() {
        mOnAllList = changeIntoDescList(mOnAllList, "desc");
        mOnLineList = changeIntoDescList(mOnLineList, "desc");
        mOnNoLineList = changeIntoDescList(mOnNoLineList, "desc");
    }


    private void setTabSize() {
        if (mOnAllList.size() > 0) {
            tvAll.setText("全部(" + mOnAllList.size() + ")");
        } else {
            tvAll.setText("全部(0)");
        }
        if (mOnLineList.size() > 0) {
            tvOnLine.setText("在线(" + mOnLineList.size() + ")");
        } else {
            tvOnLine.setText("在线(0)");
        }
        if (mOnNoLineList.size() > 0) {
            tvNoOnLine.setText("离线(" + mOnNoLineList.size() + ")");
        } else {
            tvNoOnLine.setText("离线(0)");
        }
    }

    private void splitIntoDiffList(String type, final RunningCarBean bean) {
        if (!TextUtils.isEmpty(type)) {
            if (type.equals("1")) {
                mOnNoLineList.add(bean);
            } else if (type.equals("2")) {
                mOnLineList.add(bean);
            }
        }
    }


    private String getRunCarBeanType(long mit) {
        long min = mit / 1000 / 60;
        if (min <= 15) {
            return "2";
        } else {
            return "1";
        }
    }

    private List<RunningCarBean> changeIntoDescList(List<RunningCarBean> list, String sortType) {
        if (sortType.equals("asc")) {
            for (int i = 1; i < list.size(); i++) {
                for (int j = 0; j < list.size() - i; j++) {
                    if (list.get(j).getMilliMinute() > list.get(j + 1).getMilliMinute()) {
                        swap(list, j, j + 1);
                    }
                }
            }

        } else if (sortType.equals("desc")) {
            for (int i = 1; i < list.size(); i++) {
                for (int j = 0; j < list.size() - i; j++) {
                    if (list.get(j).getMilliMinute() < list.get(j + 1).getMilliMinute()) {
                        swap(list, j, j + 1);
                    }
                }
            }
        }

        return list;
    }


    private long getMilliMinute(HttpGPsRequest.GpsBaseBean gpsBean) {
        return setGpsMilliMinute(gpsBean.gps.dateInt, gpsBean.gps.timeInt);
    }


    /**
     * @param dateInt 日期: 20170615
     * @param timeInt 时分秒: 165206
     * @return
     */
    private long setGpsMilliMinute(int dateInt, int timeInt) {
        long milliSecond = 0;
        try {
            Date curDate = null;
            Date date = null;
            String t1, t2, hor1, min, sec;

            final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String curTime = df.format(new Date());
            curDate = df.parse(curTime);// 当前系统时间
            t1 = String.valueOf(timeInt);
            if (t1 != null && t1.length() == 5) {
                hor1 = t1.substring(0, 1);
                min = t1.substring(1, 3);
                sec = t1.substring(3);
            } else {
                hor1 = t1.substring(0, 2);
                min = t1.substring(2, 4);
                sec = t1.substring(4);
            }
            String hor2 = Integer.valueOf(hor1) < 10 ? "0" + hor1 : hor1;
            t2 = hor2 + min + sec;
            String dateStr = String.valueOf(dateInt) + t2;
            date = df.parse(dateStr.toString().trim());

            milliSecond = curDate.getTime() - date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Math.abs(milliSecond);
    }


    private void setTabListener() {
        tvAll.setOnClickListener(this);
        tvOnLine.setOnClickListener(this);
        tvNoOnLine.setOnClickListener(this);
    }

    private void shownSlidingDrawer() {
        mSlidingDrawer.setVisibility(View.VISIBLE);
        mSlidingDrawer.open();
    }


    private void setEtVehicleListener() {
        mEtVehicleCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputStr = s.toString().trim().toUpperCase();
                if (!TextUtils.isEmpty(isClickTab)) {
                    if (isClickTab.equals("0")) {
                        queryVehicleCode(inputStr, mList);
                    } else if (isClickTab.equals("1")) {
                        queryVehicleCode(inputStr, mOnNoLineList);
                    } else if (isClickTab.equals("2")) {
                        queryVehicleCode(inputStr, mOnLineList);
                    }
                }
            }
        });
    }


    private void queryVehicleCode(String inputStr, List<RunningCarBean> list) {
        if (list.size() == 0) {
            return;
        }
        List<RunningCarBean> mSendList = new ArrayList<>();
        if (TextUtils.isEmpty(inputStr)) {
            mSendList.addAll(list);
        } else {
            for (int i = 0; i < list.size(); i++) {
                String veh = list.get(i).getVehicleCode().trim();
                if (!TextUtils.isEmpty(veh) && veh.contains(inputStr)) {
                    mSendList.add(list.get(i));
                }
            }
        }

        setAdapter(mSendList);
    }


    public void clearSubscribe() {
        if (runCarSubscription != null) {
            runCarSubscription.unsubscribe();
            runCarSubscription = null;
        }
    }


    private void setMarkerListener() {
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(this);
    }


    private void setAdapter(List<RunningCarBean> list) {
        if (mRvAdapter != null) {
            mRvAdapter = null;
        }
        List<RunningCarBean> sendList = new ArrayList<>();
        sendList.addAll(list);
        mRvAdapter = new VehicleCodeListAdapter(mContext, sendList,
                new VehicleCodeListAdapter.OnRunItemClickListener() {
                    @Override
                    public void onItemClick(String vehicleCode) {
                        for (int i = 0; i < mMarkerList.size(); i++) {
                            Marker marker = mMarkerList.get(i);
                            String title = marker.getTitle();
                            String[] temp = title.split(",");
                            if (!TextUtils.isEmpty(vehicleCode) && vehicleCode.equals(temp[1])) {
                                mMap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

                                marker.setVisible(true);
                                marker.showInfoWindow();
                                mCurrentMarker = marker;
                                mCurrentMarkerTitle = marker.getTitle();
                            } else {
                                marker.setVisible(false);
                                marker.hideInfoWindow();
                            }
                        }
                    }
                });
        mRv.setAdapter(mRvAdapter);
    }

   public void onMapDestroy(){
       mMapView.onDestroy();
       mLocationClient.onDestroy();
   }

    @Override
    public void onMapLoaded() {
        LatLng latLng = new LatLng(22.5428750360,114.0595699327);
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);
            } else {
                String errText = "定位失败:" + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                DebugLog.e(errText+"---");
            }
        }
    }


    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    // infoWindow适配器
    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View infoWindow = inflater.inflate(R.layout.running_car_info_window, null);
        TextView tv_driverName = (TextView) infoWindow.findViewById(R.id.tv_info_window_driver);
        TextView tv_vehicleCode = (TextView) infoWindow.findViewById(R.id.tv_info_window_vehicleCode);
        String[] temp = marker.getTitle().split(",");
        tv_driverName.setText("司机: " + temp[0]);
        tv_vehicleCode.setText("车号: " + temp[1]);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
//        if (mCurrentMarker != null) {
//            mCurrentMarker.hideInfoWindow();
//        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        mCurrentMarker = marker;
        mCurrentMarkerTitle = marker.getTitle();
        if (mRvAdapter != null){
            String[] temp = marker.getTitle().split(",");
            mSelectedVehicleCode = temp[1];
            mRvAdapter.notifyDataSetChanged();
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_query_all:
                clearEtVehicle();
                setTabBackground(0);
                setAdapter(mOnAllList);
                isClickTab = "0";
                break;
            case R.id.tv_no_on_line:
                clearEtVehicle();
                setTabBackground(1);
                setAdapter(mOnNoLineList);
                isClickTab = "1";
                break;
            case R.id.tv_on_line:
                clearEtVehicle();
                setTabBackground(2);
                setAdapter(mOnLineList);
                isClickTab = "2";
                break;
        }
    }


}
