package com.zxw.dispatch.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.source.DepartSource;
import com.zxw.data.source.LineSource;
import com.zxw.data.source.SendSource;
import com.zxw.data.source.StopSource;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.recycler.GoneAdapter;
import com.zxw.dispatch.service.CarPlanService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public final static int TYPE_SALE_AUTO = 1, TYPE_SALE_MANUAL = 2;
    private List<Line> mLineBeen;
    private boolean isAuto = false;
    private Intent receiverIntent = new Intent("com.zxw.dispatch.service.RECEIVER");
    private List<Integer> sendNums = new ArrayList<>();

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
                mLineBeen = lineBeen;
                mvpView.loadLine(lineBeen);
                timeToSend();
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
                // 自动售票,隐藏乘务员字段
                if (mLineParams.getSaleType() == TYPE_SALE_AUTO){
                    mvpView.hideStewardName();
                }else if(mLineParams.getSaleType() == TYPE_SALE_MANUAL){
                    mvpView.showStewardName();
                }
                mCurrentLine = line;
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
                mvpView.loadGoneCarList(new GoneAdapter(sendHistories, mContext, mLineParams));
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
        mDepartSource.updateVehicle(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                mvpView.hideLoading();
            }

            @Override
            public void onNext(Object o) {
                refreshList();
            }
        }, userId(), keyCode(), opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
    }

    public void sendVehicle(int opId) {
        mDepartSource.sendCar(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                mvpView.hideLoading();
            }

            @Override
            public void onNext(Object baseBean) {
               mvpView.disPlay("发车成功");
                timeToSend();
                refreshList();
            }
        }, userId(), keyCode(), opId);
    }

    public void sortVehicle(int opId, int replaceId) {
        mDepartSource.sortVehicle(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                mvpView.hideLoading();
            }

            @Override
            public void onNext(Object o) {
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
        if (serviceIntent == null)
        serviceIntent = new Intent(mContext,CarPlanService.class);
        serviceIntent.putExtra("lineId", lineId);
        mContext.startService(serviceIntent);
    }

    /**
     * 点击手动发车
     */
    public void selectManual() {
//        Intent intent = new Intent("com.zxw.dispatch.service.RECEIVER");
        receiverIntent.putExtra("lineId", lineId);
        mContext.sendBroadcast(receiverIntent);
    }

    public void manualAddStopCar(String carId, String driverId, String stewardId) {
        mDepartSource.vehicleStopCtrl(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(Object baseBean) {
                refreshList();
            }
        }, userId(), keyCode(), carId, driverId, mLineParams.getSaleType(),stewardId, String.valueOf(mCurrentLine.lineId));
    }

    public void vehicleToSchedule(StopHistory stopCar) {
        mDepartSource.vehicleToSchedule(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                refreshList();
            }
        }, userId(), keyCode(), String.valueOf(stopCar.id), mLineParams.getTimeType());
    }

    public void alertPeople(int id, int peopleId, int type) {
        mDepartSource.changePersonInfo(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(Object baseBean) {
                refreshList();
            }
        }, userId(), keyCode(), id, peopleId, type);
    }

    public void alertVehTime(int id, String vehTime) {
        HttpMethods.getInstance().alertVehTime(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        }, userId(), keyCode(), id, vehTime);
    }

    public LineParams getLineParams() {
        return mLineParams;
    }

    public void getMissionList(final int objId){
        try {
            HttpMethods.getInstance().missionList(new Subscriber<List<MissionType>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<MissionType> missionTypes) {
                    mvpView.showMissionTypeDialog(missionTypes, objId);


                }
            }, userId(), keyCode());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void changeMissionType(int objId, int type, String taskId){
        mvpView.showLoading();
        HttpMethods.getInstance().missionType(new Subscriber() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                mvpView.disPlay("修改成功");
                refreshList();

            }
        }, userId(), keyCode(), objId, type, taskId);
    }

    private int checkLine;
    public void timeToSend(){
        checkLine = 0;
        if (sendNums != null) sendNums.clear();
        for (Line line: mLineBeen) {
            sendNums.add(0);
        }
        checkIsTimeToSend(mLineBeen.get(0));
    }

    private void checkIsTimeToSend(Line line){
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

                for (DepartCar departCar: waitVehicles){
                    SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    Log.e("time1", formatter.format(curDate) + "");
                    if (Integer.valueOf(departCar.getVehTime()) - Integer.valueOf(formatter.format(curDate) + "") < 30){
                        sendNums.set(checkLine, sendNums.get(checkLine)+ 1);
                        Log.e("time2", departCar.getVehTime() + "");
                    }
                }
                checkLine ++;
                if (checkLine < mLineBeen.size()){
                    checkIsTimeToSend(mLineBeen.get(checkLine));
                }else {
                    mvpView.refreshTimeToSendCarNum(sendNums);
                }
            }
        }, userId(), keyCode(), line.lineId);
    }
}
