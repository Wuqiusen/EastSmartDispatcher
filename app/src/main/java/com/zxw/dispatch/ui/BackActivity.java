package com.zxw.dispatch.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zxw.data.bean.BackHistory;
import com.zxw.data.bean.Line;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.EndPresenter;
import com.zxw.dispatch.presenter.view.EndView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.EndAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 收车列表
 */
public class BackActivity extends PresenterActivity<EndPresenter> implements EndView {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private int lineId;
    private String lineName;
    private Line.LineStation station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        ButterKnife.bind(this);
        showTitle(station.stationName + " 已收车列表");
        showBackButton();
        showInfoBar(lineName);
        presenter.loadSend();
    }

    @Override
    protected EndPresenter createPresenter() {
        lineId = getIntent().getIntExtra("lineId", -1);
        lineName = getIntent().getStringExtra("lineName");
        station = (Line.LineStation) getIntent().getSerializableExtra("station");
        return new EndPresenter(this, lineId, station.stationId);
    }

    @Override
    public void loadEnd(List<BackHistory> backs) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new EndAdapter(backs, this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }
}
