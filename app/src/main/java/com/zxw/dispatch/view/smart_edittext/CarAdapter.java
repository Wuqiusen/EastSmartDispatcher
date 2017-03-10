package com.zxw.dispatch.view.smart_edittext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxw.data.bean.FuzzyVehicleBean;
import com.zxw.dispatch.R;

import java.util.List;

/**
 * author：CangJie on 2016/10/14 15:52
 * email：cangjie2016@gmail.com
 */
public class CarAdapter extends BaseAdapter{

        private List<FuzzyVehicleBean> mData;
    private OnSelectItemListener listener;
    private Context mContext;

        public CarAdapter(List<FuzzyVehicleBean> mData, OnSelectItemListener listener, Context context){
        this.mData = mData;
        this.listener = listener;
            this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.smart_textview, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(mData.get(position).getCode());
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectItemListener(mData.get(position));
            }
        });
        return convertView;
    }

        public interface OnSelectItemListener{
        void onSelectItemListener(FuzzyVehicleBean vehicle);
    }

    public class ViewHolder{
        TextView tv;
    }
}
