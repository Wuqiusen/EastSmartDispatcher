package com.zxw.data.bean;

/**
 * Created by moxiaoqing on 2017/5/19.
 */

public class WorkLoadVerifyBean {
    public int id; // 唯一id
    public int  no;
    public String  vehId;
    public String driverName;
    public String startTime;
    public String startStation;
    public String endStation;
    public String gps;
    // 0:待确认、1:不同意、2:同意
    public int driverOk;

}
