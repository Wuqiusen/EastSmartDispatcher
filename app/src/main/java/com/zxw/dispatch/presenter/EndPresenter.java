package com.zxw.dispatch.presenter;

import com.zxw.data.bean.BackHistory;
import com.zxw.data.source.BackSource;
import com.zxw.dispatch.presenter.view.EndView;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 15:22
 * email：cangjie2016@gmail.com
 */
public class EndPresenter extends BasePresenter<EndView> {
    private int lineId, stationId;

    private BackSource mSource = new BackSource();
    public EndPresenter(EndView mvpView, int lineId, int stationId) {
        super(mvpView);
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public void loadSend() {
        mSource.loadBack(new Subscriber<List<BackHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<BackHistory> backHistories) {
                mvpView.loadEnd(backHistories);
            }
        }, userId(), lineId, stationId, keyCode(), 1, 20);
    }
}
