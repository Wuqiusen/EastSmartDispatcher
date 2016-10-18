package com.zxw.dispatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zxw.data.bean.Line;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.MainPresenter;
import com.zxw.dispatch.presenter.view.MainView;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.MainAdapter;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.utils.SpUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends PresenterActivity<MainPresenter> implements MainView {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        presenter.loadLineList();
    }

    private void initView() {
        showTitle("主界面");
        showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.logOut(mContext);
                MainActivity.this.startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }
        });
    }



    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void loadLine(List<Line> lineList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MainAdapter(lineList, this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void reLogin() {
        SpUtils.logOut(MyApplication.mContext);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
