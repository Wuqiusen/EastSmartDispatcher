package com.zxw.dispatch.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zxw.dispatch.R;
import com.zxw.dispatch.recycler.DividerItemDecoration;


/**
 * 当前类注析：
 * Created by huson on 2016/9/23.
 * 940762301@qq.com
 */

public class CreateRecyclerView {
    private EasyRecyclerView easyRecycleView;
    private Context context;
    private RecyclerArrayAdapter arrayAdapter;
    private RecyclerArrayAdapter.OnLoadMoreListener listener;

    public void CreateRecyclerView(Context context, EasyRecyclerView easyRecycleView,
                                   RecyclerArrayAdapter arrayAdapter,
                                   RecyclerArrayAdapter.OnLoadMoreListener listener){
        this.context = context;
        this.easyRecycleView = easyRecycleView;
        this.arrayAdapter = arrayAdapter;
        this.listener = listener;
        initRecyclerView();

    }
    public void CreateRecyclerView(Context context, EasyRecyclerView easyRecycleView,
                                   RecyclerArrayAdapter arrayAdapter){
        this.context = context;
        this.easyRecycleView = easyRecycleView;
        this.arrayAdapter = arrayAdapter;
        initRecyclerView(1);

    }

    public void setLayoutManager(Context context, EasyRecyclerView easyRecycleView){
        this.context = context;
        this.easyRecycleView = easyRecycleView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        easyRecycleView.setLayoutManager(layoutManager);
        easyRecycleView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        easyRecycleView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        easyRecycleView.setLayoutManager(layoutManager);
        easyRecycleView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        easyRecycleView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
//        easyRecycleView.setProgressView(R.layout.view_progress);
//        easyRecycleView.setEmptyView(R.layout.view_empty);
        easyRecycleView.setAdapterWithProgress(arrayAdapter);
        arrayAdapter.setMore(R.layout.list_item_load_more, listener);
        arrayAdapter.setNoMore(R.layout.list_item_no_more);
        arrayAdapter.setError(R.layout.view_error);
        easyRecycleView.setRefreshingColor(context.getResources().getColor(R.color.main));

    }
    private void initRecyclerView(int type) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        easyRecycleView.setLayoutManager(layoutManager);
        easyRecycleView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        easyRecycleView.setAdapter(arrayAdapter);

    }
}
