package com.zxw.dispatch.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.NonMissionType;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.source.DepartSource;
import com.zxw.data.source.LineSource;
import com.zxw.data.source.StopSource;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.recycler.GoneAdapter;
import com.zxw.dispatch.recycler.NonMissionTypeAdapter;
import com.zxw.dispatch.service.CarPlanService;
import com.zxw.dispatch.utils.Base64;
import com.zxw.dispatch.utils.DESPlus;

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
    private StopSource mStopSource = new StopSource();
    private Line mCurrentLine;
    private DragListAdapter mDragListAdapter;
    private Context mContext;
    private Intent serviceIntent;
    private int lineId, stationId;
    private String lineName;
    private LineParams mLineParams;
    public final static int TYPE_SALE_AUTO = 1, TYPE_SALE_MANUAL = 2;
    private List<Line> mLineBeen;
    private boolean isAuto = false;
    private Intent receiverIntent = new Intent("com.zxw.dispatch.service.RECEIVER");
    private List<Integer> sendNums = new ArrayList<>();
    private SimpleDateFormat formatter = new SimpleDateFormat("HHmm");

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
                lineName = mCurrentLine.lineCode;
                refreshList();
            }
        }, userId(), keyCode(),line.lineId);
    }


    public void onAddRecordingCarTaskNameList(final int lineId){
        HttpMethods.getInstance().missionList(new Subscriber<List<MissionType>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(List<MissionType> missionTypes) {
                mvpView.onGetAddRecordingTaskNameList(missionTypes);
            }
        },userId(),keyCode(),lineId +"");

    }

    public void addRecordingCarTask(String vehicleId, String driverId, String type, String taskId, String runNum,
                                    String runEmpMileage, String beginTime, String endTime){
     HttpMethods.getInstance().lineMakeup(new Subscriber() {
         @Override
         public void onCompleted() {

         }

         @Override
         public void onError(Throwable e) {
              mvpView.disPlay(e.getMessage());
         }

         @Override
         public void onNext(Object o) {
             mvpView.disPlay("补录车辆任务成功");
         }
     },userId(),keyCode(),lineId+"",vehicleId,driverId,type,taskId,runNum,runEmpMileage,beginTime,endTime);


    }

    private void loadGoneCarList() {
        mDepartSource.goneListByLine(new Subscriber<List<SendHistory>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(List<SendHistory> sendHistories) {
                mvpView.loadGoneCarList(new GoneAdapter(sendHistories, mContext, mLineParams, MainPresenter.this));
            }
        }, userId(), keyCode(), lineId);

    }

    private void loadSendCarList(){
        mDepartSource.departListByLine(new Subscriber<List<DepartCar>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(List<DepartCar> waitVehicles) {
                mDragListAdapter = new DragListAdapter(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
                mvpView.loadSendCarList(mDragListAdapter);
                timeToSend();
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

    public void stopCarMission(StopHistory stopCar, int type, String taskId, String taskType,
                               String beginTime, String endTime,  String runNum, String runEmpMileage) {
        mDepartSource.stopToSchedule(new Subscriber() {
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
        }, userId(), keyCode(), String.valueOf(stopCar.id), type, taskId, taskType, beginTime, endTime, runNum, runEmpMileage, mLineParams.getTimeType());
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

    /**
     * 修改计划发车时间
     * @param id
     * @param vehTime
     */
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
                loadSendCarList();

            }
        }, userId(), keyCode(), id, vehTime);
    }

    public LineParams getLineParams() {
        return mLineParams;
    }


    public void getMissionList(final int objId, final int type, final String taskId){
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
                    mvpView.showMissionTypeDialog(missionTypes, objId, type, taskId, lineName);


                }
            }, userId(), keyCode(), lineId + "");
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
                refreshList();

            }
        }, userId(), keyCode(), objId, type, taskId);
    }

    /**
     * 检测计划发车时间在30分钟内的车辆数
     * ------------------begin--------------
     */
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
        mDepartSource.departListByLine(new Subscriber<List<DepartCar>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
            }

            @Override
            public void onNext(List<DepartCar> waitVehicles) {
                try{

                    for (DepartCar departCar: waitVehicles){

                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        Log.e("time1", formatter.format(curDate) + "");
                        int space = (Integer.valueOf(departCar.getVehTime().substring(0, 2))
                                -Integer.valueOf((formatter.format(curDate) + "").substring(0, 2))) * 60
                                +Integer.valueOf(departCar.getVehTime().substring(2, 4))
                                -Integer.valueOf((formatter.format(curDate) + "").substring(2, 4));
                        if (space <= 30){
                            Log.e("sendMum","===========");
                            sendNums.set(checkLine, sendNums.get(checkLine)+ 1);
                            Log.e("time2", departCar.getVehTime() + "");
                        }
                    }
                    checkLine ++;
                    if (checkLine < mLineBeen.size()){
                        Log.e("checkLine","++++++++++++");
                        checkIsTimeToSend(mLineBeen.get(checkLine));
                    }else {
                        mvpView.refreshTimeToSendCarNum(sendNums);
                    }
                }catch (Exception e){
                    Log.e("timeToSend", e.getMessage());
                }

            }
        }, userId(), keyCode(), line.lineId);
    }
    /**
     * 检测计划发车时间在30分钟内的车辆数
     * ------------------end----------------
     */

    /**
     * 撤回待发车辆
     * @param objId
     */
    public void callBackScheduleCar(int objId){
        mvpView.showLoading();
        HttpMethods.getInstance().callBackScheduleCar(new Subscriber() {
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
                refreshList();

            }
        }, userId(),keyCode(), objId);
    }

    /**
     * 撤回已发车辆
     * @param objId
     */
    public void callBackGoneCar(int objId){
        mvpView.showLoading();
        HttpMethods.getInstance().callBackGoneCar(new Subscriber() {
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
                refreshList();

            }
        }, userId(),keyCode(), objId);
    }

    /**
     * 修改已发车辆备注信息
     * @param objId
     * @param status
     * @param remark
     */
    public void goneCarRemarks(int objId, int status, String remark){
        mvpView.showLoading();
        try {
            String remarkStr = new DESPlus().encrypt(Base64.encode(remark.getBytes("utf-8")));
            HttpMethods.getInstance().goneCarRemarks(new Subscriber() {
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
                    loadGoneCarList();

                }
            }, userId(), keyCode(), objId, status, remarkStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSpaceTime(int objId, String  spaceTime){
        mvpView.showLoading();
        HttpMethods.getInstance().updateSpaceTime(new Subscriber() {
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
                loadSendCarList();
            }
        }, userId(), keyCode(), objId, spaceTime);
    }

    public void nonMissionType(String vehicleId){
        mvpView.showLoading();
        HttpMethods.getInstance().nonMissionList(new Subscriber<List<NonMissionType>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());

            }

            @Override
            public void onNext(List<NonMissionType> nonMissionTypes) {
                if (nonMissionTypes != null && !nonMissionTypes.isEmpty()){
                    NonMissionTypeAdapter nonMissionTypeAdapter = new NonMissionTypeAdapter(nonMissionTypes, mContext);
                    mvpView.nonMissionTypeDialog(nonMissionTypeAdapter);
                }else {
                    mvpView.disPlay("无非运营任务");
                }

            }
        }, userId(), keyCode(), vehicleId);
    }

    public void lineSupport(int objId, int supportLineId){
        mvpView.showLoading();
        HttpMethods.getInstance().lineSupport(new Subscriber() {
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
                mvpView.disPlay("操作成功");
                loadSendCarList();
                loadGoneCarList();

            }
        }, userId(), keyCode(), objId, supportLineId);
    }

    public void confirmInform(String vehicleId, String content, String typeId){
        mvpView.showLoading();
        try{
            String remarkStr = new DESPlus().encrypt(Base64.encode(content.getBytes("utf-8")));
            HttpMethods.getInstance().confrimInform(new Subscriber() {
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
                    mvpView.disPlay("操作成功");
                    refreshList();
                }
            }, userId(), keyCode(), vehicleId, remarkStr, typeId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getLineId(){
        return lineId;
    }
}
