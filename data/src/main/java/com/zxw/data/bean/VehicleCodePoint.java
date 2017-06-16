package com.zxw.data.bean;

/**
 * Created by moxiaoqing on 2017/6/9.
 */

public class VehicleCodePoint {
    private RunningCarBean runBean;
    private float latitude;
    private float longitude;

    public VehicleCodePoint(RunningCarBean runBean,float latitude,float longitude){
        this.runBean = runBean;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public RunningCarBean getRunBean() {
        return runBean;
    }

    public void setRunBean(RunningCarBean runBean) {
        this.runBean = runBean;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
