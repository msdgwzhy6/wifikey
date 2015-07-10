package com.wingsoft.wifikey.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.enmu.ImportState;
import com.wingsoft.wifikey.enmu.LoginState;
import com.wingsoft.wifikey.enmu.YoumiAd;
import com.wingsoft.wifikey.fragment.AboutFragment;
import com.wingsoft.wifikey.fragment.HelpFragment;
import com.wingsoft.wifikey.fragment.NewsFragment;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.model.User;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.thread.ImportThread;
import com.wingsoft.wifikey.util.ImportUtils;

import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;

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
    private ImageButton mButtonWifi, mButtonNews;
    private SlidingMenu mMenu;
    private TextView mTextViewMenuUser;
    private User mLoginUser = null;
    private boolean isad = false;
    private static boolean isFragment1 = false;

    private Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ImportState.IMPORT_NOW:
                    final Wifi wifi = (Wifi) msg.obj;
                    AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);

                    View view = LayoutInflater.from(Main.this).inflate(R.layout.dialog_wifinow, null);
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();

                    Button confirmButton = (Button) view.findViewById(R.id.button_confirm);
                    Button shareButton = (Button) view.findViewById(R.id.button_share);
                    TextView tv = (TextView)view.findViewById(R.id.text_wifinow);
                    tv.setText("当前密码："+wifi.getKey());
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            return;
                        }
                    });
                    shareButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
//                                        intent.setPackage("com.tencent.mobileqq");
                            intent.putExtra(Intent.EXTRA_TEXT, "wifi密码读取器：\n您的朋友给您分享了一条wifi\n网络名：" + wifi.getSsid() + "\n密码：" + wifi.getKey());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(Intent.createChooser(intent, "请选择"));

                        }
                    });

                    dialog.show();

                    break;
                case ImportState.IMPORT_ERROR:
                    Toast.makeText(Main.this, "导入失败,请检查root权限", Toast.LENGTH_SHORT).show();
                case YoumiAd.ISAD:
                    AdView adView = new AdView(Main.this, AdSize.FIT_SCREEN);
// 获取要嵌入广告条的布局
                    LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
// 将广告条加入到布局中
                    adLayout.addView(adView);
                    isad = true;
                    break;
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentLayout = (LinearLayout) findViewById(R.id.linear);
        changeFragment(mFragment_Wifi);
        mButtonWifi = (ImageButton) findViewById(R.id.button_wifi);
        mButtonNews = (ImageButton) findViewById(R.id.button_news);
        mButtonWifi.setOnClickListener(this);
        mButtonNews.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String value = AdManager.getInstance(Main.this).syncGetOnlineConfig("IsAd", "what");
                if(value.equals("true")){
                    Message m = _handler.obtainMessage();
                    m.what = YoumiAd.ISAD;
                    _handler.sendMessage(m);
                    Log.i("value",value);

                }
            }
        }).start();



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
                mButtonWifi.setImageResource(R.drawable.wifi_checked);
                mButtonNews.setImageResource(R.drawable.news_unchecked);
                changeFragment(mFragment_Wifi);


                break;

            case R.id.button_news:
                mButtonNews.setImageResource(R.drawable.news_checked);
                mButtonWifi.setImageResource(R.drawable.wifi_unchecked);
//               changeFragment(mFragment_News);
                Intent intent = new Intent(Main.this, LoginActivity.class);
                startActivityForResult(intent, LoginState.RESULT);
                break;
        }


    }

    public AboutFragment getFragmentAbout() {
        return mFragment_About;
    }

    public Handler get_handler() {
        return _handler;
    }


    public void reFresh() {
        if (isFragment1) {
            changeFragment(mFragment_Wifi);
            isFragment1 = false;
        } else {
            changeFragment(mFragment_Wifi2);
            isFragment1 = true;
        }

    }

    @Override
    protected void onResume() {
        if (mLoginUser != null) {
            mTextViewMenuUser.setText("当前账号：" + mLoginUser.getmUsername());
            mButtonNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(mFragment_News);
                }
            });
        }
        super.onResume();
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
        mMenu.showMenu();
        mMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);
        mTextViewMenuUser = (TextView) mMenu.findViewById(R.id.text_menu_username);
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

                        ImportThread thread = new ImportThread(Main.this, _handler);
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
            if(isad){
                SpotManager.getInstance(this).showSpotAds(this);
                Log.i("value","开启广告");
            }
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            mMenu.toggle();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();

        }
    }

    public Handler getHandler() {
        return _handler;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case LoginState.RESULT:
                mLoginUser = (User) data.getSerializableExtra("user");
                Log.i("onReuslt", "ss");
                changeFragment(mFragment_News);
                break;
        }
    }

    @Override

    protected void onStop() {

        // 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
        SpotManager.getInstance(this).onStop();
        super.onStop();
    }

    @Override

    protected void onDestroy() {

        SpotManager.getInstance(this).onDestroy();
        super.onDestroy();
    }
}
