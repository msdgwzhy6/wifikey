package com.wingsoft.wifikey.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Toast;

import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.fragment.AboutFragment;
import com.wingsoft.wifikey.fragment.NewsFragment;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.thread.ImportThread;

import java.util.Dictionary;

/**
 * Created by wing on 15/7/6.
 */
public class ItemDialog {
    public static void getDialog(final Context context,final Wifi wifi, final AboutFragment fragment,final int i){
        String[] list = { "删除", "备注", "分享到QQ" };
        new AlertDialog.Builder(context)

                .setItems(list,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface arg0, int arg1) {
                                switch (arg1) {
                                    case 0:
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                WifiDB.getWifiDB(context).delete(wifi);
                                                Main main = (Main) context;
                                                main.reFresh();
                                            }
                                        }).start();


                                        break;
                                    case 1:
                                        final Wifi wifi = (Wifi) WifiFragment.get_list().get(i);
                                        final EditText et = new EditText(context);
                                        new AlertDialog.Builder(context).setTitle("请输入备注")
                                                .setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                wifi.setComment(et.getText().toString());
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        WifiDB.getWifiDB(context).comment(wifi);
                                                        Main main = (Main) context;
                                                        main.reFresh();
                                                    }
                                                }).start();
                                            }
                                        }).show();




                                        break;
                                    case 2:
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        Wifi theWifi = (Wifi) WifiFragment.get_list().get(i);
                                        intent.setType("text/plain");
//                                        intent.setPackage("com.tencent.mobileqq");
                                        intent.putExtra(Intent.EXTRA_TEXT, "wifi密码读取器：\n您的朋友给您分享了一条wifi\n网络名："+theWifi.getSsid()+"\n密码："+theWifi.getKey());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(Intent.createChooser(intent, "请选择"));
                                        return;
                                }

                            }
                        }).show();
    }
}
