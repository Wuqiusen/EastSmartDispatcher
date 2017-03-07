package com.zxw.dispatch.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.LineParams;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.utils.DisplayTimeUtil;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.TimePlanPickerDialog;
import com.zxw.dispatch.view.UpdateIntervalPickerDialog;
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
    private  TextView  tv_plan_time;
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

        // 驾驶员
        TextView tv_driver = (TextView) view
                .findViewById(R.id.tv_driver);
        tv_driver.setText(mDatas.get(position).getDriverName());
        tv_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertNameDialog alertNameDialog = new AlertNameDialog(mContext,mDatas.get(position).getIsDouble()); ///
                alertNameDialog.showDriverDialog(mDatas.get(position).getId(), mDatas.get(position).getDriverName(),new AlertNameDialog.OnAlertDriverListener() {
                    @Override
                    public void onAlertDriverListener(int driverId) {

                        presenter.alertPeople(mDatas.get(position).getId(), driverId, AlertNameDialog.TYPE_DRIVER);
                    }
                });
            }
        });

        // 乘务员
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
                    AlertNameDialog alertNameDialog = new AlertNameDialog(mContext,mDatas.get(position).getIsDouble()); ///
                    alertNameDialog.showStewardDialog(mDatas.get(position).getId(), mDatas.get(position).getStewardName(),new AlertNameDialog.OnAlertStewardListener() {
                        @Override
                        public void onAlertStewardListener(int stewardId) {
                            // "确定"监听
                            presenter.alertPeople(mDatas.get(position).getId(), stewardId, AlertNameDialog.TYPE_DRIVER);
                        }
                    });
                }
            });
        }

        // 计划时刻
        tv_plan_time = (TextView) view
                .findViewById(R.id.tv_plan_time);
        tv_plan_time.setText(DisplayTimeUtil.substring(mDatas.get(position).getVehTime()));
        tv_plan_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePlanPickerDialog(mContext,mDatas.get(position).getVehTime(),new TimePlanPickerDialog.OnTimePickerListener() {
                    @Override
                    public void onTimePicker(String sHour, String sMinute) {
                             ToastHelper.showToast(sHour+":"+sMinute,mContext);
                             presenter.alertVehTime(mDatas.get(position).getId(), sHour + sMinute);
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
                final int mCurrentMinute = mDatas.get(position).getSpaceTime();
                new UpdateIntervalPickerDialog(mContext, mCurrentMinute, new UpdateIntervalPickerDialog.OnTimePickerListener() {
                    @Override
                    public void onTimePicker(String sMinute) {
                        presenter.updateSpaceTime(mDatas.get(position).getId(), sMinute);
                          tv_interval_time.setText(sMinute);
                    }
                }).show();
            }
        });
        // 到站时刻
        TextView tv_system_enter_time = (TextView) view
                .findViewById(R.id.tv_system_enter_time);
        tv_system_enter_time.setText(DisplayTimeUtil.substring(mDatas.get(position).getArriveTime()));


        // 非营运任务
        TextView tv_no_operation_task = (TextView) view.findViewById(R.id.tv_no_operation_task);
        tv_no_operation_task.setText(noOperationStatus(mDatas.get(position).getUnRunTaskStatus()));
//      tv_no_operation_task.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openNoOperationTaskDialog();
//            }
//      });

        // 发车
        TextView tv_send_car = (TextView) view
                .findViewById(R.id.tv_send_car);
        TextView tv_is_double = (TextView) view
                .findViewById(R.id.tv_is_double);
        TextView tv_work_type = (TextView) view
                .findViewById(R.id.tv_work_type);
        tv_is_double.setText(mDatas.get(position).getIsDouble() == 0 ?"单班":"双班");
        // 任务类型
        tv_work_type.setText(mDatas.get(position).getTypeName());
        tv_work_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatas.get(position).getTaskId() != null)
                presenter.getMissionList(mDatas.get(position).getId(), mDatas.get(position).getType(), mDatas.get(position).getTaskId());
                else
                    presenter.getMissionList(mDatas.get(position).getId(), mDatas.get(position).getType(),"");
//                openTaskTypeDialog();
            }
        });

        tv_send_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tv_plan_time.getText().toString())){
                    ToastHelper.showToast("请先填写发车时间");
                    return;
                }
                openConfirmCarDialog(position);
            }
        });
        // 撤回
        TextView tv_withdraw = (TextView) view.findViewById(R.id.tv_withdraw);
        tv_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openWithdrawCarDialog(mDatas.get(position).getId());
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

    private String noOperationStatus(int status){
        if (status == 1)
            return "无";
        else if (status == 2)
            return "有";
        else
            return "完成";
    }

    /**
     * 任务类型
     */
    private void openTaskTypeDialog() {
        final Dialog mDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.view_task_type_dialog,null);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(view,params);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * 非营运任务
     */
    private void openNoOperationTaskDialog() {
        final Dialog mDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.view_no_operation_task_dialog,null);
        Button btn_close = (Button) view.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(view,params);
        mDialog.setCancelable(true);
        mDialog.show();

    }

    /**
     * 发车
     * @param position
     */
    private void openConfirmCarDialog(final int position) {
        final Dialog sDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.view_message_confirm_dialog,null);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确定发车
                presenter.sendVehicle(mDatas.get(position).getId());
                sDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });
        sDialog.setContentView(view,params);
        sDialog.setCancelable(true);
        sDialog.show();
    }

    /**
     * 撤回
     */
    private void openWithdrawCarDialog(final int objId) {
        final Dialog mDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.view_withdraw_dialog,null);
        TextView tv_prompt = (TextView) view.findViewById(R.id.tv_prompt);
        tv_prompt.setText("您确定把车辆撤回到停场车辆列表？");
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.callBackScheduleCar(objId);
                mDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setContentView(view,params);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /***
     * 动态修改ListVIiw的方位.（数据移位）
     *
     * @param start 点击移动的position
     * @param end   松开时候的position
     */
    public void update(int start, int end) {
        final DepartCar startVehicle = mDatas.get(start);
        final DepartCar endVehicle = mDatas.get(end);
        final Dialog sDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.view_message_confirm_dialog,null);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        tv_message.setText("确定更换序号为" + (start + 1) + "和" + (end + 1) + "的车辆位置？");
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sortVehicle(startVehicle.getId(), endVehicle.getId());
                sDialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog.dismiss();
            }
        });
        sDialog.setContentView(view,params);
        sDialog.setCancelable(true);
        sDialog.show();
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