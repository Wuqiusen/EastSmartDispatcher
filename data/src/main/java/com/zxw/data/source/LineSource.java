package com.zxw.data.source;

import com.zxw.data.bean.Line;
import com.zxw.data.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/20 17:37
 * email：cangjie2016@gmail.com
 */
public class LineSource {
    public void loadLine(Subscriber<List<Line>> subscriber, String code, String keyCode, int spotId, int pageNo, int pageSize) {
        HttpMethods.getInstance().lines(subscriber, code, keyCode, spotId, pageNo, pageSize);
    }
}
