package com.zxw.dispatch.presenter.view;

import com.zxw.data.bean.VersionBean;

/**
 * 作者：${MXQ} on 2017/2/8 11:29
 * 邮箱：1299242483@qq.com
 */
public interface WelcomeView extends BaseView {
    void respondVersionSuccess(long startTime,VersionBean versionBean);
    void respondVersionFailed(String errorMessage,long startTime,String keyCode);
}
