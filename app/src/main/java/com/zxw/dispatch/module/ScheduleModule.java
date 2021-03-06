package com.zxw.dispatch.module;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.LineParams;
import com.zxw.data.bean.SchedulePlanBean;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.LogUtil;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.recycler.viewHolder.SchedulePlanViewHolder;
import com.zxw.dispatch.utils.CreateRecyclerView;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;

import java.util.Calendar;
import java.util.List;

import cn.qqtheme.framework.picker.DatePicker;
import rx.Subscriber;

/**
 * author：CangJie on 2017/5/17 16:11
 * email：cangjie2016@gmail.com
 */
public class ScheduleModule extends LinearLayout implements RecyclerArrayAdapter.OnLoadMoreListener {

    private final Context mContext;
    TextView tv_date;
    EasyRecyclerView mScheduleRV;
    private int currentYear, currentMonth, currentDay;

    private int lineId;
    private LineParams mLineParams;
    private OnLoadingListener listener;

    private RecyclerArrayAdapter mArrayAdapter;
    private int pageNo = 1;
    private final int pageSize = 15;
    private int allDataSize = 0;

    public ScheduleModule(Context context) {
        this(context, null);
    }

    public ScheduleModule(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tab_view_scheduling_plan, this);
        this.mContext  = context;

        // init
        tv_date = (TextView) findViewById(R.id.tv_date);
        mScheduleRV = (EasyRecyclerView) findViewById(R.id.rv_scheduling_plan);

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        tv_date.setText(String.valueOf(currentYear) + "-" + String.valueOf(currentMonth) + "-" + String.valueOf(currentDay));
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(currentYear, currentMonth, currentDay);
            }
        });
        mArrayAdapter = new RecyclerArrayAdapter(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new SchedulePlanViewHolder(parent);
            }
        };
        mScheduleRV.setProgressView(R.layout.view_progress);
        mScheduleRV.setEmptyView(R.layout.view_empty);
        new CreateRecyclerView().CreateRecyclerView(mContext, mScheduleRV, mArrayAdapter, this);
//        mScheduleRV.setLayoutManager(new LinearLayoutManager(mContext));
//        mScheduleRV.addItemDecoration(new DividerItemDecoration(mContext,
//                DividerItemDecoration.VERTICAL_LIST));
    }

    public void initLineParams(int lineId, LineParams params) {
        this.lineId = lineId;
        this.mLineParams = params;
        loadSchedulePlan();
    }

    private void showDatePickerDialog(int currentYear, int currentMonth, int currentDay) {
        if (listener == null)
            throw new RuntimeException("loading listener can not be null");
        DatePicker picker = new DatePicker((Activity) mContext, DatePicker.YEAR_MONTH_DAY);
        picker.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));
        picker.setHeight((int) (picker.getScreenHeightPixels() * 0.5));
        picker.setRangeStart(2017, 1, 1);
        picker.setRangeEnd(2116, 1, 1);
        picker.setSelectedItem(currentYear, currentMonth, currentDay);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tv_date.setText(year + "-" + Integer.valueOf(month) + "-" + Integer.valueOf(day));
                ScheduleModule.this.currentYear = Integer.valueOf(year);
                ScheduleModule.this.currentMonth = Integer.valueOf(month);
                ScheduleModule.this.currentDay = Integer.valueOf(day);
                if (mArrayAdapter != null) mArrayAdapter.clear();
                loadSchedulePlan();
            }
        });
        picker.show();
    }

    public void loadSchedulePlan(){
        loadSchedulePlan(String.valueOf(currentYear), String.valueOf(currentMonth), String.valueOf(currentDay));
    }


    private void  loadSchedulePlan(String strYear, String strMonth, String strDay) {
        if(lineId == -1 || mLineParams == null ){
            ToastHelper.showToast("线路参数缺失，请重试！");
            return;
        }
        if (strMonth.length() == 1){
            strMonth = "0"+strMonth;
        }
        if (strDay.length() == 1){
            strDay = "0" + strDay;
        }
        if (strYear.length() != 4)
            throw new RuntimeException("year error");
        if (strMonth.length() != 2)
            throw new RuntimeException("month error");
        if (strDay.length() != 2)
            throw new RuntimeException("day error");
        String runDate = strYear + strMonth + strDay;
        String userId = SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);
        HttpMethods.getInstance().schedulePlan(new Subscriber<BaseBean<List<SchedulePlanBean>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                listener.disPlay(e.getMessage());
                LogUtil.loadRemoteError("schedulePlan " + e.getMessage());
            }

            @Override
            public void onNext(BaseBean<List<SchedulePlanBean>> baseBean) {
                allDataSize = baseBean.returnSize;
                mArrayAdapter.clear();
                if (baseBean.returnData != null && !baseBean.returnData.isEmpty()) {
                    mArrayAdapter.addAll(baseBean.returnData);
//                    SchedulePlanListAdapter mAdapter = new SchedulePlanListAdapter(mContext, mLineParams, schedulePlanBeen);
//                    loadSchedulePlanList(mAdapter);
                    listener.showLoading();
                } else {
                    mArrayAdapter.addAll(baseBean.returnData);
                    listener.disPlay("排班计划暂时无数据");
//                    mScheduleRV.setAdapter(null);
                }

            }
        }, userId, keyCode, lineId, runDate, pageNo, pageSize);

    }

    public void setOnLoadingListener(OnLoadingListener listener){
        this.listener = listener;
    }

    @Override
    public void onLoadMore() {
        if (pageNo * pageSize >= allDataSize) {
            if (mArrayAdapter != null)
                mArrayAdapter.stopMore();
        } else {
            pageNo++;
            loadSchedulePlan();
        }

    }

    public interface OnLoadingListener{
        void showLoading();
        void hideLoading();
        void disPlay(String str);
    }

}
