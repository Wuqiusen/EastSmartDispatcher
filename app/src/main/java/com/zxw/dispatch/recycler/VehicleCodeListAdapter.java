
package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.RunningCarBean;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zxw.dispatch.view.LineRunMapView.mSelectedVehicleCode;

/**
 * Created by moxiaoqing on 2017/6/9.
 */
public class VehicleCodeListAdapter extends RecyclerView.Adapter<VehicleCodeListAdapter.ViewHolder> {

    private Context mContext;
    private List<RunningCarBean> mData = new ArrayList<>();
    private OnRunItemClickListener mListener;
    private LayoutInflater mLayoutInflater;
    private ViewHolder mViewHolder;

    public VehicleCodeListAdapter(Context context, List<RunningCarBean> data, OnRunItemClickListener listener) {
        this.mContext = context;
        this.mData = data;
        this.mListener = listener;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.item_vehicle_code_list, parent, false);
        mViewHolder = new ViewHolder(rootView);
        mViewHolder.ll = (LinearLayout) rootView.findViewById(R.id.ll_vehicle_code_detail);
        mViewHolder.imgLine = (ImageView) rootView.findViewById(R.id.img_line);
        mViewHolder.tv = (TextView) rootView.findViewById(R.id.tv_vehicleCode);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String vehicleCode = mData.get(position).getVehicleCode();
        long mit = mData.get(position).getMilliMinute();
        String type = mData.get(position).getType();
        String[] strTime = new String[]{};
        strTime = TimeUtil.shiftMilliSecond(mit);
        if (strTime != null && strTime.length > 0 && !TextUtils.isEmpty(type)) {
            String hour = null;
            String minute = null;
            String second = null;
            if (strTime[0].equals("00")){
                hour = "";
            }else {
                hour = strTime[0]+"时";
            }
            if (strTime[1].equals("00")){
                minute = "";
            }else {
                minute = strTime[1]+"分";
            }
            if (strTime[2].equals("00")){
                second = "";
            }else {
                String sec = Integer.valueOf(strTime[2]) < 10 ?  strTime[2].substring(0,1): strTime[2];
//                sec = Integer.valueOf(sec) == 0 ? "" : sec+"秒";
                second = sec+"秒";
            }

            if (type.equals("1")) {
                if (!TextUtils.isEmpty(mSelectedVehicleCode) &&  mSelectedVehicleCode.equals(vehicleCode)){
                    holder.ll.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_blue));
                    holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.font_white));
                    holder.tv.setTextColor(mContext.getResources().getColor(R.color.font_white));
                }else {
                    holder.ll.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.font_black));
                    holder.tv.setTextColor(mContext.getResources().getColor(R.color.font_black));
                }
                holder.tvTime.setText("静止 " + hour + minute + second);
                holder.imgLine.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_vehicle_code_gray));

            } else if (type.equals("2")) {
                if (!TextUtils.isEmpty(mSelectedVehicleCode) &&  mSelectedVehicleCode.equals(vehicleCode)){
                    holder.ll.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_blue));
                    holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.font_white));
                    holder.tv.setTextColor(mContext.getResources().getColor(R.color.font_white));
                }else {
                    holder.ll.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.font_green));
                    holder.tv.setTextColor(mContext.getResources().getColor(R.color.font_black));
                }
                holder.tvTime.setText(hour + minute + second);
                holder.imgLine.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_vehicle_code_green));
            }
        }

        holder.tv.setText(mData.get(position).getVehicleCode());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    String vehicleCode = mData.get(position).getVehicleCode();
                    mSelectedVehicleCode = vehicleCode;
                    notifyDataSetChanged();

                    mListener.onItemClick(vehicleCode);
                }

            }
        });
    }


    public void clearListener(){
        mListener = null;
    }
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void add(RunningCarBean item) {
        clear();
        mData.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ll_vehicle_code_detail)
        LinearLayout ll;
        @Bind(R.id.tv_vehicleCode)
        TextView tv;
        @Bind(R.id.tv_car_run_time)
        TextView tvTime;
        @Bind(R.id.img_line)
        ImageView imgLine;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public interface OnRunItemClickListener {
        void onItemClick(String vehicleCode);
    }
}
