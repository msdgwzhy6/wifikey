package com.wingsoft.wifikey.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.thread.ImportThread;
import com.wingsoft.wifikey.util.NetworkUtils;


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

                                        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_add,null);
                                        new AlertDialog.Builder(context).setTitle("请输入信息").setView(view)
                                                .setPositiveButton("确认",new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        EditText ssid = (EditText)view.findViewById(R.id.edit_ssid);
                                                        EditText key = (EditText)view.findViewById(R.id.edit_key);
                                                        wifi.setSsid(ssid.getText().toString());
                                                        wifi.setKey(key.getText().toString());
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                WifiDB.getWifiDB(context).insert(wifi);
                                                                Main main = (Main) context;
                                                                main.reFresh();
                                                            }
                                                        }).start();
                                                    }
                                                }).show();


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
