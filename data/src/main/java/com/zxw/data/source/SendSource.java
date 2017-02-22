package com.zxw.data.source;

import com.zxw.data.bean.SendHistory;
import com.zxw.data.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 15:15
 * email：cangjie2016@gmail.com
 */
public class SendSource {
    String carCode[] = new String[]{"11122", "33221", "99511", "51232","00052","55125", "66434", "66666", "10000"};
    String driver[] = new String[]{"李白", "王维", "杜甫", "赵信","吕布","诸葛亮", "刘备", "张飞", "盖伦"};
    String time[] = new String[]{"1100", "0600", "0610", "0630", "0620", "0634", "0644", "0655", "0710","0810", "0712", "0730","0750"};
    String state[] = new String[]{"已读","未读"};
    String sendState[] = new String[]{"手动","自动"};

    public void loadSend(Subscriber<List<SendHistory>> subscriber, String userId, String keyCode, int lineId){
        HttpMethods.getInstance().getScheduleHistory(subscriber, userId, keyCode, lineId);
    }
}
