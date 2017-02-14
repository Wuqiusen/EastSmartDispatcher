package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.Line;
import com.zxw.dispatch.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.LineHolder> {
    private final OnSelectLineListener listener;
    private List<Line> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private boolean isFirst = true;
    private boolean isClick[];

    public MainAdapter(List<Line> mData, Context mContext,OnSelectLineListener listener) {
        this.mData = mData;
        this.mContext = mContext;
        this.listener = listener;
        mLayoutInflater = LayoutInflater.from(mContext);
        isClick = new boolean[mData.size()];
    }


    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_main, parent, false);
        return new LineHolder(inflate);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        if (position == 0 && isFirst){
            setClickView(0);
            listener.onSelectLine(mData.get(position));
        }
        holder.mLineNumber.setText(mData.get(position).lineCode);
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectLine(mData.get(position));
                isFirst = false;
                setClickView(position);
                notifyDataSetChanged();
            }
        });
        if (isClick[position]){
            holder.mTip.setVisibility(View.VISIBLE);
        }else {
            holder.mTip.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class LineHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_line_no)
        TextView mLineNumber;
        @Bind(R.id.container)
        LinearLayout mContainer;
        @Bind(R.id.view_main_tip)
        View mTip;

        LineHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void setClickView(int poi){
        for (int i = 0; i < mData.size(); i++){
            if (poi == i){
                isClick[i] = true;
            }else {
                isClick[i] = false;
            }
        }
    }

    public interface OnSelectLineListener{
        void onSelectLine(Line line);
    }
}
