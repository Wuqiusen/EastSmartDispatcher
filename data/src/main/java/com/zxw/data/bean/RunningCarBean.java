package com.zxw.data.bean;

/**
 * Created by moxiaoqing on 2017/6/7.
 */

public class RunningCarBean {
    private String vehicleCode;
    private String vehicleId;
    private String driverName;
    private String driverId;
    private String stewartId;
    private String stewartName;

    // 建立经度和纬度标识
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getStewartId() {
        return stewartId;
    }

    public void setStewartId(String stewartId) {
        this.stewartId = stewartId;
    }

    public String getStewartName() {
        return stewartName;
    }

    public void setStewartName(String stewartName) {
        this.stewartName = stewartName;
    }

}
