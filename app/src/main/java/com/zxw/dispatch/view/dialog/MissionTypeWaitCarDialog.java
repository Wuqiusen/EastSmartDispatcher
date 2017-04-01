package com.zxw.dispatch.view.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zxw.data.bean.MissionType;
import com.zxw.dispatch.R;
import com.zxw.dispatch.recycler.DividerItemDecoration;
import com.zxw.dispatch.recycler.MissionAdapter;

import java.util.List;

/**
 * author MXQ
 * create at 2017/3/23 09:39
 * email: 1299242483@qq.com
 * 描述:代替任务类型{未测试}
 */
public class MissionTypeWaitCarDialog extends AlertDialog.Builder{

    private Context mContext;
    private  Button btn_confirm;
    private Button btn_cancel;
    private LinearLayout ll_line_work;
    private LinearLayout ll_work_mission;
    private LinearLayout ll_no_work_mission;
    private TextView tv_line_name;
    private RadioButton rb_line_work;
    private RadioButton rb_work_mission;
    private RadioButton rb_no_work_mission;
    private RecyclerView rv_work_mission;
    private RecyclerView rv_no_work_mission;

    private List<MissionType> mMissionTypes;
    private int mType;
    private String mTaskId;
    private String mLineName;
    private MissionAdapter workMission;
    private MissionAdapter noWorkMission;
    private AlertDialog dialog;
    private OnChangeMissionTypeListener mListener;


    public MissionTypeWaitCarDialog(Context context, List<MissionType> missionTypes, final int objId, int type
                              , String taskId, String lineName, OnChangeMissionTypeListener listener) {
        super(context, R.style.alder_dialog);
        this.mContext = context;
        this.mType = type;
        this.mTaskId = taskId;
        this.mLineName = lineName;
        this.mMissionTypes = missionTypes;
        this.mListener = listener;
        init(context,objId,type,taskId);
    }

    private void init(Context context,final int objId,int type,String taskId) {
        View view = View.inflate(mContext, R.layout.view_task_type_dialog, null);
        btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        ll_line_work = (LinearLayout) view.findViewById(R.id.ll_line_work);
        ll_work_mission = (LinearLayout) view.findViewById(R.id.ll_work_mission);
        ll_no_work_mission = (LinearLayout) view.findViewById(R.id.ll_no_work_mission);
        tv_line_name = (TextView) view.findViewById(R.id.tv_line_name);
        rb_line_work = (RadioButton) view.findViewById(R.id.rb_line_work);
        rb_work_mission = (RadioButton) view.findViewById(R.id.rb_work_mission);
        rb_no_work_mission = (RadioButton) view.findViewById(R.id.rb_no_work_mission);
        rv_work_mission= (RecyclerView) view.findViewById(R.id.rv_work_mission);
        rv_no_work_mission = (RecyclerView) view.findViewById(R.id.rv_no_work_mission);
        rv_work_mission.setLayoutManager(new LinearLayoutManager(context));
        rv_work_mission.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL_LIST));
        rv_no_work_mission.setLayoutManager(new LinearLayoutManager(context));
        rv_no_work_mission.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL_LIST));

        tv_line_name.setText(mLineName);
        if (type == 1){//正线运行
            rb_line_work.setChecked(true);
        }else if (type == 2){//营运任务
            rb_work_mission.setChecked(true);
        }else if (type == 3){//非营运任务
            rb_no_work_mission.setChecked(true);
        }
        ll_line_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_line_work.setChecked(true);
                rb_work_mission.setChecked(false);
                rb_no_work_mission.setChecked(false);
                mType = 1;
                mTaskId = null;
                if (workMission != null)
                    workMission.choice(-1);
                if (noWorkMission != null)
                    noWorkMission.choice(-1);
            }
        });
        ll_work_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_line_work.setChecked(false);
                rb_work_mission.setChecked(true);
                rb_no_work_mission.setChecked(false);
                if (noWorkMission != null)
                    noWorkMission.choice(-1);
            }
        });
        ll_no_work_mission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_line_work.setChecked(false);
                rb_work_mission.setChecked(false);
                rb_no_work_mission.setChecked(true);
                if (workMission != null)
                    workMission.choice(-1);
            }
        });

        if (isHaveContent(mMissionTypes.get(1).getTaskContent())){
            workMission = new MissionAdapter(mMissionTypes.get(1).getTaskContent(),taskId,mContext,
                    new MissionAdapter.OnSelectMissionListener() {
                        @Override
                        public void onSelectMission(MissionType.TaskContentBean missionType) {
                            rb_line_work.setChecked(false);
                            rb_work_mission.setChecked(true);
                            rb_no_work_mission.setChecked(false);
                            mType = missionType.getType();
                            mTaskId = missionType.getTaskId() + "";
                            if (noWorkMission != null)
                                noWorkMission.choice(-1);

                        }
                    });
            rv_work_mission.setAdapter(workMission);
        }

        if (isHaveContent(mMissionTypes.get(2).getTaskContent())){
            noWorkMission = new MissionAdapter(mMissionTypes.get(2).getTaskContent(),taskId,mContext,
                    new MissionAdapter.OnSelectMissionListener() {
                        @Override
                        public void onSelectMission(MissionType.TaskContentBean missionType) {
                            rb_line_work.setChecked(false);
                            rb_work_mission.setChecked(false);
                            rb_no_work_mission.setChecked(true);
                            mType = missionType.getType();
                            mTaskId = missionType.getTaskId() + "";
                            if (workMission != null)
                                workMission.choice(-1);
                        }
                    });
            rv_no_work_mission.setAdapter(noWorkMission);
        }

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeMissionType(objId,mType,mTaskId);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
        dialog = setView(view).show();
        dialog.setCancelable(true);
    }

    private boolean isHaveContent(List<MissionType.TaskContentBean> list){
        return list != null && !list.isEmpty();
    }


    public interface OnChangeMissionTypeListener{
        void changeMissionType(final int objId,int type, String taskId);
    }


}
