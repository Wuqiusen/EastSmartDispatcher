package com.zxw.dispatch.presenter;

import android.content.Context;

import com.zxw.data.bean.ContactInfo;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.presenter.view.CallLauncherView;
import com.zxw.dispatch.utils.SpUtils;

import java.util.List;

import rx.Subscriber;

public class CallLauncherPresenter extends BasePresenter<CallLauncherView> {
    private final Context mContext;

    public CallLauncherPresenter(CallLauncherView mvpView, Context context) {
        super(mvpView);
        this.mContext = context;
    }

    public void contactList(){
        String userId = SpUtils.getCache(mContext, SpUtils.CALL_USER_ID);
        String keyCode = SpUtils.getCache(mContext, SpUtils.CALL_KEYCODE);
        HttpMethods.getInstance().contactList(new Subscriber<List<ContactInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<ContactInfo> contactInfos) {
                mvpView.setAdapter(contactInfos);
            }
        }, userId, keyCode);
    }
}
