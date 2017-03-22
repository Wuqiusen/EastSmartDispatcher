package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.NonMissionType;
import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class NonMissionTypeAdapter extends RecyclerView.Adapter<NonMissionTypeAdapter.LineHolder> {
    private List<NonMissionType> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private String date;

    public NonMissionTypeAdapter(List<NonMissionType> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        date = formatter.format(curDate);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_non_mission_type, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        holder.tv_time.setText(date);
        holder.tv_content.setText(mData.get(position).getTaskTypeName());
        if (mData.get(position).getStatus() == 0){
            holder.tv_status.setText("未完成");
        }else {
            holder.tv_status.setText("已完成");
        }

    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class LineHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_time)
        TextView tv_time;
        @Bind(R.id.tv_content)
        TextView tv_content;
        @Bind(R.id.tv_status)
        TextView tv_status;

        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
