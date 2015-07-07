package com.wingsoft.wifikey.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.fragment.AboutFragment;
import com.wingsoft.wifikey.fragment.NewsFragment;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.dialog.MyDialog;
import com.wingsoft.wifikey.model.Wifi;

import android.os.Handler;
import android.widget.Toast;

public class Main extends ActionBarActivity implements View.OnClickListener {
    private LinearLayout fragmentLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private WifiFragment fragment_Wifi = new WifiFragment();
    private WifiFragment _fragment_Wifi2 = new WifiFragment();
    private AboutFragment fragment_About = new AboutFragment();
    private NewsFragment fragment_News = new NewsFragment();
    private Button buttonWifi,buttonAbout,buttonNews,buttonMenu;
    private static boolean isFragment1=false;
    private Handler _handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0x11:
                        Wifi wifi =(Wifi)msg.obj;
                        new AlertDialog.Builder(Main.this).setTitle("当前密码"+wifi.getKey()).show();
                        Toast.makeText(Main.this,wifi.getKey(),Toast.LENGTH_SHORT).show();
                     }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentLayout = (LinearLayout)findViewById(R.id.linear);
        changeFragment(fragment_Wifi);
        buttonAbout = (Button)findViewById(R.id.button_about);
        buttonWifi = (Button)findViewById(R.id.button_wifi);
        buttonNews = (Button)findViewById(R.id.button_news);
        buttonMenu = (Button)findViewById(R.id.button_menu);
        buttonAbout.setOnClickListener(this);
        buttonWifi.setOnClickListener(this);
        buttonNews.setOnClickListener(this);
        buttonMenu.setOnClickListener(this);
    }

    public void changeFragment(Fragment f){
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.linear,f);
        transaction.commit();
        Log.i("MainActivity","fragment changed");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_wifi:
                changeFragment(fragment_Wifi);


                break;
            case R.id.button_about:
                changeFragment(fragment_About);
                break;
            case R.id.button_news:

                changeFragment(fragment_News);
                break;
            case R.id.button_menu:

                MyDialog.getDialog(Main.this);
                break;
        }


    }
    public AboutFragment getFragmentAbout(){
        return fragment_About;
    }
    public Handler get_handler(){
        return _handler;
    }

    public WifiFragment get_fragment_Wifi2(){
        return _fragment_Wifi2;
    }
    public  void reFresh(){
        if(isFragment1){
            changeFragment(fragment_Wifi);
            isFragment1 = false;
        }else{
            changeFragment(_fragment_Wifi2);
            isFragment1 = true;
        }
    }
}
