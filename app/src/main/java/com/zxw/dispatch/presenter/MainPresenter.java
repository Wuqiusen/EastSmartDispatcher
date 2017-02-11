package com.zxw.dispatch.presenter;

import android.content.Context;
import android.content.Intent;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.bean.WaitVehicle;
import com.zxw.data.source.DepartSource;
import com.zxw.data.source.LineSource;
import com.zxw.data.source.SendSource;
import com.zxw.data.source.StopSource;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.service.CarPlanService;
import com.zxw.dispatch.view.DragListAdapter;

import java.util.List;

import rx.Subscriber;

import static com.zxw.dispatch.Constants.MANUAL_TYPE;

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

    public MainPresenter(Context context, MainView mvpView) {
        super(mvpView);
        this.mContext = context;
    }

    public void loadLineList(){
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
        }, code(), keyCode(), 1, 20);
    }

    public void onSelectLine(Line line) {
        this.mCurrentLine = line;
        stationId = mCurrentLine.lineStationList.get(0).stationId;
        lineId = mCurrentLine.id;
        refreshList();
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
        }, code(), lineId, stationId, keyCode(), 1, 20);
    }

    private void loadSendCarList(){
        mDepartSource.loadWaitVehicle(new Subscriber<List<WaitVehicle>>() {
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
            public void onNext(List<WaitVehicle> waitVehicles) {
//                mDatas = waitVehicles;
                mDragListAdapter = new DragListAdapter(mContext, MainPresenter.this, waitVehicles);
                mvpView.loadSendCarList(mDragListAdapter);
            }
        }, code(),  lineId, stationId, keyCode(), 1, 20);
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
        }, code(), keyCode(), opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
    }

    public void sendVehicle(int opId) {
        mDepartSource.sendVehicle(new Subscriber<BaseBean>() {
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
        }, code(), keyCode(), opId, MANUAL_TYPE);
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
        }, code(), keyCode(), opId, replaceId);
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
                stopHistories.add(0, new StopHistory());
                stopHistories.add(new StopHistory());
                stopHistories.add(new StopHistory());
                stopHistories.add(new StopHistory());
                mvpView.loadStopCarList(stopHistories);
            }
        }, code(), lineId, stationId, keyCode(), 1, 20);
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

}
