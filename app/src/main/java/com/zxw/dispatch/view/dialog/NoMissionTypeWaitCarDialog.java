package com.zxw.dispatch.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.zxw.dispatch.R;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.NonMissionTypeAdapter;

/**
 * author MXQ
 * create at 2017/3/29 12:45
 * email: 1299242483@qq.com
 */
public class NoMissionTypeWaitCarDialog extends AlertDialog.Builder{
    private Context mContext;
    private Button btn_close;
    private RecyclerView rvMission;
    private AlertDialog dialog;
    private NonMissionTypeAdapter mAdapter;

    public NoMissionTypeWaitCarDialog(Context context,NonMissionTypeAdapter adapter) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mAdapter = adapter;
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context,R.layout.view_no_operation_task_dialog,null);
        btn_close = (Button) view.findViewById(R.id.btn_close);
        rvMission = (RecyclerView) view.findViewById(R.id.rv_non_mission_status);
        rvMission.setLayoutManager(new LinearLayoutManager(context));
        rvMission.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL_LIST));
        rvMission.setAdapter(mAdapter);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog = setView(view).show();
        dialog.setCancelable(true);
    }


}
