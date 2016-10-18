package com.zxw.data.bean;

/**
 * author：CangJie on 2016/10/17 09:22
 * email：cangjie2016@gmail.com
 */
public class Person {
    public int companyId;
    public String companyName;
    public int id;
    public String isDele;
    public int orgId;
    public String orgName;
    public String personnelCode;
    public String personnelName;
    public String personnelType;
    public int version;

    @Override
    public String toString() {
        return "Person{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", id=" + id +
                ", isDele='" + isDele + '\'' +
                ", orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", personnelCode='" + personnelCode + '\'' +
                ", personnelName='" + personnelName + '\'' +
                ", personnelType='" + personnelType + '\'' +
                ", version=" + version +
                '}';
    }
}
