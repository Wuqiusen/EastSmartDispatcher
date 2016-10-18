package com.zxw.dispatch.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.StopHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.StopPresenter;
import com.zxw.dispatch.presenter.view.StopView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.StopAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  停班列表
 */
public class StopActivity extends PresenterActivity<StopPresenter> implements StopView {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private int lineId;
    private String lineName;
    private Line.LineStation station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        ButterKnife.bind(this);
        showTitle(station.stationName + " 已停班列表");
        showBackButton();
        showInfoBar(lineName);
        presenter.loadStop();
    }

    @Override
    protected StopPresenter createPresenter() {
        lineId = getIntent().getIntExtra("lineId", -1);
        lineName = getIntent().getStringExtra("lineName");
        station = (Line.LineStation) getIntent().getSerializableExtra("station");
        return new StopPresenter(this, lineId, station.stationId);
    }

    @Override
    public void loadStop(List<StopHistory> stops) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new StopAdapter(stops, this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }
}
