package com.zxw.dispatch.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zxw.dispatch.R;
import com.zxw.dispatch.utils.ToastHelper;

/**
 * Created by huson on 2015/10/14.
 */
public class MyDialog extends AlertDialog {
    private String imgurl;
    private Context backContext;
    private Activity backactivity;
    private String mtitle;
    private String mtype;
    private long mtotal;
    private long mcurrent;
    private String mmesg;
    private TextView title;
    private TextView line;
    private TextView mesg;
    private TextView tv_percent;
    private LinearLayout ll_tn;
    private ProgressBar dialog_progressbar;
    private ImageView imageView;
    private Button cancel;
    private Button query;

    private View mDialogView;
    private AnimationSet mModalInAnim;
    private AnimationSet mModalOutAnim;
    private boolean mCloseFromCancel;


    /**
     * HAVEBUTTON:有两个按钮，NOBUTTON：没有按钮， PROGRESS：进度条
     * IMAGEVIEW：图片， ONEBUTTON：一个按钮
     */
    public static final String HAVEBUTTON = "havebutton";
    public static final String NOBUTTON = "nobutton";
    public static final String PROGRESS = "progress";
    public static final String IMAGEVIEW = "imageview";
    public static final String ONEBUTTON = "onebutton";

    /**
     * @param context
     * @param url
     */

    public MyDialog(Activity context, String url) {
        super(context);
        backactivity = context;
        imgurl = url;
        mtype = IMAGEVIEW;
    }

    public MyDialog(Context context, int theme, Activity activity) {
        super(context, theme);
        backContext = context;
        backactivity = activity;

    }

    public MyDialog(Activity activity, String title, String mesg, String type) {
        super(activity);
        backactivity = activity;
        mtitle = title;
        mmesg = mesg;
        mtype = type;
        mModalInAnim = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.modal_in);
        mModalOutAnim = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.modal_out);
//      mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_in);
//      mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.modal_out);
        mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.setVisibility(View.GONE);
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCloseFromCancel) {
                            MyDialog.super.cancel();
                        } else {
                            MyDialog.super.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_mydialog);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        imageView =(ImageView) findViewById(R.id.img_dialog_station);
        dialog_progressbar =(ProgressBar) findViewById(R.id.dialog_progressbar);
        title = (TextView) findViewById(R.id.title_img);
        line = (TextView) findViewById(R.id.line_img);
        mesg = (TextView) findViewById(R.id.mesage_img);
        tv_percent = (TextView) findViewById(R.id.tv_percent);
        ll_tn= (LinearLayout) findViewById(R.id.ll_dialog);
        cancel = (Button) findViewById(R.id.cancel_dialog);
        query = (Button) findViewById(R.id.query_dialog);
        title.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        mesg.setVisibility(View.GONE);
        ll_tn.setVisibility(View.GONE);
        tv_percent.setVisibility(View.GONE);

        initView();

    }

    private void initView(){

        switch (mtype){
            case HAVEBUTTON:
                title.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                mesg.setVisibility(View.VISIBLE);
                ll_tn.setVisibility(View.VISIBLE);
                title.setText(mtitle);
                mesg.setText(mmesg);
                break;
            case PROGRESS:
                tv_percent.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                title.setText(mtitle);
                dialog_progressbar.setVisibility(View.VISIBLE);
                setCanceledOnTouchOutside(false);

                break;
            case IMAGEVIEW://未可用
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;
            case NOBUTTON:
                title.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                mesg.setVisibility(View.VISIBLE);
                ll_tn.setVisibility(View.INVISIBLE);
                title.setText(mtitle);
                mesg.setText(mmesg);
                break;
            case ONEBUTTON:
                title.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                mesg.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.GONE);
                ll_tn.setVisibility(View.VISIBLE);
                title.setText(mtitle);
                mesg.setText(mmesg);
                break;


        }
    }

    public void ProgressPlan(long total, float current) {
        dialog_progressbar.setMax((int) total);
        dialog_progressbar.setProgress((int) current);
        if (dialog_progressbar.getMax() != 0) {
            int i = dialog_progressbar.getProgress() * 100 / dialog_progressbar.getMax();
            tv_percent.setText(String.valueOf(i) + "%");
        } else {
            ToastHelper.showToast("当前网络状态不良,请稍后重试", backactivity);
        }
    }

    @Override
    protected void onStart() {
        mDialogView.startAnimation(mModalInAnim);
    }

    public void ButtonCancel(View.OnClickListener listener){
        cancel.setOnClickListener(listener);
    }

    public void ButtonQuery(View.OnClickListener listener){
        query.setOnClickListener(listener);
    }

    public void SetButtonText(String calaeltext, String querytext){
        cancel.setText(calaeltext);
        query.setText(querytext);
    }

}
