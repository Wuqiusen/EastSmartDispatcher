package com.zxw.dispatch.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.LineParams;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.utils.DisplayTimeUtil;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.TimePlanPickerDialog;
import com.zxw.dispatch.view.dialog.AlertNameDialog;

import java.util.Calendar;
import java.util.List;

/**
 * author：CangJie on 2016/9/21 11:12
 * email：cangjie2016@gmail.com
 */
public class DragListAdapter extends BaseAdapter {
    private List<DepartCar> mDatas;

    private Context mContext;
    private  TextView tv_interval_time;
    private MainPresenter presenter;
    private LineParams mLineParams;
    private Calendar mDate;
    private int mHour,mMinute;
    private String sHour = null;
    private String sMinute = null;

    public DragListAdapter(Context context, MainPresenter presenter, List<DepartCar> waitVehicles, LineParams mLineParams) {
        this.mContext = context;
        this.mDatas = waitVehicles;
        this.presenter = presenter;
        this.mLineParams = mLineParams;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        /***
         * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
         * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
         */
        view = LayoutInflater.from(mContext).inflate(
                R.layout.item_car, null);

        TextView tv_car_sequence = (TextView) view
                .findViewById(R.id.tv_car_sequence);
        tv_car_sequence.setText((position + 1) + "");

        TextView tv_car_code = (TextView) view
                .findViewById(R.id.tv_car_code);
        tv_car_code.setText(mDatas.get(position).getCode());

        TextView tv_driver = (TextView) view
                .findViewById(R.id.tv_driver);
        tv_driver.setText(mDatas.get(position).getDriverName());
        tv_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertNameDialog alertNameDialog = new AlertNameDialog(mContext);
                alertNameDialog.showDriverDialog(mDatas.get(position).getId(), mDatas.get(position).getDriverName(), new AlertNameDialog.OnAlertDriverListener() {
                    @Override
                    public void onAlertDriverListener(int driverId) {
                        presenter.alertPeople(mDatas.get(position).getId(), driverId, AlertNameDialog.TYPE_DRIVER);
                    }
                });
            }
        });

        TextView tv_trainman = (TextView) view
                .findViewById(R.id.tv_trainman);
        if (mLineParams.getSaleType() == MainPresenter.TYPE_SALE_AUTO){
            tv_trainman.setVisibility(View.GONE);
        }else if(mLineParams.getSaleType() == MainPresenter.TYPE_SALE_MANUAL){
            tv_trainman.setVisibility(View.VISIBLE);
            tv_trainman.setText(mDatas.get(position).getStewardName());
            tv_trainman.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertNameDialog alertNameDialog = new AlertNameDialog(mContext);
                    alertNameDialog.showStewardDialog(mDatas.get(position).getId(), mDatas.get(position).getStewardName(), new AlertNameDialog.OnAlertStewardListener() {
                        @Override
                        public void onAlertStewardListener(int stewardId) {
                            presenter.alertPeople(mDatas.get(position).getId(), stewardId, AlertNameDialog.TYPE_DRIVER);
                        }
                    });
                }
            });
        }

        // 计划时刻
        TextView tv_plan_time = (TextView) view
                .findViewById(R.id.tv_plan_time);
        //tv_plan_time.setText(DisplayTimeUtil.substring(mDatas.get(position).get));
        tv_plan_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePlanPickerDialog(mContext, new TimePlanPickerDialog.OnTimePickerListener() {
                    @Override
                    public void onTimePicker(String sHour, String sMinute) {
                             ToastHelper.showToast(sHour+":"+sMinute,mContext);

                    }
                }).show();
            }
        });
        // 发车间隔
        tv_interval_time = (TextView) view
                .findViewById(R.id.tv_interval_time);
        tv_interval_time.setText(String.valueOf(mDatas.get(position).getSpaceTime()));
        tv_interval_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     openCarIntervalDialog();
            }
        });
        // 到站时刻
        TextView tv_system_enter_time = (TextView) view
                .findViewById(R.id.tv_system_enter_time);
        tv_system_enter_time.setText(DisplayTimeUtil.substring(mDatas.get(position).getArriveTime()));

        // 任务类型
        TextView tv_task_type = (TextView) view.findViewById(R.id.tv_task_type);
        tv_task_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTaskTypeDialog();
            }
        });

        // 非营运任务
        TextView tv_no_operation_task = (TextView) view.findViewById(R.id.tv_no_operation_task);
        tv_no_operation_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNoOperationTaskDialog();
            }
        });

        // 发车
        TextView tv_send_car = (TextView) view
                .findViewById(R.id.tv_send_car);
        tv_send_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tv_interval_time.getText().toString())){
                    ToastHelper.showToast("请先填写发车时间");
                    return;
                }
                openConfirmCarDialog(position);
            }
        });
        // 支援
        TextView tv_support = (TextView) view.findViewById(R.id.tv_support);
        tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openSupportCarDialog();
            }
        });


//        TextView tv_enter_time = (TextView) view
//                .findViewById(R.lineId.tv_enter_time);
//        tv_enter_time.setText(DisplayTimeUtil.substring(mDatas.get(position).inTime2));

//        TextView tv_state = (TextView) view
//                .findViewById(R.lineId.tv_state);
//        tv_state.setText(mDatas.get(position).isScan == 1 ? "已读" : "未读");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new SendCarDialog(mContext).change(mDatas, position, new SendCarDialog.OnDialogChangeConfirmListener() {
//                    @Override
//                    public void onDialogChangeConfirm(int opId, int vehId, int sjId, String scId, String projectTime, int spaceMin, String inTime2) {
//                        presenter.updateVehicle(opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
//                    }
//                }).dialogShow();
            }
        });

        return view;
    }





    /**
     * 发车间隔
     */
    private void openCarIntervalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext,R.layout.view_update_car_interval_dialog,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        builder.setView(view);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        final AlertDialog sDialog = builder.show();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sDialog.dismiss();
            }
        });
    }

    /**
     * 任务类型
     */
    private void openTaskTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext,R.layout.view_task_type_dialog,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        builder.setView(view);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        final AlertDialog tDialog = builder.show();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tDialog.dismiss();
            }
        });
    }

    /**
     * 非营运任务
     */
    private void openNoOperationTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext,R.layout.view_no_operation_task_dialog,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        builder.setView(view);
        Button btn_close = (Button) view.findViewById(R.id.btn_close);
        final AlertDialog eDialog = builder.show();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eDialog.dismiss();
            }
        });

    }

    /**
     * 发车
     * @param position
     */
    private void openConfirmCarDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext,R.layout.view_message_confirm_dialog,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        builder.setView(view);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        final AlertDialog nDialog = builder.show();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确定发车
                presenter.sendVehicle(mDatas.get(position).getId());
                nDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nDialog.dismiss();
            }
        });
    }

    /**
     * 支援
     */
    private void openSupportCarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext,R.layout.view_support_car_dialog,null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        builder.setView(view);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        final AlertDialog sDialog = builder.show();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });
    }

    /***
     * 动态修改ListVIiw的方位.（数据移位）
     *
     * @param start 点击移动的position
     * @param end   松开时候的position
     */
    public void update(int start, int end) {
        DepartCar startVehicle = mDatas.get(start);
        DepartCar endVehicle = mDatas.get(end);
        presenter.sortVehicle(startVehicle.getId(), endVehicle.getId());
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}