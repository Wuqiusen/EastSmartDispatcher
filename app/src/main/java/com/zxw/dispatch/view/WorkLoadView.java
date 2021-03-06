package com.zxw.dispatch.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zxw.data.bean.BaseBean;
import com.zxw.data.bean.DriverWorkloadItem;
import com.zxw.data.bean.LineParams;
import com.zxw.data.http.HttpMethods;
import com.zxw.data.utils.LogUtil;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.WorkLoadVerifyViewHolder;
import com.zxw.dispatch.presenter.BasePresenter;
import com.zxw.dispatch.utils.Base64;
import com.zxw.dispatch.utils.CreateRecyclerView;
import com.zxw.dispatch.utils.DESPlus;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.view.recycle.LoadMoreAdapterWrapper;

import java.util.Calendar;
import java.util.List;

import rx.Subscriber;

/**
 * Created by moxiaoqing on 2017/5/19.
 */

public class WorkLoadView extends LinearLayout implements View.OnClickListener, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private EditText etCurrentDate, etDriverName, etVehId;
    private TextView tvStartTime, tvEndTime, tvGps, tvDriverOk, tvReport, tvAll;
    private EasyRecyclerView rvWorkLoad;
    private OnListener mListener;
    private final static int LOAD_PAGE_SIZE = 20;
    private int mPageSize;

    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int lineId;
    private LineParams mLineParams;
    private List<DriverWorkloadItem> mDatas;
    private boolean isLoadMore = false;
    private int mCurrentPage;
    private String mVehCode, mDriverName;
    private RecyclerArrayAdapter arrayAdapter;
    public LoadMoreAdapterWrapper.ILoadCallback mLoadCallback;
    private String mExceptionType;
    private final static int EXCEPTION_TYPE_OUT_TIME = 1, EXCEPTION_TYPE_ARRIVE_TIME = 2, EXCEPTION_TYPE_GPS = 3, EXCEPTION_TYPE_DRIVER_OPERATOR = 4;

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!isChinese(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public WorkLoadView(Context context, OnListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_work_load_verify, this);
        initData();
        initView();
        loadData();
    }


    private void loadData() {
        mCurrentPage = 1;
        loadWorkloadList(mVehCode, mDriverName);
    }

    private void deleteWorkload(long objId, final BasePresenter.LoadDataStatus loadDataStatus) {
        mListener.showLoading();
        String userId = SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);
        HttpMethods.getInstance().deleteWorkload(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
                loadDataStatus.OnLoadDataFinish();
                mListener.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                ToastHelper.showToast(e.getMessage());
                LogUtil.loadRemoteError("deleteWorkload " + e.getMessage());
            }

            @Override
            public void onNext(BaseBean o) {
                ToastHelper.showToast(o.returnInfo);
                loadData();
            }
        }, userId, keyCode, objId);
    }

    // 初始化线路信息的时候  加载该线路的第一页数据
    public void initLineParams(int lineId, LineParams params) {
        this.lineId = lineId;
        this.mLineParams = params;
        loadData();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {
        etCurrentDate = (EditText) findViewById(R.id.et_current_date); // 日期
        etDriverName = (EditText) findViewById(R.id.et_driver_name); //驾驶员
        etVehId = (EditText) findViewById(R.id.et_vehId); //车牌号
        etDriverName.setFilters(new InputFilter[]{filter});
        tvReport = (TextView) findViewById(R.id.tv_report); //上报

        tvGps = (TextView) findViewById(R.id.tv_gps);
        tvStartTime = (TextView) findViewById(R.id.tv_veh_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);
        tvDriverOk = (TextView) findViewById(R.id.tv_driver_ok);
        tvAll = (TextView) findViewById(R.id.tv_all);

        etCurrentDate.setText(String.valueOf(currentYear) + "-" + disPlayNum(currentMonth) + "-" + disPlayNum(currentDay));
        etCurrentDate.setOnClickListener(this);
        etDriverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDriverName = s.toString();
                loadData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etVehId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mVehCode = s.toString();
                loadData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tvReport.setOnClickListener(this);
        tvGps.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvDriverOk.setOnClickListener(this);
        tvAll.setOnClickListener(this);

        initRecyclerView();

    }

    private void initRecyclerView() {
        rvWorkLoad = (EasyRecyclerView) findViewById(R.id.rv_work_load_verify);
        arrayAdapter = new RecyclerArrayAdapter(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new WorkLoadVerifyViewHolder(parent, new WorkLoadVerifyViewHolder.OnWorkLoadItemClickListener() {
                    @Override
                    public void onAlertOutTime(long objId, String str, BasePresenter.LoadDataStatus loadDataStatus) {
                        updateWorkload(objId, str, null, null, null, loadDataStatus);
                    }

                    @Override
                    public void onAlertArriveTime(long objId, String str, BasePresenter.LoadDataStatus loadDataStatus) {
                        updateWorkload(objId, null, str, null, null, loadDataStatus);

                    }

                    @Override
                    public void onAlertGpsStatus(long objId, int str, BasePresenter.LoadDataStatus loadDataStatus) {
                        updateWorkload(objId, null, null, String.valueOf(str), null, loadDataStatus);

                    }

                    @Override
                    public void onAlertDriverStatus(long objId, int str, BasePresenter.LoadDataStatus loadDataStatus) {
                        updateWorkload(objId, null, null, null, String.valueOf(str), loadDataStatus);

                    }

                    @Override
                    public void onDelete(long objId, BasePresenter.LoadDataStatus loadDataStatus) {
                        deleteWorkload(objId, loadDataStatus);
                    }
                });
            }

        };
        rvWorkLoad.setEmptyView(R.layout.view_empty);
        rvWorkLoad.setProgressView(R.layout.view_progress);
        new CreateRecyclerView().CreateRecyclerView(mContext, rvWorkLoad, arrayAdapter, this);
        rvWorkLoad.setRefreshListener(this);
    }

    private void loadWorkloadList(String vehCode, String driverName) {
        String userId = SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);
        try {
            if (!TextUtils.isEmpty(vehCode)) {
                vehCode = new DESPlus().encrypt(Base64.encode(vehCode.getBytes("utf-8")));
            }
            if (!TextUtils.isEmpty(driverName)) {
                driverName = new DESPlus().encrypt(Base64.encode(driverName.getBytes("utf-8")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            vehCode = null;
            driverName = null;
        }
        HttpMethods.getInstance().workloadList(new Subscriber<BaseBean<List<DriverWorkloadItem>>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastHelper.showToast(e.getMessage());
                LogUtil.loadRemoteError("workloadList " + e.getMessage());
            }

            @Override
            public void onNext(BaseBean<List<DriverWorkloadItem>> basedriverWorkloadItems) {
                if (mCurrentPage == 1) arrayAdapter.clear();
                arrayAdapter.addAll(basedriverWorkloadItems.returnData);
                mPageSize = basedriverWorkloadItems.returnSize;

            }
        }, userId, keyCode, lineId, mCurrentPage, LOAD_PAGE_SIZE, vehCode, driverName, mExceptionType);
    }

    public void updateWorkload(long objId, String outTime, String arrivalTime, String gpsStatus,
                               String opStatus, final BasePresenter.LoadDataStatus loadDataStatus) {
        String userId = SpUtils.getCache(MyApplication.mContext, SpUtils.USER_ID);
        String keyCode = SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);
        HttpMethods.getInstance().updateWorkload(new Subscriber() {
            @Override
            public void onCompleted() {
                loadDataStatus.OnLoadDataFinish();

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.loadRemoteError("updateWorkload " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                loadData();
            }
        }, userId, keyCode, objId, outTime, arrivalTime, gpsStatus, opStatus);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_current_date:
                openCalendarDialog();
                break;
            case R.id.tv_report:
                mListener.onClickToReport();
                break;
            case R.id.tv_gps:
                updateTabBackground(0);
                filterWorkload(EXCEPTION_TYPE_GPS);
                break;
            case R.id.tv_veh_time:
                updateTabBackground(1);
                filterWorkload(EXCEPTION_TYPE_OUT_TIME);
                break;
            case R.id.tv_end_time:
                updateTabBackground(2);
                filterWorkload(EXCEPTION_TYPE_ARRIVE_TIME);
                break;
            case R.id.tv_driver_ok:
                updateTabBackground(3);
                filterWorkload(EXCEPTION_TYPE_DRIVER_OPERATOR);
                break;
            case R.id.tv_all:
                updateTabBackground(4);
                filterWorkload();
                break;
        }
    }

    private void filterWorkload() {
        if (TextUtils.isEmpty(mExceptionType))
            return;
        mExceptionType = null;
        loadData();
    }

    private void filterWorkload(int exceptionType) {
        String tempType = String.valueOf(exceptionType);
        if (!tempType.equals(mExceptionType)) {
            mExceptionType = tempType;
        }
        loadData();
    }

    private void updateTabBackground(int tag) {
        setWorkLoadNormalTabStyle();
        switch (tag) {
            case 0:
                tvGps.setTextColor(mContext.getResources().getColor(R.color.font_white));
                tvGps.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 1:
                tvStartTime.setTextColor(mContext.getResources().getColor(R.color.font_white));
                tvStartTime.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 2:
                tvEndTime.setTextColor(mContext.getResources().getColor(R.color.font_white));
                tvEndTime.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 3:
                tvDriverOk.setTextColor(mContext.getResources().getColor(R.color.font_white));
                tvDriverOk.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
            case 4:
                tvAll.setTextColor(mContext.getResources().getColor(R.color.font_white));
                tvAll.setBackground(mContext.getResources().getDrawable(R.drawable.btn_login_style));
                break;
        }
    }

    private void setWorkLoadNormalTabStyle() {
        tvGps.setTextColor(mContext.getResources().getColor(R.color.font_black1));
        tvStartTime.setTextColor(mContext.getResources().getColor(R.color.font_black1));
        tvEndTime.setTextColor(mContext.getResources().getColor(R.color.font_black1));
        tvDriverOk.setTextColor(mContext.getResources().getColor(R.color.font_black1));
        tvAll.setTextColor(mContext.getResources().getColor(R.color.font_black1));

        tvGps.setBackground(mContext.getResources().getDrawable(R.drawable.btn_white_style));
        tvStartTime.setBackground(mContext.getResources().getDrawable(R.drawable.btn_white_style));
        tvEndTime.setBackground(mContext.getResources().getDrawable(R.drawable.btn_white_style));
        tvDriverOk.setBackground(mContext.getResources().getDrawable(R.drawable.btn_white_style));
        tvAll.setBackground(mContext.getResources().getDrawable(R.drawable.btn_white_style));
    }

    public void openCalendarDialog() {
        cn.qqtheme.framework.picker.DatePicker picker = new cn.qqtheme.framework.picker.DatePicker((Activity) mContext, cn.qqtheme.framework.picker.DatePicker.YEAR_MONTH_DAY);
        picker.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));
        picker.setHeight((int) (picker.getScreenHeightPixels() * 0.5));
        picker.setRangeStart(2017, 1, 1);
        picker.setRangeEnd(2116, 1, 1);
        picker.setSelectedItem(currentYear, currentMonth, currentDay);
        picker.setOnDatePickListener(new cn.qqtheme.framework.picker.DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                etCurrentDate.setText(year + "-" + disPlayNum(Integer.valueOf(month)) + "-" + disPlayNum(Integer.valueOf(day)));
                String date = year + disPlayNum(Integer.valueOf(month)) + disPlayNum(Integer.valueOf(day));
                mListener.onClickCalendar(date);
            }
        });
        picker.show();
    }

    public String disPlayNum(int num) {
        return num < 10 ? "0" + num : "" + num;
    }

    @Override
    public void onLoadMore() {
        if (mCurrentPage * LOAD_PAGE_SIZE >= mPageSize) {
            arrayAdapter.stopMore();
        } else {
            mCurrentPage++;
            loadWorkloadList(mVehCode, mDriverName);
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    public interface OnListener {
        void onClickCalendar(String date);

        void onClickToReport();

        void onEditTextChanged(String str, int type);

        void showLoading();

        void hideLoading();
    }

}
