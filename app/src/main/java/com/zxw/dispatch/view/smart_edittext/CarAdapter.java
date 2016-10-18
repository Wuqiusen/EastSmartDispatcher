package com.zxw.dispatch.view.smart_edittext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxw.data.bean.Vehcile;
import com.zxw.dispatch.R;

import java.util.List;

/**
 * author：CangJie on 2016/10/14 15:52
 * email：cangjie2016@gmail.com
 */
public class CarAdapter extends BaseAdapter{

        private List<Vehcile> mData;
    private OnSelectItemListener listener;
    private Context mContext;

        public CarAdapter(List<Vehcile> mData, OnSelectItemListener listener, Context context){
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
        holder.tv.setText(mData.get(position).vehicleCode);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectItemListener(mData.get(position));
            }
        });
        return convertView;
    }

        public interface OnSelectItemListener{
        void onSelectItemListener(Vehcile vehcile);
    }

    public class ViewHolder{
        TextView tv;
    }
}

//public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarHolder> {
//    private List<Car> mData;
//    private OnSelectItemListener listener;
//
//    public CarAdapter(List<Car> mData, OnSelectItemListener listener){
//        this.mData = mData;
//        this.listener = listener;
//    }
//    @Override
//    public CarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        TextView textView = new TextView(mContext);
//        return new CarHolder(textView);
//    }
//
//    @Override
//    public void onBindViewHolder(CarHolder holder, final int position) {
//        holder.displayView.setText(mData.get(position).vehicleCode);
//        holder.displayView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onSelectItemListener(mData.get(position));
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//    public class CarHolder extends RecyclerView.ViewHolder {
//
//        public TextView displayView;
//        public CarHolder(View itemView) {
//            super(itemView);
//            displayView = (TextView) itemView;
//        }
//    }
//
//    public interface OnSelectItemListener{
//        void onSelectItemListener(Car car);
//    }
//}