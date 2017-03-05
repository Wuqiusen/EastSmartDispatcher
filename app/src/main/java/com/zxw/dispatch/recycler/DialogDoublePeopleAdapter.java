package com.zxw.dispatch.recycler;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxw.data.bean.PersonInfo;
import com.zxw.dispatch.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2017/2/21 14:56
 * email：cangjie2016@gmail.com
 */
public class DialogDoublePeopleAdapter extends RecyclerView.Adapter<DialogDoublePeopleAdapter.SpotHolder> {

    private final LayoutInflater mLayoutInflater;
    private final OnSelectedDoublePeopleListener mListener;
    private List<PersonInfo> mData;
    private final Context mContext;
    private int currentSelectedPosition = -1;

    public DialogDoublePeopleAdapter(List<PersonInfo> mData, Context mContext, OnSelectedDoublePeopleListener listener) {
        this.mData = mData;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mListener = listener;
    }

    @Override
    public SpotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_people, parent, false);
        return new DialogDoublePeopleAdapter.SpotHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SpotHolder holder, final int position) {
        holder.tv_people_name.setText(mData.get(position).personName);
        if (currentSelectedPosition == position){
            holder.tv_people_name.setBackgroundColor(Color.RED);
        }else{
            holder.tv_people_name.setBackgroundColor(Color.WHITE);
        }
        holder.tv_people_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectedPosition = position;
                notifyDataSetChanged();
                mListener.onSelectedDoublePeopleListener(mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class SpotHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_people_name)
        TextView tv_people_name;
        public SpotHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSelectedDoublePeopleListener{
        void onSelectedDoublePeopleListener(PersonInfo info);
    }
}
