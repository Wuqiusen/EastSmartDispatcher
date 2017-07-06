package com.zxw.dispatch.presenter;

import android.text.TextUtils;

import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.GroupMessagesBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.LogUtil;
import com.zxw.dispatch.presenter.view.GroupMessageView;
import com.zxw.dispatch.utils.Base64;
import com.zxw.dispatch.utils.DESPlus;

import java.util.List;

import rx.Subscriber;

public class GroupMessagePresenter extends BasePresenter<GroupMessageView> {
    private final String mLineId;
    private int mCurrentPage, mPageSize;
    private final static int LOAD_PAGE_SIZE = 20;
    public GroupMessagePresenter(GroupMessageView mvpView, int lineId) {
        super(mvpView);
        this.mLineId = String.valueOf(lineId);
    }

    public void loadGroupMessageRecord(final int currentPage) {
        mvpView.showLoading();
        HttpMethods.getInstance().groupMessageRecord(new Subscriber<BaseBean<List<GroupMessagesBean>>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BaseBean<List<GroupMessagesBean>> groupMessagesBeen) {
                if (groupMessagesBeen.returnCode != 500){
                    mvpView.hideLoading();
                    mvpView.disPlay(groupMessagesBeen.returnInfo);
                    mvpView.stopMore();
                    return;
                }
                mPageSize = groupMessagesBeen.returnSize;
                mvpView.setData(groupMessagesBeen.returnData);
            }

        }, userId(), keyCode(), mLineId, currentPage, LOAD_PAGE_SIZE);
    }

    public void loadMore() {
        if (mCurrentPage * LOAD_PAGE_SIZE >= mPageSize) {
            mvpView.stopMore();
        } else {
            mCurrentPage++;
            loadGroupMessageRecord(mCurrentPage);
        }
    }

    public void onRefresh() {
        mCurrentPage = 1;
        loadGroupMessageRecord(mCurrentPage);
    }

    public void sendGroupMessage(String message) {

        if (!TextUtils.isEmpty(message))
            try {
                message = new DESPlus().encrypt(Base64.encode(message.getBytes("utf-8")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        mvpView.showLoading();
        HttpMethods.getInstance().sendGroupMessage(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.disPlay(e.getMessage());
                LogUtil.loadRemoteError("sendGroupMessage " + e.getMessage());
            }

            @Override
            public void onNext(BaseBean baseBean) {
                mvpView.disPlay(baseBean.returnInfo);
                mvpView.onRefresh();
            }
        }, userId(), keyCode(), Long.valueOf(mLineId), message);
    }
}
