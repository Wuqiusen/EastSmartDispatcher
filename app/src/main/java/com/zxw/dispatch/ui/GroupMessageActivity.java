package com.zxw.dispatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;
import android.widget.Button;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zxw.data.bean.GroupMessagesBean;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.GroupMessageViewHolder;
import com.zxw.dispatch.adapter.WorkLoadVerifyViewHolder;
import com.zxw.dispatch.presenter.BasePresenter;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.presenter.GroupMessagePresenter;
import com.zxw.dispatch.presenter.view.GroupMessageView;
import com.zxw.dispatch.utils.CreateRecyclerView;
import com.zxw.dispatch.view.dialog.SendGroupMessageDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupMessageActivity extends PresenterActivity<GroupMessagePresenter> implements GroupMessageView, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.rv_group_message)
    EasyRecyclerView rv_group_message;
    @Bind(R.id.btn_create_group_message)
    Button btn_create_group_message;
    private RecyclerArrayAdapter arrayAdapter;

    @Override
    protected GroupMessagePresenter createPresenter() {
        int lineId = getIntent().getIntExtra("lineId", -1);
        return new GroupMessagePresenter(this, lineId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        ButterKnife.bind(this);
        presenter.onRefresh();
        initRecycleView();
        showTitle("群发消息记录");
        showBackButton();
    }

    private void initRecycleView() {
        arrayAdapter = new RecyclerArrayAdapter(mContext) {
            @Override
            public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
                return new GroupMessageViewHolder(parent, new GroupMessageViewHolder.OnGroupMessagesItemClickListener() {
                    @Override
                    public void onGroupMessagesItemClick(int id) {
                        Intent intent = new Intent(GroupMessageActivity.this, GroupMessageDetailActivity.class);
                        intent.putExtra("messageId", id);
                        GroupMessageActivity.this.startActivity(intent);
                    }
                });
            }

        };
        new CreateRecyclerView().CreateRecyclerView(mContext, rv_group_message, arrayAdapter, this);
        rv_group_message.setRefreshListener(this);
    }

    @Override
    public void onLoadMore() {
        presenter.loadMore();
    }

    @Override
    public void onRefresh() {
        arrayAdapter.clear();
        presenter.onRefresh();
    }

    @Override
    public void stopMore() {
        arrayAdapter.stopMore();
    }

    @Override
    public void setData(List<GroupMessagesBean> returnData) {
        arrayAdapter.addAll(returnData);
    }

    @OnClick(R.id.btn_create_group_message)
    public void createGroupMessage(){
        new SendGroupMessageDialog(mContext, new SendGroupMessageDialog.OnSendGroupMessageDialogListener() {
            @Override
            public void sendGroupMessage(String message) {
                presenter.sendGroupMessage(message);
            }
        });
    }
}
