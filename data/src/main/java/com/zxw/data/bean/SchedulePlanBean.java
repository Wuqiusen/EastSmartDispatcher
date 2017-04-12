package com.zxw.data.bean;

/**
 * author MXQ
 * create at 2017/4/12 10:24
 * email: 1299242483@qq.com
 */
public class SchedulePlanBean {
    public DoubleBill doubleBill;
    public Integer id;
    public Integer isDouble;
    public SingleBill singleBill;
    public Integer sortNum;
    public String vehCode;
    public Integer vehId;

    public DoubleBill getDoubleBill() {
        return doubleBill;
    }

    public void setDoubleBill(DoubleBill doubleBill) {
        this.doubleBill = doubleBill;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsDouble() {
        return isDouble;
    }

    public void setIsDouble(Integer isDouble) {
        this.isDouble = isDouble;
    }

    public SingleBill getSingleBill() {
        return singleBill;
    }

    public void setSingleBill(SingleBill singleBill) {
        this.singleBill = singleBill;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getVehCode() {
        return vehCode;
    }

    public void setVehCode(String vehCode) {
        this.vehCode = vehCode;
    }

    public Integer getVehId() {
        return vehId;
    }

    public void setVehId(Integer vehId) {
        this.vehId = vehId;
    }
}
