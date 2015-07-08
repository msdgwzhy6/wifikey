package com.wingsoft.wifikey.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.fragment.AboutFragment;
import com.wingsoft.wifikey.fragment.HelpFragment;
import com.wingsoft.wifikey.fragment.NewsFragment;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.thread.ImportThread;
import com.wingsoft.wifikey.util.ImportUtils;

import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends ActionBarActivity implements View.OnClickListener {
    private long mExitTime = 0;
    private FragmentManager mFragmentManager;
    private LinearLayout mFragmentLayout;
    private FragmentTransaction mTransaction;
    private HelpFragment mHelpFragment = new HelpFragment();
    private WifiFragment mFragment_Wifi = new WifiFragment();
    private WifiFragment mFragment_Wifi2 = new WifiFragment();
    private AboutFragment mFragment_About = new AboutFragment();
    private NewsFragment mFragment_News = new NewsFragment();
    private Button mButtonWifi, mButtonNews;
    private SlidingMenu mMenu;
    private static boolean isFragment1 = false;

    private Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x11:
                    Wifi wifi = (Wifi) msg.obj;
                    new AlertDialog.Builder(Main.this).setTitle("当前密码" + wifi.getKey()).setNegativeButton("确定", null).show();
                    break;
                case 0x111:
                    Toast.makeText(Main.this,"导入失败,请检查root权限",Toast.LENGTH_SHORT).show();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentLayout = (LinearLayout) findViewById(R.id.linear);
        changeFragment(mFragment_Wifi);
        mButtonWifi = (Button) findViewById(R.id.button_wifi);
        mButtonNews = (Button) findViewById(R.id.button_news);

        mButtonWifi.setOnClickListener(this);
        mButtonNews.setOnClickListener(this);

        initMenu();


    }

    public void changeFragment(Fragment f) {
        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.linear, f);
        mTransaction.commit();
        Log.i("MainActivity", "fragment changed");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_wifi:
                changeFragment(mFragment_Wifi);


                break;

            case R.id.button_news:

                changeFragment(mFragment_News);
                break;
        }


    }

    public AboutFragment getFragmentAbout() {
        return mFragment_About;
    }

    public Handler get_handler() {
        return _handler;
    }

//    public WifiFragment get_fragment_Wifi2() {
//        return _fragment_Wifi2;
//    }

    public void reFresh() {
        if (isFragment1) {
            changeFragment(mFragment_Wifi);
            isFragment1 = false;
        } else {
            changeFragment(mFragment_Wifi2);
            isFragment1 = true;
        }

    }

    private void initMenu() {
        String[] list = getResources().getStringArray(R.array.list);
        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT_RIGHT);
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mMenu.setShadowWidthRes(R.dimen.shadow_width);
        mMenu.setShadowDrawable(R.drawable.shadow);
        mMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mMenu.setFadeDegree(0.35f);
        mMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mMenu.setMenu(R.layout.slidingmenu);
        mMenu.setSecondaryMenu(R.layout.slidingmenu_right);
        mMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);

        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.slidingmenu_linear, mFragment_About);
        mTransaction.commit();

        ListView lv = (ListView) mMenu.findViewById(R.id.list_silding);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:

                        ImportThread thread = new ImportThread(Main.this,_handler);
                        mMenu.toggle();
                        thread.start();
                        break;
                    case 1:
                        ImportUtils.add(Main.this);
                        mMenu.toggle();
                        break;
                    case 2:
                        ImportUtils.getNow(Main.this);
                        mMenu.toggle();
                        break;

                    case 3:
                        changeFragment(mHelpFragment);
                        mMenu.toggle();
                        break;
                    case 4:
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            exit();
        return false;
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            mMenu.toggle();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();

        }
    }
    public Handler getHandler(){
        return _handler;
    }
}
