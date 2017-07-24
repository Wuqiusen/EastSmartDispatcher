package com.zxw.dispatch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zxw.data.bean.ContactInfo;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.adapter.ContactInfoAdapter;
import com.zxw.dispatch.commucation.RealCallActivity;
import com.zxw.dispatch.presenter.CallLauncherPresenter;
import com.zxw.dispatch.presenter.view.CallLauncherView;
import com.zxw.dispatch.ui.base.PresenterActivity;

import java.util.ArrayList;
import java.util.List;

import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.imlib.model.Conversation;

public class CallLauncherActivity extends PresenterActivity<CallLauncherPresenter> implements CallLauncherView, ContactInfoAdapter.OnClickContactListener {

    private RecyclerView rv_contact;
    @Override
    protected CallLauncherPresenter createPresenter() {
        return new CallLauncherPresenter(this, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_launcher);
        rv_contact = (RecyclerView) findViewById(R.id.rv_contact);
        rv_contact.setLayoutManager(new GridLayoutManager(mContext, 3));

        presenter.contactList();
    }



    private void startCallActivity(String currentCallId, boolean isCalled){
        Intent intent = new Intent(MyApplication.mContext, RealCallActivity.class);
        intent.putExtra("callId", currentCallId);
        intent.putExtra("isCalled", isCalled);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.mContext.startActivity(intent);
    }

    @Override
    public void setAdapter(List<ContactInfo> contactInfos) {
        rv_contact.setAdapter(new ContactInfoAdapter(mContext, contactInfos, this));

    }

    @Override
    public void onClickListener(String rongId) {
        ArrayList<String> userList = new ArrayList<>();
        userList.add(rongId);
        String callId = RongCallClient.getInstance().startCall(Conversation.ConversationType.PRIVATE, rongId, userList, RongCallCommon.CallMediaType.AUDIO, null);
        startCallActivity(callId, false);
    }
}
