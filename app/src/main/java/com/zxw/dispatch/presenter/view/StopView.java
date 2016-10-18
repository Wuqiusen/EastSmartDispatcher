package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.StopHistory;

import java.util.List;

/**
 * author：CangJie on 2016/9/21 15:22
 * email：cangjie2016@gmail.com
 */
public interface StopView extends BaseView {
    void loadStop(List<StopHistory> stops);
}
