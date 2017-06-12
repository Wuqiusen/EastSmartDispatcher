package com.zxw.dispatch.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zxw.data.bean.DepartCar;
import com.zxw.data.bean.InformDataBean;
import com.zxw.data.bean.LineParams;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.utils.DisplayTimeUtil;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.TimePlanPickerDialog;
import com.zxw.dispatch.view.dialog.AlertNameDialog;
import com.zxw.dispatch.view.smart_edittext.SmartEditText;

import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * author：CangJie on 2016/9/21 11:12
 * email：cangjie2016@gmail.com
 *
 */
public class DragListAdapterForOperatorEmpty extends BaseAdapter {
    private List<DepartCar> mDatas;

    private Context mContext;
    private  TextView tv_interval_time;
    private  TextView tv_plan_start_time, tv_plan_end_time, tv_station_name, tv_count, tv_empty_km;
    private MainPresenter presenter;
    private LineParams mLineParams;
    private Calendar mDate;
    private int mHour,mMinute;
    private String sHour = null;
    private String sMinute = null;
    private SmartEditText seLine;
    private int mLineId;
    private Dialog informDialog;
    private EditText etInformContent;
    private InformDataAdapter informDataAdapter;
    private String informContent;
    private String typeId;
    private String vehicleId;
    private Spinner sp_inform;
    private Button btn_confirm;
    private Button btn_cancel;
    private View viewDialog;
    private LinearLayout.LayoutParams params;

    public DragListAdapterForOperatorEmpty(Context context, MainPresenter presenter, List<DepartCar> waitVehicles, LineParams mLineParams, int lineId) {
        this.mContext = context;
        this.mDatas = waitVehicles;
        this.presenter = presenter;
        this.mLineParams = mLineParams;
        this.mLineId = lineId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        /***
         * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
         * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
         */
        view = LayoutInflater.from(mContext).inflate(
                R.layout.item_wait_car_operate_empty_driving, null);

        TextView tv_car_sequence = (TextView) view
                .findViewById(R.id.tv_car_sequence);
        tv_car_sequence.setText((position + 1) + "");

        TextView tv_car_code = (TextView) view
                .findViewById(R.id.tv_car_code);
        tv_car_code.setText(mDatas.get(position).getCode());
        if (mDatas.get(position).getTaskEditBelongId() == mDatas.get(position).getTaskEditRunId()){
            tv_car_code.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_red_btn_bg));
        }else {
            tv_car_code.setBackground(mContext.getResources().getDrawable(R.drawable.ll_stop_car_green_btn_bg));
        }

        // 驾驶员
        TextView tv_driver = (TextView) view
                .findViewById(R.id.tv_driver);
        tv_driver.setText(mDatas.get(position).getDriverName());
        tv_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertNameDialog alertNameDialog = new AlertNameDialog(mContext,mDatas.get(position).getIsDouble());
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
                    AlertNameDialog alertNameDialog = new AlertNameDialog(mContext,mDatas.get(position).getIsDouble());
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

        // 计划发车时刻
        tv_plan_start_time = (TextView) view
                .findViewById(R.id.tv_plan_start_time);
        tv_plan_start_time.setText(DisplayTimeUtil.substring(mDatas.get(position).getVehTime()));
        tv_plan_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePlanPickerDialog(mContext,mDatas.get(position).getVehTime(),new TimePlanPickerDialog.OnTimePickerListener() {
                    @Override
                    public void onTimePicker(String sHour, String sMinute) {
                        presenter.alertVehTime(mDatas.get(position).getId(), sHour + sMinute);
                    }
                }).show();
            }
        });

        // 计划结束时刻
        tv_plan_end_time = (TextView) view
                .findViewById(R.id.tv_plan_end_time);
        tv_plan_end_time.setText(DisplayTimeUtil.substring(mDatas.get(position).getTaskEndTime()));

        // 电子围栏名称
        tv_station_name = (TextView) view
                .findViewById(R.id.tv_station_name);
        tv_station_name.setText(mDatas.get(position).getElectronRailName());
        // 折算单次
        tv_count = (TextView) view
                .findViewById(R.id.tv_count);
        tv_count.setText(String.valueOf(mDatas.get(position).getRunNum()));
        // 空驶里程
        tv_empty_km = (TextView) view
                .findViewById(R.id.tv_empty_km);
        tv_empty_km.setText(String.valueOf(mDatas.get(position).getRunEmpMileage()));


        // 到站时刻
        TextView tv_system_enter_time = (TextView) view
                .findViewById(R.id.tv_system_enter_time);
        tv_system_enter_time.setText(DisplayTimeUtil.substring(mDatas.get(position).getArriveTime()));


        // 任务名称
        TextView tv_no_operation_task = (TextView) view.findViewById(R.id.tv_no_operation_task);
        tv_no_operation_task.setText(mDatas.get(position).getTypeName());

        // 备注
        TextView tv_empty_remarks = (TextView) view.findViewById(R.id.tv_empty_remarks);
        tv_empty_remarks.setText(mDatas.get(position).getRemarks());
        tv_empty_remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mDatas.get(position).getRemarks())){
                    ToastHelper.showToast("当前没有备注");
                    return;
                }
                openEmptyRemarksDialog(position);
            }
        });


        // 发车
        TextView tv_send_car = (TextView) view
                .findViewById(R.id.tv_send_car);

        tv_send_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tv_plan_start_time.getText().toString())){
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

        // 通知
        TextView tv_inform = (TextView) view.findViewById(R.id.tv_inform);
        if (mDatas.get(position).getIsNotice() == 1){
            tv_inform.setEnabled(true);
            tv_inform.setTextColor(mContext.getResources().getColor(R.color.font_blue2));
        }else if(mDatas.get(position).getIsNotice() == 2){
            tv_inform.setEnabled(false);
            tv_inform.setTextColor(mContext.getResources().getColor(R.color.font_gray));
        }
        tv_inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInformDialog(mDatas.get(position).getId() + "",mDatas.get(position).getDriverCode());
            }
        });

        return view;
    }

    //备注
    private void openEmptyRemarksDialog(final int position) {
        final Dialog sDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.dialog_wait_car_remarks_dialog,null);
        final TextView tv_empty_remarks = (TextView) view.findViewById(R.id.tv_empty_remarks);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_empty_remarks.setText(mDatas.get(position).getRemarks());
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

    private String noOperationStatus(int status){
        if (status == 1)
            return "无";
        else if (status == 2)
            return "已完成";
        else
            return "未完成";
    }


    private void queryLine(final int poistion){
        final Dialog sDialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.dialog_query_line,null);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        seLine = (SmartEditText) view.findViewById(R.id.se_line);
        seLine.addQueryLineEditTextListener(mLineId + "");
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.lineSupport(mDatas.get(poistion).getId(), seLine.getLineInfo().lineId);
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
     * 通知
     */
    private void openInformDialog(final String objId,final String driverCode){
        HttpMethods.getInstance().getInformData(new Subscriber<List<InformDataBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<InformDataBean> informDataBeen) {
                if (informDialog == null){
                    informDialog = new Dialog(mContext,R.style.customDialog);
                    params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    viewDialog = View.inflate(mContext,R.layout.dialog_inform,null);
                    sp_inform = (Spinner) viewDialog.findViewById(R.id.sp_inform);
                    etInformContent = (EditText) viewDialog.findViewById(R.id.et_remarks);
                    btn_confirm = (Button) viewDialog.findViewById(R.id.btn_confirm);
                    btn_cancel = (Button) viewDialog.findViewById(R.id.btn_cancel);
                }
                informDataAdapter = new InformDataAdapter(mContext, informDataBeen, objId);
                sp_inform.setAdapter(informDataAdapter);
                informDataAdapter.setOnItemClick(new InformDataAdapter.SetOnItemClick() {
                    @Override
                    public void itemClick(String content, String typeId, String vehicleId) {
                        etInformContent.setText(content);
                        etInformContent.setSelection(content.length());
                        DragListAdapterForOperatorEmpty.this.typeId = typeId;
                        DragListAdapterForOperatorEmpty.this.vehicleId = vehicleId;
                    }
                });

                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.confirmInform(vehicleId, etInformContent.getText().toString(), typeId, driverCode);// driverCode
                        informDialog.dismiss();
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        informDialog.dismiss();
                    }
                });
                informDialog.setContentView(viewDialog,params);
                informDialog.setCancelable(true);
                informDialog.show();

            }
        }, SpUtils.getCache(mContext, SpUtils.USER_ID), SpUtils.getCache(mContext, SpUtils.KEYCODE));

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
