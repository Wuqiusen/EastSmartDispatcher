package com.zxw.dispatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zxw.dispatch.R;

import java.util.List;

/**
 * Created by huson on 2016/5/25.
 * 940762301@qq.com
 */
public class MySpinnerAdapter extends BaseAdapter {
    private List<String> mlist;
    private Context mContext;

    public MySpinnerAdapter(Context context, List<String> list){
        mContext = context;
        mlist = list;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //找到布局信息
        LayoutInflater layout=LayoutInflater.from(mContext);
        convertView = layout.inflate(R.layout.item_spinner, null);
        if(convertView != null)
        {
            //将文本内容填充到 item.xml中的文本显示框中
            TextView one=(TextView)convertView.findViewById(R.id.tv_item_spinner);
            one.setText(mlist.get(position));

        }
        return convertView;
    }
}

