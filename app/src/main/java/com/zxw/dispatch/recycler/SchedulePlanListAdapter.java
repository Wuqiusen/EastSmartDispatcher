package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.DoubleBill;
import com.zxw.data.bean.SchedulePlanBean;
import com.zxw.data.bean.SingleBill;
import com.zxw.dispatch.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * author MXQ
 * create at 2017/4/12 11:14
 * email: 1299242483@qq.com
 */
public class SchedulePlanListAdapter extends RecyclerView.Adapter<SchedulePlanListAdapter.ScheduleHolder> {

    private Context mContext;
    private SingleBill singleBill;
    private DoubleBill doubleBill;
    private List<SchedulePlanBean> mData;
    private final LayoutInflater mLayoutInflater;


    public SchedulePlanListAdapter(Context context, List<SchedulePlanBean> data){
        this.mContext = context;
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_view_scheduling_plan,parent,false);
        return new ScheduleHolder(view);
    }


    @Override
    public void onBindViewHolder(ScheduleHolder holder, int position) {
          singleBill = mData.get(position).singleBill;
          doubleBill = mData.get(position).doubleBill;
          holder.tvNo.setText(String.valueOf(mData.get(position).sortNum));
          holder.tvVehCode.setText(mData.get(position).vehCode);
          String driverName = singleBill.getDriverName()+"/"
                                   + String.valueOf(mData.get(position).getVehId());
          holder.tvDriverName.setText(driverName);
          holder.tvRunNum.setText(String.valueOf(singleBill.getRunNum()));
          // 单双班
          if (mData.get(position).isDouble == 0) {
             //单班
              holder.tvClassName.setText("单班");
              holder.llDualClass.setVisibility(View.GONE);
              holder.viewLine.setVisibility(View.GONE);
          }else{
             //双班
              holder.tvClassName.setText("双班");
              holder.llDualClass.setVisibility(View.VISIBLE);
              holder.viewLine.setVisibility(View.VISIBLE);
              holder.tvDualDriverName.setText(doubleBill.getDriverName()+"/"
                                              + String.valueOf(doubleBill.getVehId()));
              holder.tvDualRunNum.setText(String.valueOf(doubleBill.getRunNum()));
          }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ScheduleHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_no)
        TextView tvNo;
        @Bind(R.id.view_line)
        View viewLine;
        //单班
        @Bind(R.id.tv_veh_code)
        TextView tvVehCode;
        @Bind(R.id.tv_single_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_driver_name)
        TextView tvDriverName;
        @Bind(R.id.tv_steward_name)
        TextView tvStewardName;
        @Bind(R.id.tv_run_num)
        TextView tvRunNum;
        //双班
        @Bind(R.id.ll_dual_class)
        LinearLayout llDualClass;
        @Bind(R.id.tv_dual_driver_name)
        TextView tvDualDriverName;
        @Bind(R.id.tv_dul_steward_name)
        TextView tvDulStewardName;
        @Bind(R.id.tv_dual_run_num)
        TextView tvDualRunNum;

        public ScheduleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
