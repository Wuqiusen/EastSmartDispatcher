package com.zxw.dispatch.ui;

import android.os.Bundle;

import com.zxw.dispatch.R;
import com.zxw.dispatch.module.OperatorModule;
import com.zxw.dispatch.module.ScheduleModule;
import com.zxw.dispatch.ui.base.BaseHeadActivity;

public class TestActivity extends BaseHeadActivity implements ScheduleModule.OnLoadingListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        ScheduleModule schedule_module = (ScheduleModule) findViewById(R.id.schedule_module);
//        schedule_module.setOnLoadingListener(this);
//        LineParams lineParams = new LineParams();
//        lineParams.setSaleType(1);
//        lineParams.setTimeType(1);
//        schedule_module.initLineParams(3, lineParams);
        OperatorModule operator_module = (OperatorModule) findViewById(R.id.operator_module);

    }

}
