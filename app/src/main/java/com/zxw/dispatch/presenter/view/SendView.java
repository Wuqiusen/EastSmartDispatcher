package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.SendHistory;

import java.util.List;

/**
 * author：CangJie on 2016/9/21 15:22
 * email：cangjie2016@gmail.com
 */
public interface SendView extends BaseView {
    void loadSend(List<SendHistory> sends);
}
