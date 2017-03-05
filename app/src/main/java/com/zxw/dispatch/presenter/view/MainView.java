package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.recycler.GoneAdapter;

import java.util.List;

/**
 * author：CangJie on 2016/9/20 17:26
 * email：cangjie2016@gmail.com
 */
public interface MainView extends BaseView {
    void loadLine(List<Line> lineList);

    void reLogin();

    void loadSendCarList(DragListAdapter mDragListAdapter);

    void loadGoneCarList(GoneAdapter sendHistories);

    void loadStopCarList(List<StopHistory> stopHistories);

    void showMissionTypeDialog(List<MissionType> missionTypes, int objId, int type, String taskId);

    void refreshTimeToSendCarNum(List<Integer> sendCarNum);

    void hideStewardName();

    void showStewardName();
}
