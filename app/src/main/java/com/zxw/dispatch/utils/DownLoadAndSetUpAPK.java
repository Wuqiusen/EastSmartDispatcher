package com.zxw.dispatch.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.zxw.data.http.HttpInterfaces;
import com.zxw.data.http.HttpMethods;
import com.zxw.dispatch.Constants;
import com.zxw.dispatch.utils.file_download_dialog.DownloadProgressHandler;
import com.zxw.dispatch.utils.file_download_dialog.ProgressHelper;
import com.zxw.dispatch.view.MyDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：${MXQ} on 2017/2/10 17:58
 * 邮箱：1299242483@qq.com
 */
public class DownLoadAndSetUpAPK {
    private Activity mActivity;
    private MyDialog progressDialog;
    public HttpInterfaces.UpdateVersion apiStore;
    private Call<ResponseBody> call;
    private String url;
    private boolean isLoadAll = false;
    private boolean isLoadSuccess = true;
    private LoadFailure loadFailure = null;
    public void DownLoadAndSetUpAPK(Activity activity, String downUrl, LoadFailure loadFailure){
        mActivity = activity;
        url = downUrl;
        this.loadFailure = loadFailure;
        showDialog();
        setHttp();
        progressHandler();
        setCallBack();
    }

    public void DownLoadAndSetUpAPK(Activity activity, String downUrl){
        mActivity = activity;
        url = downUrl;
        showDialog();
        setHttp();
        progressHandler();
        setCallBack();
    }

    private void showDialog(){
        progressDialog = new MyDialog(mActivity, "正在下载，请稍后...", "", MyDialog.PROGRESS);
        progressDialog.show();
        progressDialogListener();
    }

    private void setHttp(){
        Retrofit.Builder retrofitBuilder =
                // 获取一个实例
                new Retrofit.Builder()
                        // 使用RxJava作为回调适配器
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        // 添加Gson转换器
                        .addConverterFactory(GsonConverterFactory.create())
                        // 设置baseUrl{以"/"结尾}
                        .baseUrl(HttpMethods.BASE_URL);
        // Retrofit文件下载进度显示的解决方法
        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);

        apiStore = retrofitBuilder
                .client(builder.build())
                .build().create(HttpInterfaces.UpdateVersion.class);
    }

    private void progressHandler(){
        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                Log.e("是否在主线程中运行", String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
                Log.e("onProgress", String.format("%d%% done\n", (100 * bytesRead) / contentLength));
                Log.e("done", "--->" + String.valueOf(done));
                progressDialog.ProgressPlan(contentLength / 1024, bytesRead / 1024);
                if (done) {
                    isLoadAll = true;
                    progressDialog.dismiss();

                }
            }
        });
    }

    private void setCallBack(){
        call = apiStore.getFile(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断SD卡是否挂载
                        File foder = new File(Environment.getExternalStorageDirectory(), Constants.Path.SECONDPATH + "/");
                        File file = new File(foder, Constants.Path.APKNAME);
                        if (!foder.exists()) {
                            foder.mkdirs();
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();
                        }
                        fos.close();
                        bis.close();
                        is.close();
                        new SetUpAPK().setUpAPK(mActivity);//下载成功 安装

                    }else {
                        ToastHelper.showToast("请检查你的SD卡", mActivity);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastHelper.showToast("更新失败", mActivity);
                DebugLog.e(t.getMessage());
                isLoadSuccess = false;
                progressDialog.dismiss();
                if (loadFailure != null){
                    loadFailure.onLoadFailureListener();
                }

            }
        });

    }

    private void progressDialogListener(){
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isLoadAll && isLoadSuccess){
                    showTipDialog();

                }
            }
        });
    }

    private void showTipDialog(){
        // 必须暂停更新
        final MyDialog tipDialog = new MyDialog(mActivity, "提示", "确定要放弃更新？", MyDialog.HAVEBUTTON);
        tipDialog.show();
        tipDialog.setCancelable(false);
        tipDialog.ButtonQuery(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.dismiss();
                call.cancel();
            }
        });

        tipDialog.ButtonCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.dismiss();
                progressDialog.show();
            }
        });

    }

    public void setOnLoadFailureListener(LoadFailure loadFailureListener){
        this.loadFailure = loadFailureListener;
    }

    public interface LoadFailure{
        void onLoadFailureListener();
    }


}
