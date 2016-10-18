package com.zxw.dispatch.presenter;

import com.zxw.data.bean.Line;
import com.zxw.data.source.MainSource;
import com.zxw.dispatch.presenter.view.MainView;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/20 17:26
 * email：cangjie2016@gmail.com
 */
public class MainPresenter extends BasePresenter<MainView> {

    private final MainSource mSource;

    public MainPresenter(MainView mvpView) {
        super(mvpView);
        mSource = new MainSource();
    }

    public void loadLineList(){
        mvpView.showLoading();

        mSource.loadLine(new Subscriber<List<Line>>() {
            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mvpView.hideLoading();
                mvpView.disPlay(e.getMessage());
                mvpView.reLogin();
            }

            @Override
            public void onNext(List<Line> lineBeen) {
                mvpView.loadLine(lineBeen);
            }
        }, code(), keyCode(), 1, 20);
    }
}
