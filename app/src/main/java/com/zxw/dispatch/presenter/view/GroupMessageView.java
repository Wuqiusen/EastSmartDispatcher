package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.GroupMessagesBean;

import java.util.List;

public interface GroupMessageView extends BaseView {
    void stopMore();

    void setData(List<GroupMessagesBean> returnData);

    void onRefresh();
}
