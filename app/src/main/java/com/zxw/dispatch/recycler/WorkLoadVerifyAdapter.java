package com.zxw.dispatch.recycler;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zxw.data.bean.WorkLoadVerifyBean;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by moxiaoqing on 2017/5/19.
 */

public class WorkLoadVerifyAdapter extends RecyclerView.Adapter<WorkLoadVerifyAdapter.WorkLoadVerifyViewHolder>{
    private Context mContext;
    private List<WorkLoadVerifyBean> mData;
    private MainPresenter mPresenter;
    private OnWorkLoadVerifyListener mListener;
    private final LayoutInflater mLayoutInflater;

    private int mClickRbTag;

    public WorkLoadVerifyAdapter(Context context,MainPresenter presenter, List<WorkLoadVerifyBean> data,OnWorkLoadVerifyListener listener){
         this.mContext = context;
         this.mPresenter = presenter;
         this.mData = data;
         this.mListener = listener;
         mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public WorkLoadVerifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_work_load_verify,parent,false);
        return new WorkLoadVerifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkLoadVerifyViewHolder holder, final int position) {
        final int tag = mData.get(position).driverOk;
        if (tag == 0){
            holder.tvDriverOk.setText("待确认");
        }else if(tag == 1){
            holder.tvDriverOk.setText("不同意");
        }else if(tag == 2){
            holder.tvDriverOk.setText("同意");
        }
        holder.tvNo.setText(mData.get(position).no);
        holder.tvVehId.setText(mData.get(position).vehId);
        holder.tvDriverName.setText(mData.get(position).driverName);
        holder.tvStartTime.setText(mData.get(position).startTime);
        // 后期:正常_打勾、异常_打叉；是否需呀设置监听
        holder.tvStartStation.setText(mData.get(position).startStation);
        holder.tvEndStation.setText(mData.get(position).endStation);
        holder.tvGps.setText(mData.get(position).gps);

        holder.tvDriverOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDriverOkDialog(position,tag);
            }
        });

    }


    private void openDriverOkDialog(final int position,int tag){
        final Dialog dialog = new Dialog(mContext,R.style.customDialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(mContext,R.layout.dialog_work_load_driver_ok,null);
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        final RadioButton rb_wait_ok = (RadioButton) view.findViewById(R.id.rb_wait_ok);
        final RadioButton rb_unagree = (RadioButton) view.findViewById(R.id.rb_unagree);
        final RadioButton rb_agree = (RadioButton) view.findViewById(R.id.rb_agree);
        if (tag == 0){
            setRbsIsChecked(rb_wait_ok,rb_unagree,rb_agree);
        }else if(tag == 1){
            setRbsIsChecked(rb_unagree,rb_wait_ok,rb_agree);
        }else if (tag == 2){
            setRbsIsChecked(rb_agree,rb_wait_ok,rb_unagree);
        }
        mClickRbTag = tag;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
             @Override
             public void onCheckedChanged(RadioGroup group,int checkedId){
                  switch (checkedId){
                      case R.id.rb_wait_ok:
                          mClickRbTag = 0;
                          setRbsIsChecked(rb_wait_ok,rb_unagree,rb_agree);
                          break;
                      case R.id.rb_unagree:
                          mClickRbTag = 1;
                          setRbsIsChecked(rb_unagree,rb_wait_ok,rb_agree);
                          break;
                      case R.id.rb_agree:
                          mClickRbTag = 2;
                          setRbsIsChecked(rb_agree,rb_wait_ok,rb_unagree);
                          break;
                  }
             }
        });
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //司机确认
                //mPresenter.onClickToDriverOk(mClickRbTag,mData.get(position).id);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view,params);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void setRbsIsChecked(RadioButton rb0,RadioButton rb1,RadioButton rb2){
           rb0.setChecked(true);
           rb1.setChecked(false);
           rb2.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class WorkLoadVerifyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_no)
        TextView tvNo;
        @Bind(R.id.tv_vehId)
        TextView tvVehId;
        @Bind(R.id.tv_driver_name)
        TextView tvDriverName;
        @Bind(R.id.tv_start_time)
        TextView tvStartTime;
        @Bind(R.id.tv_start_station)
        TextView tvStartStation;
        @Bind(R.id.tv_end_station)
        TextView tvEndStation;
        @Bind(R.id.tv_gps)
        TextView tvGps;
        @Bind(R.id.tv_driver_ok)
        TextView tvDriverOk;
        public WorkLoadVerifyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnWorkLoadVerifyListener{
            void onClickToDriverOk(int tag,int id);
    }
}
