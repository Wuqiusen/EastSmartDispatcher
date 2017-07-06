package com.zxw.dispatch.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.zxw.data.bean.GroupMessageDetail;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.R;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.presenter.GroupMessageDetailPresenter;
import com.zxw.dispatch.presenter.view.GroupMessageDetailView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupMessageDetailActivity extends PresenterActivity<GroupMessageDetailPresenter> implements GroupMessageDetailView {
    @Bind(R.id.tv_content)
    TextView tv_content;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.tv_author)
    TextView tv_author;
    @Override
    protected GroupMessageDetailPresenter createPresenter() {
        return new GroupMessageDetailPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message_detail);
        ButterKnife.bind(this);
        int messageId = getIntent().getIntExtra("messageId", -1);
        presenter.loadData(messageId);
        showBackButton();
    }

    @Override
    public void showContent(GroupMessageDetail groupMessageDetail) {
        showTitle(groupMessageDetail.getNoticeTypeName());
        String runDate = groupMessageDetail.getNoticeDate();
        tv_time.setText(getResources().getString(R.string.group_message_send_time, runDate));
        tv_author.setText(getResources().getString(R.string.group_message_send_author, groupMessageDetail.getUserName()));
        tv_content.setText(groupMessageDetail.getNoticeInfo());
    }
}
