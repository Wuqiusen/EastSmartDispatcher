package com.zxw.data.bean;

import java.util.List;

/**
 * author MXQ
 * create at 2017/4/12 10:24
 * email: 1299242483@qq.com
 */
public class SchedulePlanBean {

    /**
     * data : [{"driverName":"张三","runNum":2.6,"runNumReal":2.6,"runNumRealTwo":2.6,"runNumTwo":2.6,"stewardName":"小红","vehicleNameTwo":"粤BDL440"}]
     * single : 1
     * sortNum : 1
     * time : 0603
     * vehicleName : 粤BDL447
     */

    private int single;
    private int sortNum;
    private String time;
    private String vehicleName;
    /**
     * driverName : 张三
     * runNum : 2.6
     * runNumReal : 2.6
     * runNumRealTwo : 2.6
     * runNumTwo : 2.6
     * stewardName : 小红
     * vehicleNameTwo : 粤BDL440
     */

    private List<DataBean> data;

    public int getSingle() {
        return single;
    }

    public void setSingle(int single) {
        this.single = single;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String driverName;
        private double runNum;
        private double runNumReal;
        private double runNumRealTwo;
        private double runNumTwo;
        private String stewardName;
        private String vehicleNameTwo;
        private String workTime;

        public String getWorkTime() {
            return workTime;
        }

        public void setWorkTime(String workTime) {
            this.workTime = workTime;
        }

        public String getDriverName() {
            return driverName;
        }

        public void setDriverName(String driverName) {
            this.driverName = driverName;
        }

        public double getRunNum() {
            return runNum;
        }

        public void setRunNum(double runNum) {
            this.runNum = runNum;
        }

        public double getRunNumReal() {
            return runNumReal;
        }

        public void setRunNumReal(double runNumReal) {
            this.runNumReal = runNumReal;
        }

        public double getRunNumRealTwo() {
            return runNumRealTwo;
        }

        public void setRunNumRealTwo(double runNumRealTwo) {
            this.runNumRealTwo = runNumRealTwo;
        }

        public double getRunNumTwo() {
            return runNumTwo;
        }

        public void setRunNumTwo(double runNumTwo) {
            this.runNumTwo = runNumTwo;
        }

        public String getStewardName() {
            return stewardName;
        }

        public void setStewardName(String stewardName) {
            this.stewardName = stewardName;
        }

        public String getVehicleNameTwo() {
            return vehicleNameTwo;
        }

        public void setVehicleNameTwo(String vehicleNameTwo) {
            this.vehicleNameTwo = vehicleNameTwo;
        }
    }
}
