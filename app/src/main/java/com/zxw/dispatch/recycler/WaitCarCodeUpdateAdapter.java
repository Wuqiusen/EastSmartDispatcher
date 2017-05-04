package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.StopCarCodeBean;
import com.zxw.dispatch.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author MXQ
 * create at 2017/5/2 09:56
 * email: 1299242483@qq.com
 */
public class WaitCarCodeUpdateAdapter extends RecyclerView.Adapter<WaitCarCodeUpdateAdapter.CarCodeHolder>{

    private Context mContext;
    private List<StopCarCodeBean> mData;
    private final LayoutInflater mLayoutInflater;
    private OnListener mListener;
    private SparseBooleanArray selectLists = new SparseBooleanArray();

    public WaitCarCodeUpdateAdapter(Context context,List<StopCarCodeBean> data,OnListener listener){
        this.mContext = context;
        this.mData = data;
        mLayoutInflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @Override
    public CarCodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_update_wait_car_code,parent,false);
        return new CarCodeHolder(view);
    }

    @Override
    public void onBindViewHolder(CarCodeHolder holder, final int position) {
        holder.tvWaitCarCode.setText(mData.get(position).code);
        if (!selectLists.get(position)){
            holder.llWaitCarCode.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else{
            holder.llWaitCarCode.setBackgroundColor(mContext.getResources().getColor(R.color.background_deep_blue));
        }
        holder.llWaitCarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    setSelectLists(position);
                    mListener.onUpdateWaitCarCode(mData.get(position));

                    notifyDataSetChanged();

                }
            }
        });

    }

    private void setSelectLists(int selectPosition){
        for (int i = 0;i < mData.size();i++){
            if (i == selectPosition){
                selectLists.put(i,true);
            }else{
                selectLists.put(i,false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  class CarCodeHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ll_update_car_code)
        LinearLayout llWaitCarCode;
        @Bind(R.id.tv_update_car_code)
        TextView tvWaitCarCode;

        public CarCodeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    public interface OnListener{
        void onUpdateWaitCarCode(StopCarCodeBean bean);
    }
}
