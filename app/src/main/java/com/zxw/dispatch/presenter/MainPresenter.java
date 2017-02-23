package com.zxw.dispatch.presenter;

import android.content.Context;
import android.content.Intent;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.source.DepartSource;
import com.zxw.data.source.LineSource;
import com.zxw.data.source.SendSource;
import com.zxw.data.source.StopSource;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.service.CarPlanService;
import com.zxw.dispatch.view.DragListAdapter;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/20 17:26
 * email：cangjie2016@gmail.com
 */
public class MainPresenter extends BasePresenter<MainView> {

    private LineSource mLineSource = new LineSource();
    private DepartSource mDepartSource = new DepartSource();
    private SendSource mSendSource = new SendSource();
    private StopSource mStopSource = new StopSource();
    private Line mCurrentLine;
    private DragListAdapter mDragListAdapter;
    private Context mContext;
    private Intent serviceIntent;
    private int lineId, stationId;
    private LineParams mLineParams;

    public MainPresenter(Context context, MainView mvpView) {
        super(mvpView);
        this.mContext = context;
    }

    public void loadLineList(int spotId){
        mvpView.showLoading();
        mLineSource.loadLine(new Subscriber<List<Line>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.hideLoading();
                mvpView.disPlay(e.getMessage());
                mvpView.reLogin();
            }

            @Override
            public void onNext(List<Line> lineBeen) {
                mvpView.loadLine(lineBeen);
            }
        }, userId(), keyCode(), spotId, 1, 20);
    }

    public void onSelectLine(final Line line) {
        HttpMethods.getInstance().lineParams(new Subscriber<LineParams>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LineParams lineParams) {
                mLineParams = lineParams;
                mCurrentLine = line;
//        stationId = mCurrentLine.lineStationList.get(0).stationId;
                lineId = mCurrentLine.lineId;
                refreshList();
            }
        }, userId(), keyCode(),line.lineId);
    }

    private void loadGoneCarList() {
        mSendSource.loadSend(new Subscriber<List<SendHistory>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.hideLoading();
                mvpView.disPlay(e.getMessage());
                mvpView.finish();
            }

            @Override
            public void onNext(List<SendHistory> sendHistories) {
                mvpView.loadGoneCarList(sendHistories);
            }
        }, userId(), keyCode(), lineId);

    }

    private void loadSendCarList(){
        mDepartSource.departList(new Subscriber<List<DepartCar>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.hideLoading();
                mvpView.disPlay(e.getMessage());
                mvpView.finish();
            }

            @Override
            public void onNext(List<DepartCar> waitVehicles) {
                mDragListAdapter = new DragListAdapter(mContext, MainPresenter.this, waitVehicles, mLineParams);
                mvpView.loadSendCarList(mDragListAdapter);
            }
        }, userId(), keyCode(), lineId);
    }

    public void updateVehicle(int opId, int vehId, int sjId, String scId, String projectTime, int spaceMin, String inTime2) {
        mDepartSource.updateVehicle(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                mvpView.hideLoading();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                mvpView.disPlay(baseBean.returnInfo);
                refreshList();
            }
        }, userId(), keyCode(), opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
    }

    public void sendVehicle(int opId) {
        mDepartSource.sendCar(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                mvpView.hideLoading();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                mvpView.disPlay(baseBean.returnInfo);
                refreshList();
            }
        }, userId(), keyCode(), opId);
    }

    public void sortVehicle(int opId, int replaceId) {
        mDepartSource.sortVehicle(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                mvpView.hideLoading();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                mvpView.disPlay(baseBean.returnInfo);
                refreshList();
            }
        }, userId(), keyCode(), opId, replaceId);
    }

    public void refreshList(){
        loadSendCarList();
        loadGoneCarList();
        loadStopCarList();

    }

    private void loadStopCarList() {
        mStopSource.loadStop(new Subscriber<List<StopHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<StopHistory> stopHistories) {
                stopHistories.add(new StopHistory());
                mvpView.loadStopCarList(stopHistories);
            }
        }, userId(), keyCode(), lineId);
    }

    /**
     * 点击自动发车
     */
    public void selectAuto() {
        serviceIntent = new Intent(mContext,CarPlanService.class);
        serviceIntent.putExtra("stationId", stationId);
        serviceIntent.putExtra("lineId", lineId);
        mContext.startService(serviceIntent);
    }

    /**
     * 点击手动发车
     */
    public void selectManual() {
        Intent intent = new Intent("com.zxw.dispatch.service.RECEIVER");
        intent.putExtra("stationId", stationId);
        intent.putExtra("lineId", lineId);
        mContext.sendBroadcast(intent);
    }

    public void manualAddStopCar(String carId, String driverId, String stewardId) {
        mDepartSource.vehicleStopCtrl(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseBean baseBean) {
                refreshList();
            }
        }, userId(), keyCode(), carId, driverId, mLineParams.getSaleType(),stewardId, String.valueOf(mCurrentLine.lineId));
    }

    public void vehicleToSchedule(StopHistory stopCar) {
        mDepartSource.vehicleToSchedule(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseBean baseBean) {
                refreshList();
            }
        }, userId(), keyCode(), String.valueOf(mCurrentLine.lineId), String.valueOf(stopCar.id), mLineParams.getTimeType());
    }
}
