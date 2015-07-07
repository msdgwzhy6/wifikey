package com.wingsoft.wifikey.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
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
    public static void getDialog(final Context context,final Wifi wifi, final AboutFragment fragment){
        String[] list = { "删除", "备注", "取消" };
        new AlertDialog.Builder(context)
                .setTitle("请选择")
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
                                            }
                                        }).start();
                                        Main main = (Main)context;
                                        main.changeFragment(fragment);

                                        break;
                                    case 1:
                                        Toast.makeText(context,"待开发",Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        return;
                                }

                            }
                        }).show();
    }
}
