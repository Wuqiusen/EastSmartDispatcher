package com.zxw.dispatch.presenter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.text.TextUtils;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.RunningCarBean;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopCarCodeBean;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.bean.VehicleNumberBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.source.DepartSource;
import com.zxw.data.source.LineSource;
import com.zxw.data.source.StopSource;
import com.zxw.data.utils.LogUtil;
import com.zxw.dispatch.Constants;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.adapter.DragListAdapterForNotOperatorEmpty;
import com.zxw.dispatch.adapter.DragListAdapterForOperatorEmpty;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.recycler.GoneAdapterForNotOperatorEmpty;
import com.zxw.dispatch.recycler.GoneAdapterForOperatorEmpty;
import com.zxw.dispatch.utils.Base64;
import com.zxw.dispatch.utils.DESPlus;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.ToastHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;
import rx.Subscription;

/**
 * author：CangJie on 2016/9/20 17:26
 * email：cangjie2016@gmail.com
 */
public class MainPresenter extends BasePresenter<MainView> {
    private LineSource mLineSource = new LineSource();
    private DepartSource mDepartSource = new DepartSource();
    private StopSource mStopSource = new StopSource();
    private Line mCurrentLine;
    private Context mContext;
    private int lineId, stationId;
    private String lineName;
    private LineParams mLineParams;
    public final static int TYPE_SALE_AUTO = 1, TYPE_SALE_MANUAL = 2;
    private List<Line> mLineBeen;
    private boolean isAuto = false;
    private List<Integer> sendNums = new ArrayList<>();
    private SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
    private Timer timer = null;
    private TimerTask timerTask;
    private int spotId;
    private List<DepartCar> mWaitVehicles = new ArrayList<>();
    private List<DepartCar> operatorVehicles = new ArrayList<>();
    private List<DepartCar> noOperatorVehicles = new ArrayList<>();
    private List<SendHistory> mSendHistories = new ArrayList<>();
    private List<SendHistory> operatorHistories = new ArrayList<>();
    private List<SendHistory> noOperatorHistories = new ArrayList<>();
    private List<StopHistory> mStopHistories = new ArrayList<>();
    private List<StopHistory> endStopHistories = new ArrayList<>();
    private List<VehicleNumberBean> mVehicleNumberBeen = new ArrayList<>();
    private List<RunningCarBean> mRunningCarBean = new ArrayList<>();
    private Map<Integer, Integer> soundMap = new HashMap<>();


    public MainPresenter(Context context, MainView mvpView) {
        super(mvpView);
        this.mContext = context;
    }

    public void loadLineList(int spotId) {
        this.spotId = spotId;
        mvpView.showLoading();
        mLineSource.loadLine(new Subscriber<List<Line>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("loadLine " + e.getMessage());
                mvpView.hideLoading();
                mvpView.disPlay(e.getMessage());
//                mvpView.reLogin();
            }

            @Override
            public void onNext(List<Line> lineBeen) {
                mLineBeen = lineBeen;
                mvpView.loadLine(lineBeen);
            }
        }, userId(), keyCode(), spotId, 1, 20);
    }

    public void onSelectLine(final Line line) {
//        mvpView.showLoading();
        HttpMethods.getInstance().lineParams(new Subscriber<LineParams>() {
            @Override
            public void onCompleted() {
//                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("lineParams " + e.getMessage());
            }

            @Override
            public void onNext(LineParams lineParams) {
                mLineParams = lineParams;
                // 自动售票,隐藏乘务员字段
                if (mLineParams.getSaleType() == TYPE_SALE_AUTO) {
                    mvpView.hideStewardName();
                } else if (mLineParams.getSaleType() == TYPE_SALE_MANUAL) {
                    mvpView.showStewardName();
                }
                mvpView.setSaleType(lineParams);
                mCurrentLine = line;
                lineId = mCurrentLine.lineId;
                lineName = mCurrentLine.lineCode;
                mvpView.onSelectLine();
            }
        }, userId(), keyCode(), line.lineId);
    }


    public void onAddRecordingCarTaskNameList(final int lineId) {
        HttpMethods.getInstance().missionList(new Subscriber<List<MissionType>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("missionList " + e.getMessage());
            }

            @Override
            public void onNext(List<MissionType> missionTypes) {
                mvpView.onGetAddRecordingTaskNameList(missionTypes);
            }
        }, userId(), keyCode(), lineId + "");

    }


    /*旧的:*/
    public void addRecordingCarTask(String vehicleId, String driverId, String stewardId, String type, String taskId, String taskType, String runNum,
                                    String runEmpMileage, String beginTime, String endTime, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        HttpMethods.getInstance().lineMakeup(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("lineMakeup " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                mvpView.disPlay("补录车辆任务成功");
                refreshList();
            }
        }, userId(), keyCode(), lineId + "", vehicleId, driverId, stewardId, type, taskId, taskType, runNum, runEmpMileage, beginTime, endTime);

    }


    private void loadGoneCarList() {
        loadGoneCarByNormal(1);
        loadGoneCarByOperatorEmpty();
        loadGoneCarByNotOperatorEmpty();
    }

    public void loadGoneCarByNormal(int pageNo) {
        mDepartSource.goneListByLine(new Subscriber<BaseBean<List<SendHistory>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("goneListByLine " + e.getMessage());
            }

            @Override
            public void onNext(BaseBean<List<SendHistory>> baseBean) {
                if (mSendHistories != null && mSendHistories.size() == baseBean.returnData.size()) {
                    for (int i = 0; i < baseBean.returnData.size(); i++) {
                        if (mSendHistories.get(i).id != baseBean.returnData.get(i).id) {
                            mvpView.loadGoneCarByNormal(baseBean.returnData, baseBean.returnSize);
                            mSendHistories.clear();
                            mSendHistories.addAll(baseBean.returnData);
                            mvpView.hideLoading();
                            return;
                        }
                    }
                } else {
                    mvpView.loadGoneCarByNormal(baseBean.returnData, baseBean.returnSize);
                    mSendHistories.clear();
                    mSendHistories.addAll(baseBean.returnData);
                    mvpView.hideLoading();
                }
            }
        }, userId(), keyCode(), lineId, pageNo, Constants.PAGE_SIZE);
    }

    private void loadGoneCarByOperatorEmpty() {
        mDepartSource.goneListByOperation(new Subscriber<List<SendHistory>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("goneListByOperation " + e.getMessage());
            }

            @Override
            public void onNext(List<SendHistory> sendHistories) {
//                if (operatorHistories == null || !checkList(operatorHistories, sendHistories)){
//                    operatorHistories = sendHistories;
//                    mvpView.loadGoneCarByOperatorEmpty(new GoneAdapterForOperatorEmpty(sendHistories, mContext, mLineParams, MainPresenter.this));
//                }

                if (operatorHistories != null && operatorHistories.size() == sendHistories.size()) {
                    for (int i = 0; i < sendHistories.size(); i++) {
                        if (operatorHistories.get(i).id != sendHistories.get(i).id) {
                            mvpView.loadGoneCarByOperatorEmpty(new GoneAdapterForOperatorEmpty(sendHistories, mContext, mLineParams, MainPresenter.this));
                            operatorHistories.clear();
                            operatorHistories.addAll(sendHistories);
                            mvpView.hideLoading();
                            return;
                        }
                    }
                } else {
                    mvpView.loadGoneCarByOperatorEmpty(new GoneAdapterForOperatorEmpty(sendHistories, mContext, mLineParams, MainPresenter.this));
                    operatorHistories.clear();
                    operatorHistories.addAll(sendHistories);
                    mvpView.hideLoading();
                }

            }
        }, userId(), keyCode(), lineId);
    }

    private void loadGoneCarByNotOperatorEmpty() {
        mDepartSource.goneListByOther(new Subscriber<List<SendHistory>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("goneListByOther " + e.getMessage());
            }

            @Override
            public void onNext(List<SendHistory> sendHistories) {
//                if (noOperatorHistories == null || !checkList(noOperatorHistories, sendHistories)){
//                    noOperatorHistories = sendHistories;
//                    mvpView.loadGoneCarByNotOperatorEmpty(new GoneAdapterForNotOperatorEmpty(sendHistories, mContext, mLineParams, MainPresenter.this));
//                }

                if (noOperatorHistories != null && noOperatorHistories.size() == sendHistories.size()) {
                    for (int i = 0; i < sendHistories.size(); i++) {
                        if (noOperatorHistories.get(i).id != sendHistories.get(i).id) {
                            mvpView.loadGoneCarByNotOperatorEmpty(new GoneAdapterForNotOperatorEmpty(sendHistories, mContext, mLineParams, MainPresenter.this));
                            noOperatorHistories.clear();
                            noOperatorHistories.addAll(sendHistories);
                            mvpView.hideLoading();
                            return;
                        }
                    }
                } else {
                    mvpView.loadGoneCarByNotOperatorEmpty(new GoneAdapterForNotOperatorEmpty(sendHistories, mContext, mLineParams, MainPresenter.this));
                    noOperatorHistories.clear();
                    noOperatorHistories.addAll(sendHistories);
                    mvpView.hideLoading();
                }

            }
        }, userId(), keyCode(), lineId);
    }

    private void loadSendCarList() {
        checkVehicleCount(spotId);
        loadSendCarForNormal();
        loadSendCarForOperatorEmpty();
        loadSendCarForNotOperatorEmpty();
    }

    private void loadSendCarForNormal() {
        mDepartSource.departListByLine(new Subscriber<List<DepartCar>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("departListByLine " + e.getMessage());
            }

            @Override
            public void onNext(List<DepartCar> waitVehicles) {
//                if (mWaitVehicles == null || !checkList(mWaitVehicles, waitVehicles)){
//                    mWaitVehicles = waitVehicles;
//                    DragListAdapter mDragListAdapter = new DragListAdapter(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
//                    mvpView.loadSendCarList(mDragListAdapter);
//                }

                if (mWaitVehicles != null && mWaitVehicles.size() == waitVehicles.size()) {
                    for (int i = 0; i < waitVehicles.size(); i++) {
                        if (mWaitVehicles.get(i).getId() != waitVehicles.get(i).getId() || mWaitVehicles.get(i).getIsNotice() != waitVehicles.get(i).getIsNotice()) {
                            DragListAdapter mDragListAdapter = new DragListAdapter(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
                            mvpView.loadSendCarList(mDragListAdapter);
                            mWaitVehicles.clear();
                            mWaitVehicles.addAll(waitVehicles);
                            mvpView.hideLoading();
                            return;
                        }
                    }
                } else {
                    DragListAdapter mDragListAdapter = new DragListAdapter(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
                    mvpView.loadSendCarList(mDragListAdapter);
                    mWaitVehicles.clear();
                    mWaitVehicles.addAll(waitVehicles);
                    mvpView.hideLoading();
                }

            }
        }, userId(), keyCode(), lineId);
    }

    private void loadSendCarForOperatorEmpty() {
        mDepartSource.departListByOperation(new Subscriber<List<DepartCar>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("departListByOperation " + e.getMessage());
            }

            @Override
            public void onNext(List<DepartCar> waitVehicles) {
//                if (operatorVehicles == null || !checkList(operatorVehicles, waitVehicles)){
//                    operatorVehicles = waitVehicles;
//                    DragListAdapterForOperatorEmpty mDragListAdapter = new DragListAdapterForOperatorEmpty(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
//                    mvpView.loadSendCarForOperatorEmpty(mDragListAdapter);
//                }

                if (operatorVehicles != null && operatorVehicles.size() == waitVehicles.size()) {
                    for (int i = 0; i < waitVehicles.size(); i++) {
                        if (operatorVehicles.get(i).getId() != waitVehicles.get(i).getId()) {
                            DragListAdapterForOperatorEmpty mDragListAdapter = new DragListAdapterForOperatorEmpty(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
                            mvpView.loadSendCarForOperatorEmpty(mDragListAdapter);
                            operatorVehicles.clear();
                            operatorVehicles.addAll(waitVehicles);
                            mvpView.hideLoading();
                            return;
                        }
                    }
                } else {
                    DragListAdapterForOperatorEmpty mDragListAdapter = new DragListAdapterForOperatorEmpty(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
                    mvpView.loadSendCarForOperatorEmpty(mDragListAdapter);
                    operatorVehicles.clear();
                    operatorVehicles.addAll(waitVehicles);
                    mvpView.hideLoading();
                }

            }
        }, userId(), keyCode(), lineId);
    }

    private void loadSendCarForNotOperatorEmpty() {
        mDepartSource.departListByOther(new Subscriber<List<DepartCar>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("departListByOther " + e.getMessage());
            }

            @Override
            public void onNext(List<DepartCar> waitVehicles) {
//                if (noOperatorVehicles == null || !checkList(noOperatorVehicles, waitVehicles)){
//                    noOperatorVehicles = waitVehicles;
//                    DragListAdapterForNotOperatorEmpty mDragListAdapter = new DragListAdapterForNotOperatorEmpty(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
//                    mvpView.loadSendCarForNotOperatorEmpty(mDragListAdapter);
//                }

                if (noOperatorVehicles != null && noOperatorVehicles.size() == waitVehicles.size()) {
                    for (int i = 0; i < waitVehicles.size(); i++) {
                        if (noOperatorVehicles.get(i).getId() != waitVehicles.get(i).getId()) {
                            DragListAdapterForNotOperatorEmpty mDragListAdapter = new DragListAdapterForNotOperatorEmpty(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
                            mvpView.loadSendCarForNotOperatorEmpty(mDragListAdapter);
                            noOperatorVehicles.clear();
                            noOperatorVehicles.addAll(waitVehicles);
                            mvpView.hideLoading();
                            return;
                        }
                    }
                } else {
                    DragListAdapterForNotOperatorEmpty mDragListAdapter = new DragListAdapterForNotOperatorEmpty(mContext, MainPresenter.this, waitVehicles, mLineParams, lineId);
                    mvpView.loadSendCarForNotOperatorEmpty(mDragListAdapter);
                    noOperatorVehicles.clear();
                    noOperatorVehicles.addAll(waitVehicles);
                    mvpView.hideLoading();
                }
            }
        }, userId(), keyCode(), lineId);
    }

    public void sendVehicle(int opId, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mDepartSource.sendCar(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                mvpView.hideLoading();
                LogUtil.loadRemoteError("sendCar " + e.getMessage());
            }

            @Override
            public void onNext(Object baseBean) {
                mvpView.disPlay("发车成功");
                refreshList();
            }
        }, userId(), keyCode(), opId);
    }

    public void sortVehicle(int opId, int replaceId, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mDepartSource.sortVehicle(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("sortVehicle " + e.getMessage());
                mvpView.disPlay(e.getMessage());
                mvpView.hideLoading();
            }

            @Override
            public void onNext(Object o) {
                refreshPlanData();
                refreshList();
            }
        }, userId(), keyCode(), opId, replaceId);
    }

    public void refreshList() {
        loadSendCarList();
        loadGoneCarList();
        loadStopCarList();
    }

    private void loadStopCarList() {
        loadStopCarByStay();
        loadStopCarByEnd();
    }

    private void loadStopCarByEnd() {
        mStopSource.loadStopByEnd(new Subscriber<List<StopHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("loadStopByEnd " + e.getMessage());
            }

            @Override
            public void onNext(List<StopHistory> stopHistories) {
//                if (endStopHistories == null || !checkList(endStopHistories, stopHistories)){
//                    endStopHistories = stopHistories;
//                    mvpView.loadStopEndCarList(stopHistories);
//                }

                if (endStopHistories != null && endStopHistories.size() == stopHistories.size()) {
                    for (int i = 0; i < stopHistories.size(); i++) {
                        if (endStopHistories.get(i).id != stopHistories.get(i).id) {
                            mvpView.loadStopEndCarList(stopHistories);
                            endStopHistories.clear();
                            endStopHistories.addAll(stopHistories);
                            mvpView.hideLoading();
                            return;
                        }
                    }
                } else {
                    mvpView.loadStopEndCarList(stopHistories);
                    endStopHistories.clear();
                    endStopHistories.addAll(stopHistories);
                    mvpView.hideLoading();
                }

            }
        }, userId(), keyCode(), lineId);
    }

    private void loadStopCarByStay() {
        for (final Line line: mLineBeen){
            mStopSource.loadStopByStay(new Subscriber<List<StopHistory>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    LogUtil.loadRemoteError("loadStopByStay " + e.getMessage());
                }

                @Override
                public void onNext(List<StopHistory> stopHistories) {
                    DebugLog.e("加载已发历史0000-----------");
                    if (soundMap.containsKey(line.lineId)){
                        if (soundMap.get(line.lineId) < stopHistories.size()){
                            warningSound();
                        }
                    }
                    soundMap.put(line.lineId, stopHistories.size());
                    stopHistories.add(new StopHistory());
                    if (line.lineId == lineId){
                        mvpView.loadStopStayCarList(stopHistories);
                        mvpView.hideLoading();
                    }
//
//                    if (mStopHistories != null && mStopHistories.size() == stopHistories.size()) {
//                        for (int i = 0; i < stopHistories.size(); i++) {
//                            if (mStopHistories.get(i).id != stopHistories.get(i).id) {
//                                mvpView.loadStopStayCarList(stopHistories);
//                                mStopHistories.clear();
//                                mStopHistories.addAll(stopHistories);
//                                mvpView.hideLoading();
//                                warningSound();
//                                return;
//                            }
//                        }
//                    } else {
//                        if (mStopHistories.size() > 1 && mStopHistories.size() < stopHistories.size())
//                            warningSound();
//                        mvpView.loadStopStayCarList(stopHistories);
//                        mStopHistories.clear();
//                        mStopHistories.addAll(stopHistories);
//                        mvpView.hideLoading();
//                    }

                }
            }, userId(), keyCode(), line.lineId);
        }
    }

    private void warningSound(){
        MediaPlayer mMediaPlayer = MediaPlayer.create(mContext, RingtoneManager.getActualDefaultRingtoneUri(mContext,
                RingtoneManager.TYPE_NOTIFICATION));
        mMediaPlayer.setLooping(false);//设置循环
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }


    public void manualAddStopCar(String carId, String driverId, String stewardId, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mDepartSource.vehicleStopCtrl(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("vehicleStopCtrl " + e.getMessage());
            }

            @Override
            public void onNext(Object baseBean) {
                refreshList();
            }
        }, userId(), keyCode(), carId, driverId, mLineParams.getSaleType(), stewardId, String.valueOf(mCurrentLine.lineId));
    }

    public void stopCarMission(StopHistory stopCar, int type, String taskId, String taskType,
                               String beginTime, String endTime, String runNum, String runEmpMileage, String remarks, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        try {
            if (!TextUtils.isEmpty(remarks))
                remarks = new DESPlus().encrypt(Base64.encode(remarks.getBytes("utf-8")));
            mDepartSource.stopToSchedule(new Subscriber() {
                @Override
                public void onCompleted() {
                    mLoadDataStatus.OnLoadDataFinish();

                }

                @Override
                public void onError(Throwable e) {
                    mvpView.disPlay(e.getMessage());
                    LogUtil.loadRemoteError("stopToSchedule " + e.getMessage());
                }

                @Override
                public void onNext(Object o) {
                    refreshList();
                }
            }, userId(), keyCode(), String.valueOf(stopCar.id), type, taskId, taskType, beginTime, endTime, runNum, runEmpMileage, mLineParams.getTimeType(), remarks);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void alertPeople(int id, int peopleId, int type, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mDepartSource.changePersonInfo(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("changePersonInfo " + e.getMessage());
            }

            @Override
            public void onNext(Object baseBean) {
                refreshPlanData();
                refreshList();
            }
        }, userId(), keyCode(), id, peopleId, type);
    }

    /**
     * 修改计划发车时间
     *
     * @param id
     * @param vehTime
     */
    public void alertVehTime(int id, String vehTime, BasePresenter.LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        HttpMethods.getInstance().alertVehTime(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("alertVehTime " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                refreshPlanData();
                loadSendCarList();

            }
        }, userId(), keyCode(), id, vehTime);
    }

    public LineParams getLineParams() {
        return mLineParams;
    }


    public void getMissionList(final int objId, final int type, final String taskId) {
        try {
            HttpMethods.getInstance().missionList(new Subscriber<List<MissionType>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    LogUtil.loadRemoteError("missionList " + e.getMessage());
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

    public void changeMissionType(int objId, int type, String taskId) {
        mvpView.showLoading();
        HttpMethods.getInstance().missionType(new Subscriber() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("missionType " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                refreshPlanData();
                refreshList();

            }
        }, userId(), keyCode(), objId, type, taskId);
    }

    /**
     * 检测计划发车时间在30分钟内的车辆数
     * ------------------begin--------------
     */
    public void checkStopCar() {
        if (timer == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    DebugLog.w("--------检测进出站情况");
//                    checkIsTimeToSend(spotId);
                    loadStopCarList();
                }
            };
            timer.schedule(timerTask, 0, 1000 * 60);
        }

    }

    public void closeTimer() {
        if (timer != null) {
            timerTask.cancel();
            timer.cancel();
            timer = null;
            timerTask = null;
        }
    }

    public void checkVehicleCount(int spotId) {
        HttpMethods.getInstance().getVehicleNumber(new Subscriber<List<VehicleNumberBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("getVehicleNumber " + e.getMessage());
            }

            @Override
            public void onNext(List<VehicleNumberBean> vehicleNumberBeen) {
//                DebugLog.e("加载停场车辆-------");
//                if (mVehicleNumberBeen == null || !checkList(mVehicleNumberBeen, vehicleNumberBeen)){
//                    DebugLog.e("刷新停场车辆--------");
//                    mVehicleNumberBeen = vehicleNumberBeen;
//                    mvpView.refreshTimeToSendCarNum(vehicleNumberBeen);
//                }

                if (mVehicleNumberBeen != null && mVehicleNumberBeen.size() == vehicleNumberBeen.size()) {
                    for (int i = 0; i < vehicleNumberBeen.size(); i++) {
                        if (!TextUtils.equals(mVehicleNumberBeen.get(i).vehNumber, vehicleNumberBeen.get(i).vehNumber)
                                || !TextUtils.equals(mVehicleNumberBeen.get(i).lineId, vehicleNumberBeen.get(i).lineId)) {
                            mvpView.refreshTimeToSendCarNum(vehicleNumberBeen);
                            mVehicleNumberBeen.clear();
                            mVehicleNumberBeen.addAll(vehicleNumberBeen);
                            mvpView.hideLoading();
                            return;
                        }
                    }
                } else {
                    mvpView.refreshTimeToSendCarNum(vehicleNumberBeen);
                    mVehicleNumberBeen.clear();
                    mVehicleNumberBeen.addAll(vehicleNumberBeen);
                    mvpView.hideLoading();
                }

            }
        }, userId(), keyCode(), spotId);
    }
    /**
     * 检测计划发车时间在30分钟内的车辆数
     * ------------------end----------------
     */

    /**
     * 撤回待发车辆
     *
     * @param objId
     */
    public void callBackScheduleCar(int objId, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mvpView.showLoading();
        HttpMethods.getInstance().callBackScheduleCar(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("callBackScheduleCar " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                refreshList();

            }
        }, userId(), keyCode(), objId);
    }

    /**
     * 撤回已发车辆
     *
     * @param objId
     */
    public void callBackGoneCar(int objId, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mvpView.showLoading();
        HttpMethods.getInstance().callBackGoneCar(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("callBackGoneCar " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                refreshList();

            }
        }, userId(), keyCode(), objId);
    }

    /**
     * 修改已发车辆备注信息
     *
     * @param objId
     * @param status
     * @param remark
     */
    public void goneCarNormalRemarks(int objId, int status, String remark, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mvpView.showLoading();
        try {
            String remarkStr = new DESPlus().encrypt(Base64.encode(remark.getBytes("utf-8")));
            HttpMethods.getInstance().goneCarNormalRemarks(new Subscriber() {
                @Override
                public void onCompleted() {
                    mLoadDataStatus.OnLoadDataFinish();
                    mvpView.hideLoading();
                }

                @Override
                public void onError(Throwable e) {
                    mvpView.disPlay(e.getMessage());
                    LogUtil.loadRemoteError("goneCarNormalRemarks " + e.getMessage());
                }

                @Override
                public void onNext(Object o) {
                    refreshHistoryData();
                    loadGoneCarList();

                }
            }, userId(), keyCode(), objId, status, remarkStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改已发车辆备注信息_异常
     *
     * @param objId
     * @param status
     * @param remarks
     * @param runOnce
     * @param operateMileage
     * @param emptyMileage
     */
    public void goneCarAbNormalRemarks(int objId, int status, String remarks, int runOnce,
                                       double operateMileage, double emptyMileage, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mvpView.showLoading();
        try {
            String remarkStr = new DESPlus().encrypt(Base64.encode(remarks.getBytes("utf-8")));
            HttpMethods.getInstance().goneCarAbNormalRemarks(new Subscriber() {
                @Override
                public void onCompleted() {
                    mLoadDataStatus.OnLoadDataFinish();
                    mvpView.hideLoading();
                }

                @Override
                public void onError(Throwable e) {
                    mvpView.disPlay(e.getMessage());
                    LogUtil.loadRemoteError("goneCarAbNormalRemarks " + e.getMessage());
                }

                @Override
                public void onNext(Object o) {
                    refreshHistoryData();
                    loadGoneCarList();

                }
            }, userId(), keyCode(), objId, status, remarkStr, runOnce, operateMileage, emptyMileage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lineSupport(int objId, int supportLineId) {
        mvpView.showLoading();
        HttpMethods.getInstance().lineSupport(new Subscriber() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("lineSupport " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                mvpView.disPlay("操作成功");
                loadSendCarList();
                loadGoneCarList();


            }
        }, userId(), keyCode(), objId, supportLineId);
    }

    public void confirmInform(String vehicleId, String content, String typeId,String driverCode, BasePresenter.LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mvpView.showLoading();
        try {
            String remarkStr = new DESPlus().encrypt(Base64.encode(content.getBytes("utf-8")));
            HttpMethods.getInstance().confrimInform(new Subscriber() {
                @Override
                public void onCompleted() {
                    mLoadDataStatus.OnLoadDataFinish();
                    mvpView.hideLoading();
                }

                @Override
                public void onError(Throwable e) {
                    mvpView.disPlay(e.getMessage());
                    LogUtil.loadRemoteError("confrimInform " + e.getMessage());
                }

                @Override
                public void onNext(Object o) {
                    mvpView.disPlay("操作成功");
                    refreshList();
                }
            }, userId(), keyCode(), vehicleId, remarkStr, typeId, driverCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLineId() {
        return lineId;
    }

    public void stopCarEndToStay(int id) {
        mvpView.showLoading();
        mStopSource.stopCarEndToStay(new Subscriber() {
                                         @Override
                                         public void onCompleted() {
                                             mvpView.hideLoading();
                                         }

                                         @Override
                                         public void onError(Throwable e) {
                                             mvpView.disPlay(e.getMessage());
                                             LogUtil.loadRemoteError("stopCarEndToStay " + e.getMessage());
                                         }

                                         @Override
                                         public void onNext(Object o) {
                                             mvpView.disPlay("操作成功");
                                             refreshList();
                                         }
                                     },
                userId(), keyCode(), id);
    }

    public void stopCarStayToEnd(int id, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mvpView.showLoading();
        mStopSource.stopCarStayToEnd(new Subscriber() {
                                         @Override
                                         public void onCompleted() {
                                             mLoadDataStatus.OnLoadDataFinish();
                                             mvpView.hideLoading();
                                         }

                                         @Override
                                         public void onError(Throwable e) {
                                             mvpView.disPlay(e.getMessage());
                                             LogUtil.loadRemoteError("stopCarStayToEnd " + e.getMessage());
                                         }

                                         @Override
                                         public void onNext(Object o) {
                                             mvpView.disPlay("操作成功");
                                             refreshList();
                                         }
                                     },
                userId(), keyCode(), id);
    }

    /**
     * 要刷新列表所以要清空
     */
    private void refreshPlanData() {
        mWaitVehicles.clear();
        operatorVehicles.clear();
        noOperatorVehicles.clear();
    }

    /**
     * 要刷新列表所以要清空
     */
    private void refreshHistoryData() {
        mSendHistories.clear();
        operatorHistories.clear();
        noOperatorHistories.clear();
    }

    private void refreshStopData() {
        endStopHistories.clear();
        mStopHistories.clear();

    }

    // 待发车辆替换停场车辆
    public void updateWaitCarCode(int objId, final StopCarCodeBean bean, LoadDataStatus loadDataStatus) {
        this.mLoadDataStatus = loadDataStatus;
        mvpView.showLoading();
        HttpMethods.getInstance().updateWaitCarCode(new Subscriber() {
            @Override
            public void onCompleted() {
                mLoadDataStatus.OnLoadDataFinish();
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.hideLoading();
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("updateWaitCarCode " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                mvpView.disPlay("操作成功");
                refreshPlanData();
                loadSendCarList();

                refreshStopData();
                loadStopCarList();

            }
        }, userId(), keyCode(), objId, bean.vehicleId);

    }

    public void vehicleStopRemove(String objId){
        mvpView.showLoading();
        HttpMethods.getInstance().vehicleStopRemove(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());

            }

            @Override
            public void onNext(BaseBean baseBean) {
                ToastHelper.showToast("删除成功");
                loadStopCarByStay();

            }
        }, userId(), keyCode(), objId);
    }


    private Subscription runCarSubscription;


    // 47.根据任务线路id获取当前在跑的所有车辆(新)
    public void loadRunningCarCodeList(final int lineId) {
        HttpMethods.getInstance().runningCarsAtMap(new Subscriber<BaseBean<List<RunningCarBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("loadRunningCarCodeList " + e.getMessage());
            }

            @Override
            public void onNext(BaseBean<List<RunningCarBean>> runCarBeanList) {
                int size = runCarBeanList.returnData.size();
                if(size == 0){
                    mvpView.sendFailedRunCarCodeList();
                }else {
                    mvpView.sendSuccessRunCarCodeList(runCarBeanList.returnData);
                    DebugLog.e("查询lineId:"+lineId+"---");
                }
            }
        }, userId(),keyCode(),lineId + "");
    }


    public List<RunningCarBean> getRunningCarList() {
        return mRunningCarBean;
    }

    public void unSubscribe() {
        if (runCarSubscription != null)
            runCarSubscription.unsubscribe();
    }

}
