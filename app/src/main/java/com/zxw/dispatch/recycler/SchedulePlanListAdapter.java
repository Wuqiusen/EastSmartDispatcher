package com.zxw.dispatch.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.DoubleBill;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.SchedulePlanBean;
import com.zxw.data.bean.SingleBill;
import com.zxw.dispatch.R;

import java.util.List;

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
    private LineParams mLineParams;
    private final LayoutInflater mLayoutInflater;


    public SchedulePlanListAdapter(Context context, LineParams lineParams, List<SchedulePlanBean> data){
        this.mContext = context;
        this.mLineParams = lineParams;
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_plan,parent,false);
        return new ScheduleHolder(view);
    }


    @Override
    public void onBindViewHolder(ScheduleHolder holder, int position) {
        clearViewState(holder);
        switch (mData.get(position).getSingle()){
            case 1:
                holder.tv_single_class_name_1.setText("单班");
                displayType1(holder, mData.get(position));
                break;
            case 2:
                holder.tv_single_class_name_1.setText("双班");
                displayType2(holder, mData.get(position));
                break;
            case 3:
                holder.tv_single_class_name_1.setText("三班");
                displayType3(holder, mData.get(position));
                break;
            case 4:
                holder.tv_single_class_name_1.setText("四班");
                displayType4(holder, mData.get(position));
                break;
            case 5:
                holder.tv_single_class_name_1.setText("五班");
                displayType5(holder, mData.get(position));
                break;
        }
    }

    private void clearViewState(ScheduleHolder holder) {
        holder.ll_container2.setVisibility(View.GONE);
        holder.ll_container3.setVisibility(View.GONE);
        holder.ll_container4.setVisibility(View.GONE);
        holder.ll_container5.setVisibility(View.GONE);
    }

    private void displayType1(ScheduleHolder holder, SchedulePlanBean schedulePlanBean) {
        holder.tv_no_1.setText(String.valueOf(schedulePlanBean.getSortNum()));
        holder.tv_send_time_1.setText(String.valueOf(schedulePlanBean.getTime()));
        holder.tv_veh_code_1.setText(String.valueOf(schedulePlanBean.getVehicleName()));

        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(0);
        holder.tv_duty_time_1.setText(dataBean.getWorkTime());
        holder.tv_driver_name_1.setText(dataBean.getDriverName());
        holder.tv_steward_name_1.setText(dataBean.getStewardName());
        holder.tv_run_num_1.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        holder.tv_change_veh_code_1.setText(dataBean.getVehicleNameTwo());
        holder.tv_change_veh_num_1.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
    }

    private void displayType2(ScheduleHolder holder, SchedulePlanBean schedulePlanBean) {
        holder.ll_container2.setVisibility(View.VISIBLE);
        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(1);
        holder.tv_duty_time_2.setText(dataBean.getWorkTime());
        holder.tv_driver_name_2.setText(dataBean.getDriverName());
        holder.tv_steward_name_2.setText(dataBean.getStewardName());
        holder.tv_run_num_2.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        holder.tv_change_veh_code_2.setText(dataBean.getVehicleNameTwo());
        holder.tv_change_veh_num_2.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
        displayType1(holder, schedulePlanBean);
    }

    private void displayType3(ScheduleHolder holder, SchedulePlanBean schedulePlanBean) {
        holder.ll_container3.setVisibility(View.VISIBLE);
        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(2);
        holder.tv_duty_time_3.setText(dataBean.getWorkTime());
        holder.tv_driver_name_3.setText(dataBean.getDriverName());
        holder.tv_steward_name_3.setText(dataBean.getStewardName());
        holder.tv_run_num_3.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        holder.tv_change_veh_code_3.setText(dataBean.getVehicleNameTwo());
        holder.tv_change_veh_num_3.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
        displayType2(holder, schedulePlanBean);
    }

    private void displayType4(ScheduleHolder holder, SchedulePlanBean schedulePlanBean) {
        holder.ll_container4.setVisibility(View.VISIBLE);
        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(3);
        holder.tv_duty_time_4.setText(dataBean.getWorkTime());
        holder.tv_driver_name_4.setText(dataBean.getDriverName());
        holder.tv_steward_name_4.setText(dataBean.getStewardName());
        holder.tv_run_num_4.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        holder.tv_change_veh_code_4.setText(dataBean.getVehicleNameTwo());
        holder.tv_change_veh_num_4.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
        displayType3(holder, schedulePlanBean);
    }

    private void displayType5(ScheduleHolder holder, SchedulePlanBean schedulePlanBean) {
        holder.ll_container5.setVisibility(View.VISIBLE);
        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(4);
        holder.tv_duty_time_5.setText(dataBean.getWorkTime());
        holder.tv_driver_name_5.setText(dataBean.getDriverName());
        holder.tv_steward_name_5.setText(dataBean.getStewardName());
        holder.tv_run_num_5.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        holder.tv_change_veh_code_5.setText(dataBean.getVehicleNameTwo());
        holder.tv_change_veh_num_5.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
        displayType4(holder, schedulePlanBean);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ScheduleHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_container1;
        LinearLayout ll_container2;
        LinearLayout ll_container3;
        LinearLayout ll_container4;
        LinearLayout ll_container5;
        TextView tv_no_1;
        TextView tv_no_2;
        TextView tv_no_3;
        TextView tv_no_4;
        TextView tv_no_5;

        TextView tv_send_time_1;
        TextView tv_send_time_2;
        TextView tv_send_time_3;
        TextView tv_send_time_4;
        TextView tv_send_time_5;

        TextView tv_veh_code_1;
        TextView tv_veh_code_2;
        TextView tv_veh_code_3;
        TextView tv_veh_code_4;
        TextView tv_veh_code_5;

        TextView tv_single_class_name_1;
        TextView tv_single_class_name_2;
        TextView tv_single_class_name_3;
        TextView tv_single_class_name_4;
        TextView tv_single_class_name_5;

        TextView tv_duty_time_1;
        TextView tv_duty_time_2;
        TextView tv_duty_time_3;
        TextView tv_duty_time_4;
        TextView tv_duty_time_5;

        TextView tv_driver_name_1;
        TextView tv_driver_name_2;
        TextView tv_driver_name_3;
        TextView tv_driver_name_4;
        TextView tv_driver_name_5;

        TextView tv_steward_name_1;
        TextView tv_steward_name_2;
        TextView tv_steward_name_3;
        TextView tv_steward_name_4;
        TextView tv_steward_name_5;

        TextView tv_run_num_1;
        TextView tv_run_num_2;
        TextView tv_run_num_3;
        TextView tv_run_num_4;
        TextView tv_run_num_5;

        TextView tv_change_veh_code_1;
        TextView tv_change_veh_code_2;
        TextView tv_change_veh_code_3;
        TextView tv_change_veh_code_4;
        TextView tv_change_veh_code_5;

        TextView tv_change_veh_num_1;
        TextView tv_change_veh_num_2;
        TextView tv_change_veh_num_3;
        TextView tv_change_veh_num_4;
        TextView tv_change_veh_num_5;

        public ScheduleHolder(View itemView) {
            super(itemView);
            ll_container1 = (LinearLayout) itemView.findViewById(R.id.ll_container1);
            ll_container2 = (LinearLayout) itemView.findViewById(R.id.ll_container2);
            ll_container3 = (LinearLayout) itemView.findViewById(R.id.ll_container3);
            ll_container4 = (LinearLayout) itemView.findViewById(R.id.ll_container4);
            ll_container5 = (LinearLayout) itemView.findViewById(R.id.ll_container5);

            tv_no_1 =  (TextView) ll_container1.findViewById(R.id.tv_no);
            tv_no_2 = (TextView) ll_container2.findViewById(R.id.tv_no);
            tv_no_3 = (TextView) ll_container3.findViewById(R.id.tv_no);
            tv_no_4 = (TextView) ll_container4.findViewById(R.id.tv_no);
            tv_no_5 = (TextView) ll_container5.findViewById(R.id.tv_no);

            tv_send_time_1 = (TextView) ll_container1.findViewById(R.id.tv_send_time);
            tv_send_time_2 = (TextView) ll_container2.findViewById(R.id.tv_send_time);
            tv_send_time_3 = (TextView) ll_container3.findViewById(R.id.tv_send_time);
            tv_send_time_4 = (TextView) ll_container4.findViewById(R.id.tv_send_time);
            tv_send_time_5 = (TextView) ll_container5.findViewById(R.id.tv_send_time);

            tv_veh_code_1 = (TextView) ll_container1.findViewById(R.id.tv_veh_code);
            tv_veh_code_2 = (TextView) ll_container2.findViewById(R.id.tv_veh_code);
            tv_veh_code_3 = (TextView) ll_container3.findViewById(R.id.tv_veh_code);
            tv_veh_code_4 = (TextView) ll_container4.findViewById(R.id.tv_veh_code);
            tv_veh_code_5 = (TextView) ll_container5.findViewById(R.id.tv_veh_code);

            tv_single_class_name_1 =  (TextView) ll_container1.findViewById(R.id.tv_single_class_name);
            tv_single_class_name_2 = (TextView) ll_container2.findViewById(R.id.tv_single_class_name);
            tv_single_class_name_3 = (TextView) ll_container3.findViewById(R.id.tv_single_class_name);
            tv_single_class_name_4 = (TextView) ll_container4.findViewById(R.id.tv_single_class_name);
            tv_single_class_name_5 = (TextView) ll_container5.findViewById(R.id.tv_single_class_name);

            tv_no_2.setVisibility(View.INVISIBLE);
            tv_no_3.setVisibility(View.INVISIBLE);
            tv_no_4.setVisibility(View.INVISIBLE);
            tv_no_5.setVisibility(View.INVISIBLE);

            tv_send_time_2.setVisibility(View.INVISIBLE);
            tv_send_time_3.setVisibility(View.INVISIBLE);
            tv_send_time_4.setVisibility(View.INVISIBLE);
            tv_send_time_5.setVisibility(View.INVISIBLE);

            tv_veh_code_2.setVisibility(View.INVISIBLE);
            tv_veh_code_3.setVisibility(View.INVISIBLE);
            tv_veh_code_4.setVisibility(View.INVISIBLE);
            tv_veh_code_5.setVisibility(View.INVISIBLE);

            tv_single_class_name_2.setVisibility(View.INVISIBLE);
            tv_single_class_name_3.setVisibility(View.INVISIBLE);
            tv_single_class_name_4.setVisibility(View.INVISIBLE);
            tv_single_class_name_5.setVisibility(View.INVISIBLE);

            tv_duty_time_1 = (TextView) ll_container1.findViewById(R.id.tv_duty_time);
            tv_duty_time_2 = (TextView) ll_container2.findViewById(R.id.tv_duty_time);
            tv_duty_time_3 = (TextView) ll_container3.findViewById(R.id.tv_duty_time);
            tv_duty_time_4 = (TextView) ll_container4.findViewById(R.id.tv_duty_time);
            tv_duty_time_5 = (TextView) ll_container5.findViewById(R.id.tv_duty_time);

            tv_driver_name_1 = (TextView) ll_container1.findViewById(R.id.tv_driver_name);
            tv_driver_name_2 = (TextView) ll_container2.findViewById(R.id.tv_driver_name);
            tv_driver_name_3 = (TextView) ll_container3.findViewById(R.id.tv_driver_name);
            tv_driver_name_4 = (TextView) ll_container4.findViewById(R.id.tv_driver_name);
            tv_driver_name_5 = (TextView) ll_container5.findViewById(R.id.tv_driver_name);

            tv_steward_name_1 = (TextView) ll_container1.findViewById(R.id.tv_steward_name);
            tv_steward_name_2 = (TextView) ll_container2.findViewById(R.id.tv_steward_name);
            tv_steward_name_3 = (TextView) ll_container3.findViewById(R.id.tv_steward_name);
            tv_steward_name_4 = (TextView) ll_container4.findViewById(R.id.tv_steward_name);
            tv_steward_name_5 = (TextView) ll_container5.findViewById(R.id.tv_steward_name);

            tv_run_num_1 = (TextView) ll_container1.findViewById(R.id.tv_run_num);
            tv_run_num_2 = (TextView) ll_container2.findViewById(R.id.tv_run_num);
            tv_run_num_3 = (TextView) ll_container3.findViewById(R.id.tv_run_num);
            tv_run_num_4 = (TextView) ll_container4.findViewById(R.id.tv_run_num);
            tv_run_num_5 = (TextView) ll_container5.findViewById(R.id.tv_run_num);

            tv_change_veh_code_1 = (TextView) ll_container1.findViewById(R.id.tv_change_veh_code);
            tv_change_veh_code_2 = (TextView) ll_container2.findViewById(R.id.tv_change_veh_code);
            tv_change_veh_code_3 = (TextView) ll_container3.findViewById(R.id.tv_change_veh_code);
            tv_change_veh_code_4 = (TextView) ll_container4.findViewById(R.id.tv_change_veh_code);
            tv_change_veh_code_5 = (TextView) ll_container5.findViewById(R.id.tv_change_veh_code);

            tv_change_veh_num_1 = (TextView) ll_container1.findViewById(R.id.tv_change_veh_num);
            tv_change_veh_num_2 = (TextView) ll_container2.findViewById(R.id.tv_change_veh_num);
            tv_change_veh_num_3 = (TextView) ll_container3.findViewById(R.id.tv_change_veh_num);
            tv_change_veh_num_4 = (TextView) ll_container4.findViewById(R.id.tv_change_veh_num);
            tv_change_veh_num_5 = (TextView) ll_container5.findViewById(R.id.tv_change_veh_num);


        }
    }
}
