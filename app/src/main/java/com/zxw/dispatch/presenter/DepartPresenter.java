package com.zxw.dispatch.presenter;

import android.content.Context;
import android.content.Intent;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.WaitVehicle;
import com.zxw.data.source.DepartSource;
import com.zxw.data.source.QuerySource;
import com.zxw.dispatch.presenter.view.DepartView;
import com.zxw.dispatch.service.CarPlanService;
import com.zxw.dispatch.view.DragListAdapter;
import com.zxw.dispatch.view.SendCarDialog;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 11:13
 * email：cangjie2016@gmail.com
 */
public class DepartPresenter extends BasePresenter<DepartView> {
    DepartSource mSource = new DepartSource();
    QuerySource mQuerySource = new QuerySource();
    private List<WaitVehicle> mDatas;
    private Context mContext;
    private DragListAdapter mDragListAdapter;
    private int lineId, stationId, currentType = MANUAL_TYPE;
    private static final int AUTO_TYPE = 1; // 自动发车
    private static final int MANUAL_TYPE = 2; // 手动发车
    private Intent serviceIntent;

    public DepartPresenter(Context context, DepartView mvpView, int lineId, int stationId) {
        super(mvpView);
        this.mContext = context;
        this.lineId = lineId;
        this.stationId = stationId;
    }

    /**
     * 加载车辆列表
     * 显示Loading图层
     * 成功返回数据, new适配器并传到view层, 隐藏loading
     * 失败, 隐藏Loading图层, 显示错误returnInfo, 并结束界面
     */
    public void loadCarData() {
        mvpView.showLoading();
        mSource.loadWaitVehicle(new Subscriber<List<WaitVehicle>>() {
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
                mDatas = waitVehicles;
                mDragListAdapter = new DragListAdapter(mContext, DepartPresenter.this, mDatas);
                mvpView.loadLine(mDragListAdapter);
            }
        }, userId(), lineId, stationId, keyCode(), 1, 20);

    }

    /**
     * 增加车辆, 显示Loading图层,
     * 如增加成功, 则重新加载车辆列表,
     * 若增加失败, 则显示returnInfo 并隐藏Loading图层
     */
    public void add() {
        new SendCarDialog(mContext).create(new SendCarDialog.OnDialogCreateConfirmListener() {
            @Override
            public void onDialogCreateConfirm(int vehId, int sjId, String scId, String projectTime, int spaceMin, String inTime2, String sortNum) {
                mvpView.showLoading();
                mSource.addVehicle(new Subscriber<BaseBean>() {
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
                        loadCarData();
                    }
                }, userId(), keyCode(), lineId, stationId, vehId, sjId, scId, projectTime, spaceMin, inTime2, sortNum);
            }
        }).dialogShow();
    }

    /**
     * 线路重新排序
     * 如增加成功, 则重新加载车辆列表,
     * 若增加失败, 则显示returnInfo 并隐藏Loading图层
     */
    public void sortVehicle(int opId, int replaceId) {
        mSource.sortVehicle(new Subscriber<BaseBean>() {
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
                loadCarData();
            }
        }, userId(), keyCode(), opId, replaceId);
    }

    /**
     * 调度员发车
     */
    public void sendVehicle(int opId){
        mvpView.showCtrlCarLoading();
        mSource.sendVehicle(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                mvpView.hideCtrlCarLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());

            }

            @Override
            public void onNext(BaseBean baseBean) {
                mvpView.disPlay(baseBean.returnInfo);
                loadCarData();
            }
        }, userId(), keyCode(), opId, currentType);
    }

    /**
     * 点击自动发车
     */
    public void selectAuto() {
        currentType = AUTO_TYPE;
        serviceIntent = new Intent(mContext,CarPlanService.class);
        serviceIntent.putExtra("stationId", stationId);
        serviceIntent.putExtra("lineId", lineId);
        mContext.startService(serviceIntent);
    }

    /**
     * 点击手动发车
     */
    public void selectManual() {
        currentType = MANUAL_TYPE;
        Intent intent = new Intent("com.zxw.dispatch.service.RECEIVER");
        intent.putExtra("stationId", stationId);
        intent.putExtra("lineId", lineId);
        mContext.sendBroadcast(intent);
    }

    /**
     * 修改待操作车辆信息
     */
    public void updateVehicle(int opId, int vehId,
                              int sjId, String scId, String projectTime,
                              int spaceMin, String inTime2) {
        mSource.updateVehicle(new Subscriber<BaseBean>() {
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
                loadCarData();
            }
        }, userId(), keyCode(), opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
    }
}
