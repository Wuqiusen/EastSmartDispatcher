package com.zxw.dispatch.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.SendHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.SendPresenter;
import com.zxw.dispatch.presenter.view.SendView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.SendAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SendActivity extends PresenterActivity<SendPresenter> implements SendView {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private int lineId;
    private String lineName;
    private Line.LineStation station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);
        showTitle(station.stationName + " 已发车列表");
        showBackButton();
        showInfoBar(lineName);
        presenter.loadSend();
    }

    @Override
    protected SendPresenter createPresenter() {
        lineId = getIntent().getIntExtra("lineId", -1);
        lineName = getIntent().getStringExtra("lineName");
        station = (Line.LineStation) getIntent().getSerializableExtra("station");
        return new SendPresenter(this, lineId, station.stationId);
    }
    @Override
    public void loadSend(List<SendHistory> sends) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new SendAdapter(sends, this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }
}
