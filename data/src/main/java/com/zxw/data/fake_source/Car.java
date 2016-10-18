package com.zxw.data.fake_source;

/**
 * author：CangJie on 2016/9/21 11:22
 * email：cangjie2016@gmail.com
 */
public class Car {
    public String carSequence;
    public String carCode;
    public String driver;
    public String trainman;
    public String planTime;
    public String intervalTime;
    public String systemEnterTime;
    public String enterTime;
    public String state;

    public Car(String carSequence, String carCode, String driver, String trainman, String planTime, String intervalTime, String systemEnterTime, String enterTime, String state) {
        this.carSequence = carSequence;
        this.carCode = carCode;
        this.driver = driver;
        this.trainman = trainman;
        this.planTime = planTime;
        this.intervalTime = intervalTime;
        this.systemEnterTime = systemEnterTime;
        this.enterTime = enterTime;
        this.state = state;
    }
}
