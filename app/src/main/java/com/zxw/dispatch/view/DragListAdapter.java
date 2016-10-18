package com.zxw.dispatch.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zxw.data.bean.WaitVehicle;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.DepartPresenter;
import com.zxw.dispatch.utils.DisplayTimeUtil;

import java.util.List;

/**
 * author：CangJie on 2016/9/21 11:12
 * email：cangjie2016@gmail.com
 */
public class DragListAdapter extends BaseAdapter {
    private List<WaitVehicle> mDatas;

    private Context mContext;
    private DepartPresenter presenter;

    public DragListAdapter(Context context, DepartPresenter presenter, List<WaitVehicle> arrayTitles) {
        this.mContext = context;
        this.mDatas = arrayTitles;
        this.presenter = presenter;
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
        tv_car_code.setText(mDatas.get(position).vehCode);

        TextView tv_driver = (TextView) view
                .findViewById(R.id.tv_driver);
        tv_driver.setText(mDatas.get(position).sjName);

        TextView tv_trainman = (TextView) view
                .findViewById(R.id.tv_trainman);
        tv_trainman.setText(mDatas.get(position).scName);

        TextView tv_plan_time = (TextView) view
                .findViewById(R.id.tv_plan_time);
        tv_plan_time.setText(DisplayTimeUtil.substring(mDatas.get(position).projectTime));

        TextView tv_interval_time = (TextView) view
                .findViewById(R.id.tv_interval_time);
        tv_interval_time.setText(mDatas.get(position).spaceMin);

        TextView tv_system_enter_time = (TextView) view
                .findViewById(R.id.tv_system_enter_time);
        tv_system_enter_time.setText(DisplayTimeUtil.substring(mDatas.get(position).inTime1));

        TextView tv_enter_time = (TextView) view
                .findViewById(R.id.tv_enter_time);
        tv_enter_time.setText(DisplayTimeUtil.substring(mDatas.get(position).inTime2));

        TextView tv_state = (TextView) view
                .findViewById(R.id.tv_state);
        tv_state.setText(mDatas.get(position).isScan == 1 ? "已读" : "未读");

        Button moreBtn = (Button) view.findViewById(R.id.more);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendCarDialog(mContext).change(mDatas, position, new SendCarDialog.OnDialogChangeConfirmListener() {
                    @Override
                    public void onDialogChangeConfirm(int opId, int vehId, int sjId, String scId, String projectTime, int spaceMin, String inTime2) {
                        presenter.updateVehicle(opId, vehId, sjId, scId, projectTime, spaceMin, inTime2);
                    }
                }).dialogShow();
            }
        });
        Button sendBtn = (Button) view.findViewById(R.id.start);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendVehicle(mDatas.get(position).id);
            }
        });
        return view;
    }

    /***
     * 动态修改ListVIiw的方位.（数据移位）
     *
     * @param start 点击移动的position
     * @param end   松开时候的position
     */
    public void update(int start, int end) {
        WaitVehicle startVehicle = mDatas.get(start);
        WaitVehicle endVehicle = mDatas.get(end);
        presenter.sortVehicle(startVehicle.id, endVehicle.sortNum);
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