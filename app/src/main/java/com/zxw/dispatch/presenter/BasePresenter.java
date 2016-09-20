package com.zxw.dispatch.presenter;

import com.zxw.dispatch.presenter.view.BaseView;

/**
 * author：CangJie on 2016/8/18 14:38
 * email：cangjie2016@gmail.com
 */
public class BasePresenter<V extends BaseView> {
    public V mvpView;
    protected int httpCount;

    public BasePresenter(V mvpView) {
        this.mvpView = mvpView;
    }
//    protected String keyCode(){
//        return SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);
//    }
//    protected String userName(){
//        return SpUtils.getCache(MyApplication.mContext, SpUtils.USERPHONE);
//    }
//    protected String userId(){
//        return SpUtils.getCache(MyApplication.mContext, SpUtils.USERID);
//    }
//    protected String timestamp(){
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//        return formatter.format(curDate);
//    }
//    protected String md5(String str){
//        return MD5.MD5Encode(str);
//    }
}
