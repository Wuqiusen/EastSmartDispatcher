package com.zxw.data.bean;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public class RunGpsBean {
    private String driverName;
    private String driverCode;
    private double latitude;
    private double longitude;

    public RunGpsBean(String driverName,String driverCode, double latitude,double longitude){
        this.driverName = driverName;
        this.driverCode = driverCode;
    }


    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
