package com.zxw.dispatch.presenter.view;


import com.zxw.data.bean.ContactInfo;

import java.util.List;

public interface CallLauncherView extends BaseView {
    void setAdapter(List<ContactInfo> contactInfos);
}
