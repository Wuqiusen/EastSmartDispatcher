package com.zxw.dispatch.utils;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;


/**
 * Created by Huson on 2016/3/16.
 * 940762301@qq.com
 */
public class GetCodeBtnStyle {
    private static GetCodeBtnStyle btnStyle = new GetCodeBtnStyle();
    private final static int REST = 198;
    private final static int REST_FINISH = 199;
    private int restTime;
    private Button mbtn;

    public static GetCodeBtnStyle btnStyle(){
        if (btnStyle == null){
            btnStyle = new GetCodeBtnStyle();
        }
        return btnStyle;
    }

    public Handler handler = new Handler(){
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case REST:
                    mbtn.setText((60 - restTime) + "秒后再次获取");
                    break;
                case REST_FINISH:
                    mbtn.setText("获取验证码");
                    mbtn.setClickable(true);
                    break;
            }
        }
    };


    public void GetCodeBtnStyle(Button btn){
        mbtn = btn;
        //开启线程，设置按钮60秒不可点
        btn.setClickable(false);
        restTime = 0;
        new Thread(){
            @Override
            public void run() {
                try {
                    while(restTime < 60){

                        Message msg = Message.obtain();
                        msg.what = REST;
                        handler.sendMessage(msg);
                        restTime++;
                        sleep(1000);
                    }
                    Message msg = Message.obtain();
                    msg.what = REST_FINISH;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void setClickFalse(){
        if (mbtn != null){
            mbtn.setClickable(false);
        }
    }
    public void setClickTrue(){
        if (mbtn != null){
            mbtn.setClickable(true);
        }
    }
}
