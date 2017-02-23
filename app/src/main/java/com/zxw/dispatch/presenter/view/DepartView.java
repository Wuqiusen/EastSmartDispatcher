package com.zxw.dispatch.presenter.view;

import com.zxw.dispatch.adapter.DragListAdapter;

/**
 * author：CangJie on 2016/9/21 11:14
 * email：cangjie2016@gmail.com
 */
public interface DepartView extends BaseView{
    void loadLine(DragListAdapter adapter);

    void showCtrlCarLoading();
    void hideCtrlCarLoading();
}
