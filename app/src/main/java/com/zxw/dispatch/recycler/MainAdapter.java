package com.zxw.dispatch.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.Line;
import com.zxw.dispatch.R;
import com.zxw.dispatch.ui.DepartActivity;
import com.zxw.dispatch.utils.ToastHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.LineHolder> {
    private List<Line> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;

    public MainAdapter(List<Line> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_main, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        holder.mNumber.setText((position +1 )+"");
        holder.mLineNumber.setText(mData.get(position).lineCode);
        final List<Line.LineStation> lineStationList = mData.get(position).lineStationList;
        if (lineStationList == null || lineStationList.size() == 0)
            return;
        holder.mFirstStation.setText(lineStationList.get(0).stationName);
        holder.mFirstStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DepartActivity.class);
                intent.putExtra("station", lineStationList.get(0));
                intent.putExtra("lineId", mData.get(position).id);
                intent.putExtra("lineName", mData.get(position).lineCode);
                mContext.startActivity(intent);
            }
        });
        if (lineStationList.size() == 2){
            holder.mSecondStation.setText(lineStationList.get(1).stationName);
            holder.mSecondStation.setVisibility(View.VISIBLE);
            holder.mSecondStation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DepartActivity.class);
                    intent.putExtra("station", lineStationList.get(1));
                    intent.putExtra("lineId", mData.get(position).id);
                    intent.putExtra("lineName", mData.get(position).lineCode);
                    mContext.startActivity(intent);
                }
            });
        }else{
            holder.mSecondStation.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class LineHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_no)
        TextView mNumber;
        @Bind(R.id.tv_line_no)
        TextView mLineNumber;
        @Bind(R.id.tv_first_station)
        TextView mFirstStation;
        @Bind(R.id.tv_second_station)
        TextView mSecondStation;
        @Bind(R.id.container)
        LinearLayout mContainer;

        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
