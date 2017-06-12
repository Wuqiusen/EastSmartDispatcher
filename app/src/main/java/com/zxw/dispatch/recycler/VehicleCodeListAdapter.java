package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.RunningCarBean;
import com.zxw.dispatch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moxiaoqing on 2017/6/9.
 */
public class VehicleCodeListAdapter extends RecyclerView.Adapter<VehicleCodeListAdapter.ViewHolder>{

    private Context mContext;
    private List<RunningCarBean> mData = new ArrayList<>();
    private OnItemClickListener mListener;
    private LayoutInflater mLayoutInflater;
    private ViewHolder mViewHolder;

    public VehicleCodeListAdapter(Context context, List<RunningCarBean> data,OnItemClickListener listener){
        this.mContext = context;
        this.mData = data;
        this.mListener = listener;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.item_vehicle_code_list,parent,false);
        mViewHolder = new ViewHolder(rootView);
        mViewHolder.tv = (TextView) rootView.findViewById(R.id.tv_vehicleCode);
        mViewHolder.ll = (LinearLayout) rootView.findViewById(R.id.ll_vehicle_code_detail);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv.setText(mData.get(position).getVehicleCode());
        mViewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(mData.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener{
        void onClick(RunningCarBean bean,int position);
    }
}
