package com.zxw.data.bean;

/**
 * Created by 李振强 on 2017/5/20.
 */

public class DriverWorkloadItem {
    private long objId;
    private String vehCode;
    private String vehTime;
    private String driverName;
    private String outTime;
    private String arrivalTime;
    private String gps; //0-error  1-normal
    private String opStatus; //司机操作状态 2-未结束 3-异常终止  4-正常结束
    private String taskName;
    private String content;

    public long getObjId() {
        return objId;
    }

    public void setObjId(long objId) {
        this.objId = objId;
    }

    public String getVehCode() {
        return vehCode;
    }

    public void setVehCode(String vehCode) {
        this.vehCode = vehCode;
    }

    public String getVehTime() {
        return vehTime;
    }

    public void setVehTime(String vehTime) {
        this.vehTime = vehTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(String opStatus) {
        this.opStatus = opStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
