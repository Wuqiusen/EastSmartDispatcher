package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.Line;

import java.util.List;

/**
 * author：CangJie on 2016/9/20 17:26
 * email：cangjie2016@gmail.com
 */
public interface MainView extends BaseView {
    void loadLine(List<Line> lineList);

    void reLogin();
}
