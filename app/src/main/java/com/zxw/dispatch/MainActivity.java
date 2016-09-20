package com.zxw.dispatch;

import android.os.Bundle;

import com.zxw.dispatch.ui.base.BaseHeadActivity;

public class MainActivity extends BaseHeadActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showTitle("主界面");

    }
}
