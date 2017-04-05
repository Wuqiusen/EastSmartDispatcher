package com.zxw.data.bean;

/**
 * Created by ZXW2016 on 2017/4/3.
 */

public class ScheduleHistoryBean {

    /**
     * code : 粤BDL457
     * driverName : 蔡常振
     * id : 3019
     * spaceTime : 10
     * status : 1
     * taskEditBelongId : 3
     * taskEditRunId : 3
     * taskId : 3
     * type : 1
     * typeName : M203上行
     * vehTime : 0800
     */

    private String code;
    private String driverName;
    private int id;
    private int spaceTime;
    private int status;
    private int taskEditBelongId;
    private int taskEditRunId;
    private int taskId;
    private int type;
    private String typeName;
    private String vehTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpaceTime() {
        return spaceTime;
    }

    public void setSpaceTime(int spaceTime) {
        this.spaceTime = spaceTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTaskEditBelongId() {
        return taskEditBelongId;
    }

    public void setTaskEditBelongId(int taskEditBelongId) {
        this.taskEditBelongId = taskEditBelongId;
    }

    public int getTaskEditRunId() {
        return taskEditRunId;
    }

    public void setTaskEditRunId(int taskEditRunId) {
        this.taskEditRunId = taskEditRunId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getVehTime() {
        return vehTime;
    }

    public void setVehTime(String vehTime) {
        this.vehTime = vehTime;
    }
}
