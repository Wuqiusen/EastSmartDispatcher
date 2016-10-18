package com.zxw.dispatch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zxw.data.bean.Line;
import com.zxw.dispatch.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogActivity extends Activity {

    private int lineId;
    private String lineName;
    private Line.LineStation station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        lineId = getIntent().getIntExtra("lineId", -1);
        lineName = getIntent().getStringExtra("lineName");
        station = (Line.LineStation) getIntent().getSerializableExtra("station");
    }

    @OnClick(R.id.btn_send)
    public void send(){
        Intent intent = new Intent(this, SendActivity.class);
        intent.putExtra("lineId", lineId);
        intent.putExtra("lineName", lineName);
        intent.putExtra("station", station);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.btn_stop)
    public void stop(){
        Intent intent = new Intent(this, StopActivity.class);
        intent.putExtra("lineId", lineId);
        intent.putExtra("lineName", lineName);
        intent.putExtra("station", station);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.btn_end)
    public void end(){
        Intent intent = new Intent(this, BackActivity.class);
        intent.putExtra("lineId", lineId);
        intent.putExtra("lineName", lineName);
        intent.putExtra("station", station);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.btn_more)
    public void more(){
        Intent intent = new Intent(this, MoreActivity.class);
        intent.putExtra("lineId", lineId);
        intent.putExtra("lineName", lineName);
        intent.putExtra("station", station);
        startActivity(intent);
        finish();
    }
}
