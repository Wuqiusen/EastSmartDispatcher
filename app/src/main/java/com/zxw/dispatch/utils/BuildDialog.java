package com.zxw.dispatch.utils;


import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.zxw.dispatch.view.MyDialog;

/**
 * Created by Huson on 2016/2/25.
 * 940762301@qq.com
 */
public class BuildDialog {
    private static BuildDialog mDialog = new BuildDialog();
    private static MyDialog sDialog;
    private static MyDialog bDialog;
    private static String mtitle;
    private static String msg;
    private static String mtype;
    private static String imgurl;
    private static Activity mactivity;
    private BuildDialog(){}
    public static BuildDialog myDialog(){
        if (mDialog == null){
            mDialog = new BuildDialog();
        }
        return mDialog;
    }

    /**
     * 文本显示对话框
     * @param activity
     * @param title 标题
     * @param mesg 内容
     * @param type 类型（HAVEBUTTON：有按钮，NOBUTTON：无按钮，IMAGEVIEW：图片，PROGRESS：进度条, ONEBUTTON：一个按钮）
     */
    public void showDialog(Activity activity, String title, String mesg, String type){
        mactivity = activity;
        msg = mesg;
        mtitle = title;
        mtype = type;
        sDialog = new MyDialog(mactivity, mtitle, msg, mtype);
        sDialog.show();

    }

    /**
     * 图片对话框
     * @param activity
     * @param url
     */
    public void showImageviewDialog(Activity activity, String url){
        mactivity = activity;
        imgurl = url;
        sDialog = new MyDialog(mactivity, imgurl);
        sDialog.show();

    }

    /**
     * 关闭对话框
     */
    public void DismissDialog(){
        if (sDialog != null){
            sDialog.dismiss();
        }
    }

    /**
     * 取消按钮监听
     * @param listener
     */
    public void ButtonCancel(View.OnClickListener listener){
        if (sDialog != null){
            sDialog.ButtonCancel(listener);
        }

    }

    /**
     * 确定按钮监听
     * @param listener
     */
    public void ButtonQuery(View.OnClickListener listener){
        if (sDialog != null){
            sDialog.ButtonQuery(listener);
        }

    }

    /**
     * 设置按钮名称
     * @param cancletext 取消按钮
     * @param querytext 确定按钮
     */
    public void SetButtonText(String cancletext, String querytext){
        if (sDialog != null){
            sDialog.SetButtonText(cancletext, querytext);
        }
    }

    /**
     * 设置对话框是否可以关闭
     * @param isCancel
     */
    public void setCancelable(boolean isCancel){
        if (sDialog != null){
            sDialog.setCancelable(isCancel);
        }
    }

    /**
     * 显示进度条
     * @param total 最大值
     * @param current 当前值
     */
    public void ProgressPlan(long total, long current){
        if (sDialog != null){
            sDialog.ProgressPlan(total, current);
        }
    }

    /**
     * 对话框关闭监听
     * @param dismissListener
     */
    public void DismissListener(DialogInterface.OnDismissListener dismissListener){
        if (sDialog != null){
            sDialog.setOnDismissListener(dismissListener);
        }

    }

    public boolean isDismiss(){
        return sDialog.isShowing();
    }

}
