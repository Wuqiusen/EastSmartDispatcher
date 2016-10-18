package com.zxw.data.fake_source;

/**
 * author：CangJie on 2016/9/21 15:16
 * email：cangjie2016@gmail.com
 */
public class End {
    public String carSequence;
    public String carCode;
    public String driver;
    public String trainman;
    public String planTime;
    public String intervalTime;
    public String systemEnterTime;
    public String enterTime;
    public String state;
    public String endTime;
    public String remark;

    public End(String carSequence, String carCode, String driver, String trainman, String planTime, String intervalTime, String systemEnterTime,
               String enterTime, String state, String endTime, String remark) {
        this.carSequence = carSequence;
        this.carCode = carCode;
        this.driver = driver;
        this.trainman = trainman;
        this.planTime = planTime;
        this.intervalTime = intervalTime;
        this.systemEnterTime = systemEnterTime;
        this.enterTime = enterTime;
        this.endTime = endTime;
        this.remark = remark;
        this.state = state;
    }
}
