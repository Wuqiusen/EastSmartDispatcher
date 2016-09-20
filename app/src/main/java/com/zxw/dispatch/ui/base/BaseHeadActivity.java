package com.zxw.dispatch.ui.base;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.AnimationHelper;


public abstract class BaseHeadActivity extends BaseActivity {

    protected RelativeLayout rel_contentArea;
    private ImageButton ibtn_headLeftImageButton;
    private ImageButton ibtn_headRightImageButton;
    private Button btn_headRightButton;
    private Button btn_backButton;
    private TextView btn_headTitle;
    protected RelativeLayout mLoading;
	private RelativeLayout rel_base_headArea;
    private View mContantArea;

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

	protected void hideHeadArea(){
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
        btn_headTitle.setVisibility(View.VISIBLE);
    }
	public void hideTitle(){
		btn_headTitle.setVisibility(View.GONE);
	}

    public void showRightImageButton(OnClickListener listener) {
        ibtn_headRightImageButton.setOnClickListener(listener);
    }
    public void setRightIBtnResource(int iBtnResource){
        ibtn_headRightImageButton.setImageResource(iBtnResource);
        ibtn_headRightImageButton.setVisibility(View.VISIBLE);
    }
    public void setRightIBtnEnable(boolean enable){
        ibtn_headRightImageButton.setEnabled(enable);
    }


    private void assignViews() {
		rel_contentArea = (RelativeLayout) findViewById(R.id.rel_base_contentArea);
		ibtn_headLeftImageButton = (ImageButton) findViewById(R.id.btn_base_head_left_imgbutton);
		ibtn_headRightImageButton = (ImageButton) findViewById(R.id.btn_base_head_right_imgbutton);
		btn_backButton = (Button) findViewById(R.id.btn_base_head_back);
		btn_headTitle = (TextView) findViewById(R.id.tv_base_head_title);
		btn_headRightButton = (Button) findViewById(R.id.btn_base_head_right_button);
		mLoading = (RelativeLayout) findViewById(R.id.rel_base_loading);
		rel_base_headArea = (RelativeLayout) findViewById(R.id.rel_base_headArea);
		ibtn_headLeftImageButton.setVisibility(View.GONE);
		btn_headTitle.setVisibility(View.GONE);
		btn_headRightButton.setVisibility(View.GONE);
        ibtn_headRightImageButton.setVisibility(View.GONE);
	}
}
