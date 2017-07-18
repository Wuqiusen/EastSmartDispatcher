package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.VehicleNumberBean;
import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.DebugLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author：CangJie on 2016/9/21 09:53
 * email：cangjie2016@gmail.com
 *
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.LineHolder> {
    private final OnSelectLineListener listener;
    private List<Line> mData;
    private List<VehicleNumberBean> sendCarNum;
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
        String lineName = "";
        if (mData.get(position).lineCode.contains("上行")){
            lineName = mData.get(position).lineCode.replace("上行", "\n上行");
        }else if (mData.get(position).lineCode.contains("下行")){
            lineName = mData.get(position).lineCode.replace("下行", "\n下行");
        }else {
            lineName = mData.get(position).lineCode;
        }
        holder.mLineNumber.setText(lineName);
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
//            holder.mTip.setVisibility(View.VISIBLE);
              holder.mContainer.setBackgroundColor(mContext.getResources().getColor(R.color.background_bg_blue));

        }else {
//            holder.mTip.setVisibility(View.GONE);
            holder.mContainer.setBackgroundColor(mContext.getResources().getColor(R.color.background_deep_blue));

        }
        String vehCount = "";
        if (sendCarNum != null && !sendCarNum.isEmpty()) {
           for (VehicleNumberBean bean: sendCarNum){
               if (TextUtils.equals(bean.lineId, mData.get(position).lineId + "")){
                   if (bean.vehNumber != null){
                       vehCount = bean.vehNumber + "辆";
                       DebugLog.e("main代发车辆数:lineId" + bean.lineId + "count:" + bean.vehNumber);
                   }else{
                       DebugLog.e("main代发车辆数:为空");
                       vehCount = 0 + "辆";
                   }
               }
           }

        }
        holder.tv_time_to_send_count.setText(vehCount);
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
//      @Bind(R.id.view_main_tip)
//      View mTip;
        @Bind(R.id.tv_time_to_send_count)
        TextView tv_time_to_send_count;

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



    public void setSendCarNum(List<VehicleNumberBean> sendCarNum){
        this.sendCarNum = sendCarNum;
        isFirst = false;
        notifyDataSetChanged();
    }
}
