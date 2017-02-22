package com.zxw.dispatch.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zxw.data.bean.SpotBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.presenter.view.SelectDispatcherPointView;
import com.zxw.dispatch.recycler.SpotAdapter;
import com.zxw.dispatch.ui.LoginActivity;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.SpUtils;

import java.util.List;

import rx.Subscriber;

public class SelectDispatcherPointPresenter extends BasePresenter<SelectDispatcherPointView> {
    private final Context mContext;

    public SelectDispatcherPointPresenter(SelectDispatcherPointView mvpView, Context mContext) {
        super(mvpView);
        this.mContext = mContext;
    }

    public void loadDispatcherPoint() {
        HttpMethods.getInstance().dispatcherSpotList(new Subscriber<List<SpotBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                DebugLog.w(e.getMessage());
                if (e.getMessage().equals("重复登录")){
                    SpUtils.logOut(mContext);
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                    ((Activity)mContext).finish();
                }
            }

            @Override
            public void onNext(List<SpotBean> spotBeen) {
                SpotAdapter spotAdapter = new SpotAdapter(spotBeen, mContext);
                mvpView.showSpotList(spotAdapter);
            }
        }, userId(), keyCode());
    }
}
