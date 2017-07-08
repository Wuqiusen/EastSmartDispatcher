package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.MissionType;
import com.zxw.data.bean.RunningCarBean;
import com.zxw.data.bean.SendHistory;
import com.zxw.data.bean.StopHistory;
import com.zxw.data.bean.VehicleNumberBean;
import com.zxw.dispatch.adapter.DragListAdapter;
import com.zxw.dispatch.adapter.DragListAdapterForNotOperatorEmpty;
import com.zxw.dispatch.adapter.DragListAdapterForOperatorEmpty;
import com.zxw.dispatch.recycler.GoneAdapterForNotOperatorEmpty;
import com.zxw.dispatch.recycler.GoneAdapterForOperatorEmpty;
import com.zxw.dispatch.recycler.NonMissionTypeAdapter;

import java.util.List;

/**
 * author：CangJie on 2016/9/20 17:26
 * email：cangjie2016@gmail.com
 */
public interface MainView extends BaseView {
    void loadLine(List<Line> lineList);

    void reLogin();

    void loadSendCarList(DragListAdapter mDragListAdapter);

    void loadGoneCarByNormal(List<SendHistory> sendHistories, int count);

    void loadStopStayCarList(List<StopHistory> stopHistories);

    void loadStopEndCarList(List<StopHistory> stopHistories);

    void showMissionTypeDialog(List<MissionType> missionTypes, int objId, int type, String taskId, String lineName);

    void refreshTimeToSendCarNum(List<VehicleNumberBean> sendCarNum);

    void setSaleType(LineParams lineParams);

    void nonMissionTypeDialog(NonMissionTypeAdapter adapter);

    void hideStewardName();

    void showStewardName();

    void onGetAddRecordingTaskNameList(List<MissionType> missionTypes);


    void loadGoneCarByOperatorEmpty(GoneAdapterForOperatorEmpty goneAdapter);

    void loadGoneCarByNotOperatorEmpty(GoneAdapterForNotOperatorEmpty goneAdapter);

    void loadSendCarForOperatorEmpty(DragListAdapterForOperatorEmpty mDragListAdapter);

    void loadSendCarForNotOperatorEmpty(DragListAdapterForNotOperatorEmpty mDragListAdapter);

    void onSelectLine();

    void sendSuccessRunCarCodeList(List<RunningCarBean> runCarList);

    void sendFailedRunCarCodeList();


}
