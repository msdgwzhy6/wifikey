package com.wingsoft.wifikey.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.enmu.YoumiAd;

import java.util.Timer;
import java.util.TimerTask;

public class Logo extends ActionBarActivity {
    public static boolean isAd = true;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case YoumiAd.ISAD:

                break;
                default:


            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        final Intent intent = new Intent(Logo.this,Main.class);
        TimerTask task = new TimerTask() {
            public void run() {
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 5000);

    }




}
