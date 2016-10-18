package com.zxw.data.bean;

/**
 * author：CangJie on 2016/10/12 17:13
 * email：cangjie2016@gmail.com
 */
public class WaitVehicle {
    public int id;
    public String inTime1;
    public String inTime2;
    public int isScan;
    public int mainId;
    public int scId;
    public int sjId;
    public int vehId;
    public String projectTime;
    public String realTime;
    public String scName;
    public String sjName;
    public int sortNum;
    public String spaceMin;
    public String vehCode;



    @Override
    public String toString() {
        return "WaitVehicle{" +
                "id=" + id +
                ", inTime1='" + inTime1 + '\'' +
                ", inTime2='" + inTime2 + '\'' +
                ", isScan=" + isScan +
                ", mainId=" + mainId +
                ", projectTime='" + projectTime + '\'' +
                ", realTime='" + realTime + '\'' +
                ", scName='" + scName + '\'' +
                ", sjName='" + sjName + '\'' +
                ", sortNum=" + sortNum +
                ", spaceMin=" + spaceMin +
                ", vehCode='" + vehCode + '\'' +
                '}';
    }
}
