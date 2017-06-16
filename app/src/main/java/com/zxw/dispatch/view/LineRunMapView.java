package com.zxw.dispatch.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zxw.dispatch.recycler.VehicleCodeListAdapter;
import com.zxw.dispatch.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


public class LineRunMapView extends LinearLayout implements LocationSource, AMapLocationListener
        , AMap.OnMapClickListener, AMap.OnMapLoadedListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter {

    private Context mContext;
    private AMap mMap;
    private MapView mMapView;
    private SlidingDrawer mSlidingDrawer;
    private ImageView mImgHandle;
    private RecyclerView mRv;
    private Marker mCurrentMarker;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;
    private static List<Marker> mMarkers = new ArrayList<>();


    public LineRunMapView(Context context, Bundle savedInstanceState) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_line_run_map_new, this);
        initView();
        initEvent();
        initMap(savedInstanceState);
    }


    private void initView() {
        mSlidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        mImgHandle = (ImageView) findViewById(R.id.iv_handle);
        mRv = (RecyclerView) findViewById(R.id.rv_vehicleCode_list);
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


    private void initMap(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.mv_map2d);
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();
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


    public void initLineParams(final List<RunningCarBean> list) {
        drawMarkersAtMap(list);
        setMarkerListener();
    }


    private void drawMarkersAtMap(final List<RunningCarBean> list) {
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        final List<RunningCarBean> sendList = new ArrayList<>();
        if (mMarkers != null) {
            mMap.clear();
            mMarkers.clear();
        }
        // 转Gps坐标系
        final CoordinateConverter converter = new CoordinateConverter(mContext);
        for (int i = 0; i < list.size(); i++) {
            DebugLog.e("在跑车辆总数:" + list.size() + "---");
            DebugLog.e(String.valueOf(i) + "___:司机名称:__" + list.get(i).getDriverName() + "__车牌号:___" + list.get(i).getVehicleCode() + "---");
            final int index = i;
            final RunningCarBean runCarBean = list.get(i);
            HttpGPsRequest.getInstance().gpsByVehCode(new Subscriber<HttpGPsRequest.GpsBaseBean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (index == list.size() - 1) {
                        if (sendList != null && sendList.size() > 0) {
                            setAdapter(sendList);
                        }
                    }
                }

                @Override
                public void onNext(HttpGPsRequest.GpsBaseBean gpsBean) {
                    try {
                        if (gpsBean.success) {
                            DebugLog.e("gps:success:__" + String.valueOf(index) + "+:__纬度___" + gpsBean.gps.latitude + "__经度__" + gpsBean.gps.longitude + "---");
                            // CoordType.GPS 待转换坐标类型
                            converter.from(CoordinateConverter.CoordType.GPS);
                            // sourceLatLng待转换坐标点 LatLng类型
                            DPoint dPoint = new DPoint(gpsBean.gps.latitude,gpsBean.gps.longitude);
                            converter.coord(dPoint);
                            // 执行转换操作
                            DPoint desLatLng = converter.convert();
                            LatLng latLng = new LatLng(desLatLng.getLatitude(), desLatLng.getLongitude());

                            runCarBean.setLatitude(desLatLng.getLatitude());
                            runCarBean.setLongitude(desLatLng.getLongitude());
                            builder.include(latLng);
                            Marker marker = mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                                    .position(latLng)
                                    .title(runCarBean.getDriverName() + "," + runCarBean.getVehicleCode())
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_real_time_position))
                                    .draggable(true)
                            );

                            mMarkers.add(marker);
                            sendList.add(runCarBean);

                            if (index == list.size() - 1) {
                                if (sendList != null && sendList.size() > 0) {
                                    setAdapter(sendList);
                                }
                            }

                        } else {
                            DebugLog.e("gps:failed:" + String.valueOf(index) + runCarBean.getDriverName() + ":" + runCarBean.getVehicleCode() + "---");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, runCarBean.getVehicleCode());
        }
    }


    private void setAdapter(List<RunningCarBean> list) {
        VehicleCodeListAdapter adapter = new VehicleCodeListAdapter(mContext, list,
                new VehicleCodeListAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(RunningCarBean bean, int position) {
                        double latitude = bean.getLatitude();
                        double longitude = bean.getLongitude();
                        LatLng marker1 = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
                        mMarkers.get(position).showInfoWindow();
                        mCurrentMarker = mMarkers.get(position);

                    }
                });
        mRv.setAdapter(adapter);
        mSlidingDrawer.setVisibility(View.VISIBLE);
        mSlidingDrawer.open();
    }

    private void setMarkerListener() {
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(this);
    }

    @Override
    public void onMapLoaded() {
        LatLng marker1 = new LatLng(22.6024400000, 114.1201800000);
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(mContext);
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
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                DebugLog.e(errText);
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

    // 点击infoWindow事件
    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mCurrentMarker != null) {
            mCurrentMarker.hideInfoWindow();
        }
    }

    // 点击Marker事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        mCurrentMarker = marker;
        return false;
    }


}
