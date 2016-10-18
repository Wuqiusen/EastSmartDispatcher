package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.MoreHistory;

import java.util.List;

/**
 * author：CangJie on 2016/9/21 15:22
 * email：cangjie2016@gmail.com
 */
public interface MoreView extends BaseView {
    void loadMore(List<MoreHistory> mores);
}
