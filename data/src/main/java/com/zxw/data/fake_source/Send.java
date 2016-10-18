package com.zxw.data.fake_source;

/**
 * author：CangJie on 2016/9/21 15:16
 * email：cangjie2016@gmail.com
 */
public class Send {
    public String carSequence;
    public String carCode;
    public String driver;
    public String trainman;
    public String planTime;
    public String intervalTime;
    public String systemEnterTime;
    public String enterTime;
    public String sendTime;
    public String sendState;
    public String state;

    public Send(String carSequence, String carCode, String driver, String trainman, String planTime, String intervalTime, String systemEnterTime, String enterTime, String sendTime, String sendState, String state) {
        this.carSequence = carSequence;
        this.carCode = carCode;
        this.driver = driver;
        this.trainman = trainman;
        this.planTime = planTime;
        this.intervalTime = intervalTime;
        this.systemEnterTime = systemEnterTime;
        this.enterTime = enterTime;
        this.sendTime = sendTime;
        this.sendState = sendState;
        this.state = state;
    }
}
