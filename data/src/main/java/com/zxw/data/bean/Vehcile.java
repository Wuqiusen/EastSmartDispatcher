package com.zxw.data.bean;

/**
 * author：CangJie on 2016/10/17 09:21
 * email：cangjie2016@gmail.com
 */
public class Vehcile {
    public int companyId;
    public String companyName;
    public int id;
    public String isDele;
    public String lineCode;
    public int lineId;
    public int organizationId;
    public String organizationName;
    public int status;
    public String vehicleCode;
    public int version;

    @Override
    public String toString() {
        return "Vehcile{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", id=" + id +
                ", isDele='" + isDele + '\'' +
                ", lineCode='" + lineCode + '\'' +
                ", lineId=" + lineId +
                ", organizationId=" + organizationId +
                ", organizationName='" + organizationName + '\'' +
                ", status=" + status +
                ", vehicleCode='" + vehicleCode + '\'' +
                ", version=" + version +
                '}';
    }
}
