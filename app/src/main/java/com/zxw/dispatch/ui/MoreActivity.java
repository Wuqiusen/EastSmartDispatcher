package com.zxw.dispatch.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zxw.data.bean.Line;
import com.zxw.data.bean.MoreHistory;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MorePresenter;
import com.zxw.dispatch.presenter.view.MoreView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.MoreAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoreActivity extends PresenterActivity<MorePresenter> implements MoreView {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private int lineId;
    private String lineName;
    private Line.LineStation station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        showTitle("更多列表");
        showBackButton();
        String lineNo = getIntent().getStringExtra("lineNo");
        showInfoBar(lineNo);
        presenter.loadMore();
    }

    @Override
    protected MorePresenter createPresenter() {
        lineId = getIntent().getIntExtra("lineId", -1);
        lineName = getIntent().getStringExtra("lineName");
        station = (Line.LineStation) getIntent().getSerializableExtra("station");
        return new MorePresenter(this, lineId, station.stationId);
    }

    @Override
    public void loadMore(List<MoreHistory> mores) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MoreAdapter(mores, this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }
}
