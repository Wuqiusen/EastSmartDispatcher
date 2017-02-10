package com.zxw.dispatch.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zxw.data.bean.VersionBean;
import com.zxw.dispatch.Constants;
import com.zxw.dispatch.MyApplication;
import com.zxw.dispatch.R;
import com.zxw.dispatch.presenter.WelcomePresenter;
import com.zxw.dispatch.presenter.view.WelcomeView;
import com.zxw.dispatch.ui.base.PresenterActivity;
import com.zxw.dispatch.ui.my.ForgetPasswordActivity;
import com.zxw.dispatch.utils.BuildDialog;
import com.zxw.dispatch.utils.DebugLog;
import com.zxw.dispatch.utils.IsNetworkAvailable;
import com.zxw.dispatch.utils.SpUtils;
import com.zxw.dispatch.utils.ToastHelper;
import com.zxw.dispatch.utils.VersionUpdateUtils;
import com.zxw.dispatch.view.MyDialog;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WelcomeActivity extends PresenterActivity<WelcomePresenter> implements WelcomeView {
    @Bind(R.id.img_welcome)
    ImageView img_welcome;
    @Bind(R.id.tv_version_name)
    TextView tv_version_name;

    private Message msg;
    private String downLoadUrl;
    private String desc;
    private String mVersionName;

    private PackageManager pm;
    private int currentVersion;
    private static final int NEW_VERSION =1;
    private static final int OLD_VERSION =2;
    private HttpHandler httpHandler;
    private MyDialog mDialog = null;
    private boolean flag;

    @Override
    protected WelcomePresenter createPresenter() {
        return new WelcomePresenter(this,this);
    }

    private Handler handler = new Handler(){
        public  void handleMessage(Message msg){
            switch (msg.what){
                case NEW_VERSION:
                    if (!isFinishing()) {
                        BuildDialog.myDialog().showDialog(WelcomeActivity.this,"版本更新",desc, MyDialog.HAVEBUTTON);
                        BuildDialog.myDialog().SetButtonText("取消","确定");
                        BuildDialog.myDialog().setCancelable(false);
                        BuildDialog.myDialog().ButtonCancel(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 进入主页面或登录页面
                                loadMain();
                                BuildDialog.myDialog().DismissDialog();
                            }
                        });

                        BuildDialog.myDialog().ButtonQuery(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BuildDialog.myDialog().DismissDialog();
                                // 判断SD卡内容
                                if (VersionUpdateUtils.getSDCardMemory(mContext)){
                                    download(downLoadUrl);
                                }
                            }
                        });
                    }
                    break;
                case OLD_VERSION:
                    loadMain();
                    break;
            }

        }
    };

    private void download(String downLoadUrl) {
        //下载本地sdcard
        final HttpUtils utils = new HttpUtils();
        httpHandler = utils.download(downLoadUrl, Environment.getExternalStorageDirectory() + "/" + Constants.APP_PATH.SECONDPATH + "/" + Constants.APP_PATH.APKNAME, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                flag = true;
                if (!isFinishing() && mDialog != null){
                    mDialog.dismiss();
                }
                //下载成功
                //安装
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_PATH.SECONDPATH, Constants.APP_PATH.APKNAME)), "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);
                finish();
            }

            @Override
            public void onStart() {
                super.onStart();
                mDialog = new MyDialog(WelcomeActivity.this,  "正在下载请稍后···", null, MyDialog.PROGRESS);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!flag) {
                            outLoading();
                        }
                    }
                });
                mDialog.show();
            }


            @Override
            public void onFailure(HttpException arg0, String arg1) {
                ToastHelper.showToast("下载失败，请稍后再试", WelcomeActivity.this);
                flag = true;
                if (mDialog != null){
                    mDialog.dismiss();
                }
                finish();
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                mDialog.ProgressPlan(total, current);
            }
        });
    }

    private void outLoading() {
        BuildDialog.myDialog().showDialog(WelcomeActivity.this, "提示", "正在下载新版本，你确定要停止吗？", MyDialog.HAVEBUTTON);
        BuildDialog.myDialog().ButtonCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildDialog.myDialog().DismissDialog();
                if (mDialog != null)
                    mDialog.show();
            }
        });
        BuildDialog.myDialog().ButtonQuery(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildDialog.myDialog().DismissDialog();
                if (httpHandler != null) {
                    httpHandler.cancel();
                    finish();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initData();
        hideHeadArea();


        animation();


    }

    private void initData() {
        pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(),0);
            String versionName = info.versionName;
            tv_version_name.setText("V "+versionName);
            currentVersion = info.versionCode;
            DebugLog.i("当前版本号:"+currentVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void animation() {
        // 初始化控件
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2F, 0.9F);
        animationSet.setDuration(1000);
        animationSet.addAnimation(alphaAnimation);
        // 监听动画过程
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 版本更新
                 presenter.checkVersion();

            }
        });
        img_welcome.startAnimation(animationSet);
    }

    @Override
    public void respondVersionSuccess(long startTime, VersionBean versionBean) {
           DebugLog.e("startTime:"+startTime);
           DebugLog.e("Version:"+versionBean);
           msg = Message.obtain();
           try {
               if (versionBean != null) {
                   int serviceCode = versionBean.codeNum;
                   desc = versionBean.remarks;
                   downLoadUrl = versionBean.url;
                   mVersionName = versionBean.code;
                   if (serviceCode > currentVersion) {
                       msg.what = NEW_VERSION;
                   }else{
                       msg.what = OLD_VERSION;
                   }
               }else {
                   msg.what = OLD_VERSION;
               }
           }finally{
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < 2000) {
                    SystemClock.sleep(2000-(endTime-startTime));
                }
                handler.sendMessage(msg);
           }
    }

    @Override
    public void respondVersionFailed(String errorMessage, long startTime, String keyCode) {
        // 获取版本信息的请求，未成功
            new Thread(){
                  @Override
                  public void run(){
                      try {
                         runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    disPlay("正在获取网络请求，请静候！");
                            }
                         });
                         Thread.sleep(500);
                          presenter.checkVersion();
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
              }.start();
    }



    private void loadMain() {
        if(isUserLogin()){
            goToMainPage();
        }else{
            goToLoginPage();
        }
    }


    public boolean isUserLogin(){
        String code = SpUtils.getCache(MyApplication.mContext, SpUtils.CODE);
        String keycode = SpUtils.getCache(MyApplication.mContext, SpUtils.KEYCODE);
        DebugLog.w(code);
        DebugLog.w(keycode);
        if(TextUtils.isEmpty(code) || TextUtils.isEmpty(keycode))
            return false;
        return true;
    }

    public void goToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();//关闭当前界面
    }

    public void goToLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();//关闭当前界面
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadMain();
    }

}
