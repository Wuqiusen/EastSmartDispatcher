package com.zxw.data.source;

import com.zxw.data.bean.MoreHistory;
import com.zxw.data.http.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 17:37
 * email：cangjie2016@gmail.com
 */
public class MoreSource {
    String carCode[] = new String[]{"11122", "33221", "99511", "51232","00052","55125", "66434", "66666", "10000"};
    String driver[] = new String[]{"李白", "王维", "杜甫", "赵信","吕布","诸葛亮", "刘备", "张飞", "盖伦"};
    String time[] = new String[]{"1100", "0600", "0610", "0630", "0620", "0634", "0644", "0655", "0710","0810", "0712", "0730","0750"};
    String state[] = new String[]{"已读","未读"};
    String moreType[] = new String[]{"加油","加电", "其他任务", "事故", "紧急维修"};
    String moreDetail[] = new String[]{"加油","加电", "其他任务", "事故", "紧急维修"};

    public void loadMore(Subscriber<List<MoreHistory>> subscriber, String code, int lineId, int stationId, String keyCode, int pageNo, int pageSize){
        HttpMethods.getInstance().moreHistory(subscriber, code, lineId, stationId, keyCode, pageNo, pageSize);
    }
}
