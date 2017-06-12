package com.zxw.data.bean;

import java.io.Serializable;

/**
 * author：CangJie on 2017/2/21 18:50
 * email：cangjie2016@gmail.com
 */
public class DepartCar implements Serializable {

    /**
     * arriveTime : 0535
     * code : 粤B074KN
     * driverCoed:007
     * driverName : 刘铁光
     * electronRailName : 淘金地停车场
     * id : 2
     * isDouble : 0
     * runNum : 5
     * runNumReal : 1
     * spaceTime : 5
     * type : 1
     * unRunTaskStatus : 1
     * vehTime : 0555
     */

    private String arriveTime;
    private String code;
    private String driverCode;
    private String driverName;
    private String stewardName;
    private String electronRailName;
    private int id;
    private int isDouble;
    private int spaceTime;
    private int type;
    private int unRunTaskStatus;
    private String taskId;
    private String vehTime;
    private String typeName;
    private String vehicleId;
    private String remarks;
    private int taskEditBelongId;
    private int taskEditRunId;

    private String taskEndTime;
    private String taskName;
    private int runNum;
    private int runNumReal;
    private int runEmpMileage;
    private int isNotice;  //1 未通知 2 已通知

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public int getIsNotice() {
        return isNotice;
    }

    public void setIsNotice(int isNotice) {
        this.isNotice = isNotice;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getRunNum() {
        return runNum;
    }

    public void setRunNum(int runNum) {
        this.runNum = runNum;
    }

    public int getRunNumReal() {
        return runNumReal;
    }

    public void setRunNumReal(int runNumReal) {
        this.runNumReal = runNumReal;
    }

    public int getRunEmpMileage() {
        return runEmpMileage;
    }

    public void setRunEmpMileage(int runEmpMileage) {
        this.runEmpMileage = runEmpMileage;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStewardName() {
        return stewardName;
    }

    public void setStewardName(String stewardName) {
        this.stewardName = stewardName;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

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

    public String getElectronRailName() {
        return electronRailName;
    }

    public void setElectronRailName(String electronRailName) {
        this.electronRailName = electronRailName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsDouble() {
        return isDouble;
    }

    public void setIsDouble(int isDouble) {
        this.isDouble = isDouble;
    }

    public int getSpaceTime() {
        return spaceTime;
    }

    public void setSpaceTime(int spaceTime) {
        this.spaceTime = spaceTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnRunTaskStatus() {
        return unRunTaskStatus;
    }

    public void setUnRunTaskStatus(int unRunTaskStatus) {
        this.unRunTaskStatus = unRunTaskStatus;
    }

    public String getVehTime() {
        return vehTime;
    }

    public void setVehTime(String vehTime) {
        this.vehTime = vehTime;
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

    public String getVehicleId() {
        return vehicleId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
