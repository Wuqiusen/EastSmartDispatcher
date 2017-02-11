package com.zxw.dispatch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zxw.dispatch.R;

public class WeatherDialogActivity extends Activity {
    private String weather = "天气：";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_weather);
    }
    public void sunDay(View view){
        weather = weather + "晴";
        setWeather();
    }

    public void cloudyDay(View view){
        weather = weather + "阴";
        setWeather();
    }
    public void rainDay(View view){
        weather = weather + "雨";
        setWeather();
    }
    public void fogDay(View view){
        weather = weather + "雾";
        setWeather();
    }

    private void setWeather(){
        Intent intent = new Intent();
        intent.putExtra("weather", weather);
        WeatherDialogActivity.this.setResult(RESULT_OK, intent);
        WeatherDialogActivity.this.finish();
    }
}
