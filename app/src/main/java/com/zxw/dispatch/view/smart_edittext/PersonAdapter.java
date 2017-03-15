package com.zxw.dispatch.view.smart_edittext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.PersonInfo;
import com.zxw.dispatch.R;

import java.util.List;

/**
 * author：CangJie on 2016/10/14 15:52
 * email：cangjie2016@gmail.com
 */
public class PersonAdapter extends BaseAdapter{

    private List<PersonInfo> mData;
    private OnSelectItemListener listener;
    private Context mContext;

    public PersonAdapter(List<PersonInfo> mData, OnSelectItemListener listener, Context context){
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
            holder.ll_tv = (LinearLayout) convertView.findViewById(R.id.ll_tv);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            holder.tv_code.setVisibility(View.GONE);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(mData.get(position).personName);
//      holder.tv_code.setText(mData.get(position).personId+"");

        holder.ll_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectItemListener(mData.get(position));
            }
        });
        return convertView;
    }

        public interface OnSelectItemListener{
        void onSelectItemListener(PersonInfo vehcile);
    }

    public class ViewHolder{
        LinearLayout ll_tv;
        TextView tv;
        TextView tv_code;
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