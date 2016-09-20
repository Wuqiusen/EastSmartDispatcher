package com.zxw.dispatch.ui.base;

import android.os.Bundle;

/**
 * author：CangJie on 2016/8/18 16:44
 * email：cangjie2016@gmail.com
 */
public abstract class PresenterActivity<P> extends BaseHeadActivity {
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onCreate(savedInstanceState);

    }

    protected abstract P createPresenter();
}
