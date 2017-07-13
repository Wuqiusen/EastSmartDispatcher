package com.zxw.dispatch.presenter;

import com.zxw.data.bean.GroupMessageDetail;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.presenter.view.GroupMessageDetailView;

import rx.Subscriber;

public class GroupMessageDetailPresenter extends BasePresenter<GroupMessageDetailView> {
    public GroupMessageDetailPresenter(GroupMessageDetailView mvpView) {
        super(mvpView);
    }

    public void loadData(int messageId) {
        if (messageId == -1)
            return;
        mvpView.showLoading();
        HttpMethods.getInstance().groupMessageDetailRecord(new Subscriber<GroupMessageDetail>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.hideLoading();
            }

            @Override
            public void onNext(GroupMessageDetail groupMessageDetail) {
                mvpView.showContent(groupMessageDetail);
            }
        }, userId(), keyCode(), messageId);
    }
}
