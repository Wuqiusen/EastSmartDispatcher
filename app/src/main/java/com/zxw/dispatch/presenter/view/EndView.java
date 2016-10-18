package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.BackHistory;

import java.util.List;

/**
 * author：CangJie on 2016/9/21 15:22
 * email：cangjie2016@gmail.com
 */
public interface EndView extends BaseView {
    void loadEnd(List<BackHistory> backs);
}
