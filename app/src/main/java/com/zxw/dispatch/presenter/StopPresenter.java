package com.zxw.dispatch.presenter;

import com.zxw.data.bean.StopHistory;
import com.zxw.data.source.StopSource;
import com.zxw.dispatch.presenter.view.StopView;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 15:22
 * email：cangjie2016@gmail.com
 */
public class StopPresenter extends BasePresenter<StopView> {

    private int lineId, stationId;
    private StopSource mSource = new StopSource();
    public StopPresenter(StopView mvpView, int lineId, int stationId) {
        super(mvpView);
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public void loadStop() {
        mSource.loadStop(new Subscriber<List<StopHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<StopHistory> stopHistories) {
                mvpView.loadStop(stopHistories);
            }
        }, userId(), lineId, stationId, keyCode(), 1, 20);
    }
}
