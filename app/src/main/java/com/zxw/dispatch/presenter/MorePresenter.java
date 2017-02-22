package com.zxw.dispatch.presenter;

import com.zxw.data.bean.MoreHistory;
import com.zxw.data.source.MoreSource;
import com.zxw.dispatch.presenter.view.MoreView;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 15:22
 * email：cangjie2016@gmail.com
 */
public class MorePresenter extends BasePresenter<MoreView> {

    private int lineId, stationId;
    private MoreSource mSource = new MoreSource();
    public MorePresenter(MoreView mvpView, int lineId, int stationId) {
        super(mvpView);
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public void loadMore() {
        mSource.loadMore(new Subscriber<List<MoreHistory>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<MoreHistory> moreHistories) {
                mvpView.loadMore(moreHistories);
            }
        }, userId(), lineId, stationId, keyCode(), 1, 20);
    }
}
