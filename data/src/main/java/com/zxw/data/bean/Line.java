package com.zxw.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author：CangJie on 2016/10/12 16:14
 * email：cangjie2016@gmail.com
 */
public class Line {
    public int id;
    public String lineCode;
    public List<LineStation> lineStationList;

    public class LineStation implements Serializable{
        public int stationId;
        public String stationName;

        @Override
        public String toString() {
            return "LineStation{" +
                    "stationId=" + stationId +
                    ", stationName='" + stationName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", lineCode='" + lineCode + '\'' +
                ", lineStationList=" + lineStationList +
                '}';
    }
}
