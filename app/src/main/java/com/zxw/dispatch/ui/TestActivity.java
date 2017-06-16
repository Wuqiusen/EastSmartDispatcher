package com.zxw.dispatch.ui;

import android.os.Bundle;

import com.zxw.data.bean.DriverWorkloadItem;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.R;
import com.zxw.dispatch.module.OperatorModule;
import com.zxw.dispatch.module.ScheduleModule;
import com.zxw.dispatch.ui.base.BaseHeadActivity;

import java.util.List;

import rx.Subscriber;

public class TestActivity extends BaseHeadActivity implements ScheduleModule.OnLoadingListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

}
