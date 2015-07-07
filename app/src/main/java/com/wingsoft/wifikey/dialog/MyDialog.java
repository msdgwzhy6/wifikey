package com.wingsoft.wifikey.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.Toast;

import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.thread.ImportThread;
import com.wingsoft.wifikey.util.ImportUtils;
import com.wingsoft.wifikey.util.NetworkUtils;

import java.util.Dictionary;

/**
 * Created by wing on 15/7/6.
 */
public class MyDialog {
    public static void getDialog(final Context context) {
        String[] list = {"一键导入", "手动添加", "查看当前wifi密码", "取消"};
        new AlertDialog.Builder(context)
                .setTitle("请选择")
                .setItems(list,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface arg0, int arg1) {
                                switch (arg1) {
                                    case 0:

                                        ImportThread thread = new ImportThread(context);
                                        thread.start();

                                        break;
                                    case 1:
                                        final Wifi wifi = new Wifi();
                                        wifi.setKey("从这里录入");
                                        wifi.setSsid("test insert");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                WifiDB.getWifiDB(context).insert(wifi);
                                            }
                                        }).start();
                                        Main main = (Main) context;
                                        main.reFresh();
                                        break;
                                    case 2:
                                        if (NetworkUtils.isWifi(context)) {
                                            NetworkUtils.getNow(context);
                                        } else

                                            Toast.makeText(context, "当前没有连接wifi", Toast.LENGTH_SHORT).show();

                                    case 3:
                                        return;
                                }

                            }
                        }).show();
    }


}
