package com.wingsoft.wifikey.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.enmu.ImportState;
import com.wingsoft.wifikey.enmu.LoginState;
import com.wingsoft.wifikey.enmu.Urls;
import com.wingsoft.wifikey.enmu.YoumiAd;
import com.wingsoft.wifikey.fragment.AboutFragment;
import com.wingsoft.wifikey.fragment.HelpFragment;
import com.wingsoft.wifikey.fragment.NewsFragment;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.model.User;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.thread.ImportThread;
import com.wingsoft.wifikey.util.DownLoadManager;
import com.wingsoft.wifikey.util.ImportUtils;

import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main extends ActionBarActivity implements View.OnClickListener {
    private static final int DOWNLOAD_ERROR = 0x2142 ;
    private long mExitTime = 0;
    private FragmentManager mFragmentManager;
    private LinearLayout mFragmentLayout;
    private FragmentTransaction mTransaction;
    private boolean mIsNewVersion = true;
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

                    new SweetAlertDialog(Main.this).setTitleText("wifi信息")
                            .setContentText("当前密码:" + wifi.getKey()).setCancelText("确认")
                    .setConfirmText("分享给好友").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
//                                        intent.setPackage("com.tencent.mobileqq");
                            intent.putExtra(Intent.EXTRA_TEXT, "wifi密码读取器：\n您的朋友给您分享了一条wifi\n网络名：" + wifi.getSsid() + "\n密码：" + wifi.getKey());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(Intent.createChooser(intent, "请选择"));

                        }
                    }).show();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
//
//                    View view = LayoutInflater.from(Main.this).inflate(R.layout.dialog_wifinow, null);
//                    builder.setView(view);
//                    final AlertDialog dialog = builder.create();
//
//                    Button confirmButton = (Button) view.findViewById(R.id.button_confirm);
//                    Button shareButton = (Button) view.findViewById(R.id.button_share);
//                    TextView tv = (TextView) view.findViewById(R.id.text_wifinow);
//                    tv.setText("当前密码：" + wifi.getKey());
//                    confirmButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.cancel();
//                            return;
//                        }
//                    });
//                    shareButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    });
//
//                    dialog.show();

                    break;
                case ImportState.IMPORT_ERROR:
                    Toast.makeText(Main.this, "导入失败,请检查root权限", Toast.LENGTH_SHORT).show();
                case YoumiAd.ISAD:
//
//                    LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
//// 将广告条加入到布局中
//                    adLayout.addView(adView);
//                    isad = true;
                    break;
                case DOWNLOAD_ERROR:
                    Toast.makeText(Main.this, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Urls.UPDATEURL,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int version = jsonObject.getInt("versioncode");
                    String url = jsonObject.getString("url");
                    if(version > getPackageManager().getPackageInfo(getPackageName(), 0).versionCode){
                       showUpdataDialog(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(Main.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Main.this, "failed", Toast.LENGTH_SHORT).show();
                        Log.i("wing","Main----->update failed");
                    }
                });
        queue.add(request);
        mFragmentLayout = (LinearLayout) findViewById(R.id.linear);
        changeFragment(mFragment_Wifi);
        mButtonWifi = (ImageButton) findViewById(R.id.button_wifi);
        mButtonNews = (ImageButton) findViewById(R.id.button_news);
        mButtonWifi.setOnClickListener(this);
        mButtonNews.setOnClickListener(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                String value = AdManager.getInstance(Main.this).syncGetOnlineConfig("IsAd", "what");
////                if(value.equals("true")){
////                    Message m = _handler.obtainMessage();
////                    m.what = YoumiAd.ISAD;
////                    _handler.sendMessage(m);
////                    Log.i("value",value);
////
////                }
//            }
//        }).start();


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
            if (isad) {

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


        super.onStop();
    }

    @Override

    protected void onDestroy() {

        super.onDestroy();
    }
    protected void downloadNewAPK(final String url){
        final ProgressDialog pd;
        pd = new ProgressDialog(this);

        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在更新..");
        pd.setCancelable(false);
        pd.show();

        new Thread(){
            @Override
            public void run() {
                try {
                    File file = DownLoadManager.getFileFromServer(url, pd);
                    sleep(3000);
                    installApk(file);
                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = _handler.obtainMessage();
                    msg.what = DOWNLOAD_ERROR;
                    _handler.sendMessage(msg);
                }

            }
        }.start();
    }

    private void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    protected void showUpdataDialog(final String url) {
        mIsNewVersion = false;
        SweetAlertDialog dialog =
                new SweetAlertDialog(this).setTitleText("有新版本").setContentText("检查到新版本!是否更新").setConfirmText("确认")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        downloadNewAPK(url);
                    }
                }).setCancelText("取消").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }
}
