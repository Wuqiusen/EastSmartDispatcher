package com.zxw.dispatch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.dispatch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：${MXQ} on 2017/2/15 16:14
 * 邮箱：1299242483@qq.com
 */
public class PopupAdapter extends BaseAdapter{

    private Context mContext;
    private List<String> mTitleList = new ArrayList<>();

    private final OnPopupWindowListener mListener;

    public PopupAdapter(Context context,List<String> titles,OnPopupWindowListener listener) {
           mContext = context;
           mTitleList = titles;
           mListener = listener;
    }

    @Override
    public View getView(final int position, View rootView, ViewGroup parent) {
        ViewHolder holder = null;
        if (rootView == null) {
            holder = new ViewHolder();
            rootView = LayoutInflater.from(mContext).inflate(R.layout.item_pupupwindow,null);
            holder.ll_setting = (LinearLayout) rootView.findViewById(R.id.ll_setting);
            holder.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            rootView.setTag(holder);
        }else{
            holder = (ViewHolder) rootView.getTag();
        }

        holder.tv_name.setText(mTitleList.get(position));
        holder.ll_setting.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (position == 0) {
                       // 修改资料
                       mListener.onPopupListener(0);
                 }else if(position == 1) {
                       // 密码修改
                       mListener.onPopupListener(1);
                 }else{
                       // 退出
                       mListener.onPopupListener(2);
                 }
             }
         });
        return rootView;
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder{
          LinearLayout ll_setting;
          TextView tv_name;
    }


    public interface OnPopupWindowListener{
        void onPopupListener(int position);
    }
}
