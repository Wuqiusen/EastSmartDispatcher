package com.zxw.data.bean;

/**
 * author：CangJie on 2017/2/21 18:42
 * email：cangjie2016@gmail.com
 */
public class LineParams {

    @Override
    public String toString() {
        return "LineParams{" +
                "saleType='" + saleType + '\'' +
                ", timeType=" + timeType +
                '}';
    }

    /**
     * saleType : 2
     * timeType : 1
     */

    private int saleType;
    private int timeType;

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }
}
