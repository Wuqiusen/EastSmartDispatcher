package com.zxw.dispatch.presenter;

import com.zxw.data.bean.SendHistory;
import com.zxw.data.source.SendSource;
import com.zxw.dispatch.presenter.view.SendView;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 15:22
 * email：cangjie2016@gmail.com
 */
public class SendPresenter extends BasePresenter<SendView> {
    private int lineId, stationId;

    private SendSource mSource = new SendSource();
    public SendPresenter(SendView mvpView, int lineId, int stationId) {
        super(mvpView);
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public void loadSend() {
        mSource.loadSend(new Subscriber<List<SendHistory>>() {
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
                mvpView.loadSend(sendHistories);
            }
        }, code(), lineId, stationId, keyCode(), 1, 20);
    }
}
