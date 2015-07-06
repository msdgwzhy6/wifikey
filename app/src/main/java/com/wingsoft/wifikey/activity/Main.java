package com.wingsoft.wifikey.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.fragment.AboutFragment;
import com.wingsoft.wifikey.fragment.WifiFragment;

public class Main extends ActionBarActivity implements View.OnClickListener {
    private LinearLayout fragmentLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private WifiFragment fragment_Wifi = new WifiFragment();
    private AboutFragment fragment_About = new AboutFragment();
    private RadioButton buttonWifi,buttonAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentLayout = (LinearLayout)findViewById(R.id.linear);
        changeFragment(fragment_Wifi);
        buttonAbout = (RadioButton)findViewById(R.id.button_about);
        buttonWifi = (RadioButton)findViewById(R.id.button_wifi);
        buttonAbout.setOnClickListener(this);
        buttonWifi.setOnClickListener(this);


    }

    private void changeFragment(Fragment f){
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
        }
    }
}
