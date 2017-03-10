package com.zxw.dispatch.recycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxw.data.bean.SpotBean;
import com.zxw.dispatch.R;
import com.zxw.dispatch.ui.MainActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2017/2/21 14:56
 * email：cangjie2016@gmail.com
 */
public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<SpotBean> mData;
    private final Context mContext;
    private Activity mActivity;

    public SpotAdapter(List<SpotBean> mData, Activity activity) {
        this.mData = mData;
        this.mActivity = activity;
        this.mContext =  mActivity;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public SpotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_spot, parent, false);
        return new SpotAdapter.SpotHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SpotHolder holder, final int position) {
        holder.tv_spot_name.setText(mData.get(position).getName());
        holder.tv_spot_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("spotId", mData.get(position).getSpotId());
                mContext.startActivity(intent);
                mActivity.finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class SpotHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_spot_name)
        TextView tv_spot_name;
        public SpotHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
