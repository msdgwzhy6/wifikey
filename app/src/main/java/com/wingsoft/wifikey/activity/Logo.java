package com.wingsoft.wifikey.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.enmu.YoumiAd;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import java.util.Timer;
import java.util.TimerTask;

public class Logo extends ActionBarActivity {
    public static boolean isAd = true;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case YoumiAd.ISAD:
                    SpotManager.getInstance(Logo.this).showSplashSpotAds(Logo.this, Main.class);
                break;
                default:

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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);


        AdManager.getInstance(this).init("d4df0c25a8497f75", "ff823fe457fe89bb",false);
        AdManager.getInstance(this).setUserDataCollect(true);
        SpotManager.getInstance(this).loadSplashSpotAds();
        AdManager.getInstance(this).setUserDataCollect(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String value = AdManager.getInstance(Logo.this).syncGetOnlineConfig("IsAd", "what");
                if(value.equals("true")){
                    Message m = mHandler.obtainMessage();
                    m.what = YoumiAd.ISAD;
                    mHandler.sendMessage(m);
                    Log.i("value",value);

                }else{
                    Message m = mHandler.obtainMessage();
                    m.what = YoumiAd.NOTAD;
                    mHandler.sendMessage(m);
                    Log.i("value",value);
                }
            }
        }).start();

    }




}
