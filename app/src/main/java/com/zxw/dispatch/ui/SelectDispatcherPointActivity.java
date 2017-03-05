package com.zxw.dispatch.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.SelectDispatcherPointPresenter;
import com.zxw.dispatch.presenter.view.SelectDispatcherPointView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.SpotAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SelectDispatcherPointActivity extends PresenterActivity<SelectDispatcherPointPresenter> implements SelectDispatcherPointView {
    @Bind(R.id.rv_dispatcher_point)
    RecyclerView mSpotRV;
    @Override
    protected SelectDispatcherPointPresenter createPresenter() {
        return new SelectDispatcherPointPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dispatcher_point);
        initView();
        ButterKnife.bind(this);
        presenter.loadDispatcherPoint();
    }

    private void initView() {
        showColorTitle("请选择调度站");
        showHeadAreaBackground();
    }

    @Override
    public void showSpotList(SpotAdapter spotAdapter) {
//      mSpotRV.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSpotRV.setLayoutManager(layoutManager);
        mSpotRV.setAdapter(spotAdapter);
        mSpotRV.addItemDecoration(new DividerItemDecoration(this,R.color.white,
                DividerItemDecoration.VERTICAL_LIST));
    }
}
