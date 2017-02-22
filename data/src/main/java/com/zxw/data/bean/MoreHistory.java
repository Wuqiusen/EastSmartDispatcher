package com.zxw.data.bean;

/**
 * author：CangJie on 2016/10/13 09:43
 * email：cangjie2016@gmail.com
 */
public class MoreHistory {

    public int id;
    public String inTime1;
    public String inTime2;
    public int isScan;
    public int mainId;
    public String opTime;
    public int opTimeKey;
    public int opType;
    public String projectTime;
    public String remarks;
    public String scName;
    public String sjName;
    public int sortNum;
    public int spaceMin;
    public String vehCode;

    @Override
    public String toString() {
        return "MoreHistory{" +
                "lineId=" + id +
                ", inTime1='" + inTime1 + '\'' +
                ", inTime2='" + inTime2 + '\'' +
                ", isScan=" + isScan +
                ", mainId=" + mainId +
                ", opTime='" + opTime + '\'' +
                ", opTimeKey=" + opTimeKey +
                ", opType=" + opType +
                ", projectTime='" + projectTime + '\'' +
                ", remarks='" + remarks + '\'' +
                ", scName='" + scName + '\'' +
                ", sjName='" + sjName + '\'' +
                ", sortNum=" + sortNum +
                ", spaceMin=" + spaceMin +
                ", vehCode='" + vehCode + '\'' +
                '}';
    }
}
