package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.view.DragListAdapter;

import java.util.List;

/**
 * author：CangJie on 2016/9/20 17:26
 * email：cangjie2016@gmail.com
 */
public interface MainView extends BaseView {
    void loadLine(List<Line> lineList);

    void reLogin();

    void loadSendCarList(DragListAdapter mDragListAdapter);

    void loadGoneCarList(List<SendHistory> sendHistories);

    void loadStopCarList(List<StopHistory> stopHistories);
}
