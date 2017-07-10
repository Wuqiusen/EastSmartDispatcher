package com.zxw.dispatch.recycler.viewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.zxw.data.bean.SchedulePlanBean;
import com.zxw.dispatch.R;

/**
 * Created by Thinkpad on 2017/7/8.
 */

public class SchedulePlanViewHolder extends BaseViewHolder<SchedulePlanBean> {
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
    
    
    public SchedulePlanViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.item_plan);
        ll_container1 = $(R.id.ll_container1);
        ll_container2 = $(R.id.ll_container2);
        ll_container3 = $(R.id.ll_container3);
        ll_container4 = $(R.id.ll_container4);
        ll_container5 = $(R.id.ll_container5);

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

    @Override
    public void setData(SchedulePlanBean data) {
        super.setData(data);
        clearViewState();
        switch (data.getSingle()){
            case 1:
                tv_single_class_name_1.setText("单班");
                displayType1(data);
                break;
            case 2:
                tv_single_class_name_1.setText("双班");
                displayType2(data);
                break;
            case 3:
                tv_single_class_name_1.setText("三班");
                displayType3(data);
                break;
            case 4:
                tv_single_class_name_1.setText("四班");
                displayType4(data);
                break;
            case 5:
                tv_single_class_name_1.setText("五班");
                displayType5(data);
                break;
        }
    }

    private void clearViewState() {
        ll_container2.setVisibility(View.GONE);
        ll_container3.setVisibility(View.GONE);
        ll_container4.setVisibility(View.GONE);
        ll_container5.setVisibility(View.GONE);
    }

    private void displayType1(SchedulePlanBean schedulePlanBean) {
        tv_no_1.setText(String.valueOf(schedulePlanBean.getSortNum()));
        tv_send_time_1.setText(String.valueOf(schedulePlanBean.getTime()));
        tv_veh_code_1.setText(String.valueOf(schedulePlanBean.getVehicleName()));

        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(0);
        tv_duty_time_1.setText(dataBean.getWorkTime());
        tv_driver_name_1.setText(dataBean.getDriverName());
        tv_steward_name_1.setText(dataBean.getStewardName());
        tv_run_num_1.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        tv_change_veh_code_1.setText(dataBean.getVehicleNameTwo());
        tv_change_veh_num_1.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
    }

    private void displayType2(SchedulePlanBean schedulePlanBean) {
        ll_container2.setVisibility(View.VISIBLE);
        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(1);
        tv_duty_time_2.setText(dataBean.getWorkTime());
        tv_driver_name_2.setText(dataBean.getDriverName());
        tv_steward_name_2.setText(dataBean.getStewardName());
        tv_run_num_2.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        tv_change_veh_code_2.setText(dataBean.getVehicleNameTwo());
        tv_change_veh_num_2.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
        displayType1(schedulePlanBean);
    }

    private void displayType3(SchedulePlanBean schedulePlanBean) {
        ll_container3.setVisibility(View.VISIBLE);
        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(2);
        tv_duty_time_3.setText(dataBean.getWorkTime());
        tv_driver_name_3.setText(dataBean.getDriverName());
        tv_steward_name_3.setText(dataBean.getStewardName());
        tv_run_num_3.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        tv_change_veh_code_3.setText(dataBean.getVehicleNameTwo());
        tv_change_veh_num_3.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
        displayType2(schedulePlanBean);
    }

    private void displayType4(SchedulePlanBean schedulePlanBean) {
        ll_container4.setVisibility(View.VISIBLE);
        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(3);
        tv_duty_time_4.setText(dataBean.getWorkTime());
        tv_driver_name_4.setText(dataBean.getDriverName());
        tv_steward_name_4.setText(dataBean.getStewardName());
        tv_run_num_4.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        tv_change_veh_code_4.setText(dataBean.getVehicleNameTwo());
        tv_change_veh_num_4.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
        displayType3(schedulePlanBean);
    }

    private void displayType5(SchedulePlanBean schedulePlanBean) {
        ll_container5.setVisibility(View.VISIBLE);
        SchedulePlanBean.DataBean dataBean = schedulePlanBean.getData().get(4);
        tv_duty_time_5.setText(dataBean.getWorkTime());
        tv_driver_name_5.setText(dataBean.getDriverName());
        tv_steward_name_5.setText(dataBean.getStewardName());
        tv_run_num_5.setText(String.valueOf(dataBean.getRunNumReal()) + "/" + String.valueOf(dataBean.getRunNum()));
        tv_change_veh_code_5.setText(dataBean.getVehicleNameTwo());
        tv_change_veh_num_5.setText(String.valueOf(dataBean.getRunNumRealTwo()) + "/" + String.valueOf(dataBean.getRunNumTwo()));
        displayType4(schedulePlanBean);
    }
}
