package com.zxw.dispatch.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.ui.WeatherDialogActivity;
import com.zxw.dispatch.utils.AnimationHelper;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public abstract class BaseHeadActivity extends BaseActivity {

    RelativeLayout rel_contentArea;
    ImageButton ibtn_headLeftImageButton;
    ImageButton ibtn_headRightImageButton;
    Button btn_headRightButton;
    Button btn_backButton;
    TextView btn_headTitle;
    RelativeLayout mLoading;
    LinearLayout rel_base_headArea;

    LinearLayout info_bar;
    TextView tv_date;
    TextView tv_line_no;
    TextView tv_user;
    TextView tv_weather;
    RadioGroup rg_depart;
    RadioButton rb_automatic;
    RadioButton rb_manual;


    View mContantArea;
    public final static int WEATHER_RESULT =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_head);
        assignViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        // 判断是否有网络
        mContantArea = getLayoutInflater().inflate(layoutResID, rel_contentArea, false);
        setContentView(mContantArea);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        rel_contentArea.addView(view, params);

    }

    protected void hideHeadArea() {
        rel_base_headArea.setVisibility(View.GONE);
    }


    public void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        AnimationHelper.crossfade(mLoading, rel_contentArea, getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    public void showBackButton() {
        this.showBackButton(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_backButton.setVisibility(View.VISIBLE);
    }

    public void showBackButton(OnClickListener listener) {
        btn_backButton.setOnClickListener(listener);
        btn_backButton.setVisibility(View.VISIBLE);
    }

    public void showTitle(String title) {
        btn_headTitle.setText(title);
    }

    public void hideTitle() {
        btn_headTitle.setVisibility(View.GONE);
    }

    public void showRightImageButton(int iBtnResource) {
        ibtn_headRightImageButton.setImageResource(iBtnResource);
        ibtn_headRightImageButton.setVisibility(View.VISIBLE);
    }

    public void showRightImageButton(int iBtnResource, OnClickListener listener) {
        ibtn_headRightImageButton.setImageResource(iBtnResource);
        ibtn_headRightImageButton.setVisibility(View.VISIBLE);
        ibtn_headRightImageButton.setOnClickListener(listener);
    }

    public boolean checkIsEmpty(String str, String toast) {
        if (TextUtils.isEmpty(str)) {
            ToastHelper.showToast(toast, mContext);
            return true;
        }
        return false;
    }

    public ImageButton getRightImageButton() {
        return ibtn_headRightImageButton;
    }

    protected void showInfoBar(String lineNo){
        showInfoBar(lineNo, "晴");
    }

    protected void showInfoBar(String lineNo , String weather){
        info_bar.setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("日期：yyyy年MM月dd日", Locale.CHINA);
        String day = format.format(new Date());
        tv_date.setText(day);
        lineNo = "线路编号：" + lineNo;
        tv_line_no.setText(lineNo);
        String userName = SpUtils.getCache(this, SpUtils.NAME);
        userName = "调度员：" + userName;
        tv_user.setText(userName);
        weather = "天气：" + weather;
        tv_weather.setText(weather);
        tv_weather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BaseHeadActivity.this, WeatherDialogActivity.class), WEATHER_RESULT);
            }
        });
    }

    protected void showRadioGroup(@NonNull OnClickListener autoListener, @NonNull OnClickListener manualListener){
        rg_depart.setVisibility(View.VISIBLE);
        rb_automatic.setOnClickListener(autoListener);
        rb_manual.setOnClickListener(manualListener);
    }
    private void assignViews() {
        rel_contentArea = (RelativeLayout) findViewById(R.id.rel_base_contentArea);
        ibtn_headLeftImageButton = (ImageButton) findViewById(R.id.btn_base_head_left_imgbutton);
        ibtn_headRightImageButton = (ImageButton) findViewById(R.id.btn_base_head_right_imgbutton);
        btn_backButton = (Button) findViewById(R.id.btn_base_head_back);
        btn_headTitle = (TextView) findViewById(R.id.tv_base_head_title);
        btn_headRightButton = (Button) findViewById(R.id.btn_base_head_right_button);
        mLoading = (RelativeLayout) findViewById(R.id.rel_base_loading);
        rel_base_headArea = (LinearLayout) findViewById(R.id.rel_base_headArea);

        info_bar = (LinearLayout) findViewById(R.id.info_bar);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_line_no = (TextView) findViewById(R.id.tv_line_no);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        rg_depart = (RadioGroup) findViewById(R.id.rg_depart);
        rb_automatic = (RadioButton) findViewById(R.id.rb_automatic);
        rb_manual = (RadioButton) findViewById(R.id.rb_manual);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEATHER_RESULT && resultCode == RESULT_OK){
            tv_weather.setText(data.getStringExtra("weather"));
        }
    }
}
