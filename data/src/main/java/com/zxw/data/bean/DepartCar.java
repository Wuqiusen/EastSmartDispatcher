package com.zxw.data.bean;

/**
 * author：CangJie on 2017/2/21 18:50
 * email：cangjie2016@gmail.com
 */
public class DepartCar {

    /**
     * arriveTime : 0535
     * code : 粤B074KN
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
    private String driverName;
    private String stewardName;
    private String electronRailName;
    private int id;
    private int isDouble;
    private int spaceTime;
    private int type;
    private int unRunTaskStatus;
    private String vehTime;

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
}