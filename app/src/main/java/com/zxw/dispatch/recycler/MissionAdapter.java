package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zxw.data.bean.MissionType;
import com.zxw.dispatch.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 */
public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.MissionHolder> {
    private final OnSelectMissionListener listener;
    private List<MissionType.TaskContentBean> mData;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    private boolean isFirst = true;
    private boolean isClick[];

    public MissionAdapter(List<MissionType.TaskContentBean> mData, Context mContext, OnSelectMissionListener listener) {
        this.mData = mData;
        this.mContext = mContext;
        this.listener = listener;
        mLayoutInflater = LayoutInflater.from(mContext);
        isClick = new boolean[mData.size()];
        for (int i = 0; i < isClick.length; i++){
            isClick[i] = false;
        }
    }


    @Override
    public MissionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.item_mission_type, parent, false);
        return new MissionHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final MissionHolder holder, final int position) {
        holder.llMissionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onSelectMission(mData.get(position));
               choice(position);
            }
        });
        holder.rbMissionType.setChecked(isClick[position]);
        holder.tv_mission_type.setText(mData.get(position).getTaskName());
    }

    public void choice(int poi){
        for (int i = 0; i < isClick.length; i++){
            isClick[i] = false;
            if (i == poi){
                isClick[i] = true;
            }
        }
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MissionHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rb_mission_type)
        RadioButton rbMissionType;
        @Bind(R.id.ll_mission_type)
        LinearLayout llMissionType;
        @Bind(R.id.tv_mission_type)
        TextView tv_mission_type;

        MissionHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnSelectMissionListener {
        void onSelectMission(MissionType.TaskContentBean missionType);
    }
}
