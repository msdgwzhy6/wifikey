package com.wingsoft.wifikey.util;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wingsoft.wifikey.R;
import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.enmu.ImportState;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.model.Wifi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by wing on 15/7/6.
 */
public class ImportUtils {
    public static void sysToFile(Context context) {
        Process process = null;
        DataOutputStream os = null;
        File file = new File("/sdcard/wifikey");
        Log.i("开始导出", "导出");
        if (!file.exists()) {
            Log.i("开始导出", "创建文件夹" + file);
            file.mkdirs();
        }

        try {

            String cmd = "cat /data/misc/wifi/wpa_supplicant.conf > "
                    + "/sdcard/wifikey/wpa_supplicant.conf";

            process = Runtime.getRuntime().exec("su"); // �л���root�ʺ�
            {
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(cmd + "\n");
                os.writeBytes("exit\n");
                os.flush();

            }
            process.waitFor();
        } catch (Exception e) {

            Log.e("1111111111111111111111", "catch到了111111111111");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {

            }
        }
    }

    public static ArrayList fileToList(Context context) throws Exception {
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();

        File file = new File("/sdcard/wifikey/wpa_supplicant.conf");
        try {
            FileInputStream in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in));

            String line = "";
            String str = "";
            while ((line = reader.readLine()) != null) {
                content.append(line + "\n");
            }
            Parser parser = new Parser(content.toString());
            ArrayList<Wifi> alni = parser.parse();
//            Toast.makeText(context,"String长度"+content.length()+" "+alni.size(),Toast.LENGTH_SHORT).show();
            return alni;

        } catch (IOException e) {
            e.printStackTrace();
            Main main = (Main) context;
            Message msg = main.getHandler().obtainMessage();
            msg.what = ImportState.IMPORT_ERROR;
            main.getHandler().sendMessage(msg);
            System.out.print("打开文件出错");
//            Toast.makeText(context, "导入出错", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void importWifi(Context context) throws Exception {
        sysToFile(context);
        WifiDB.getWifiDB(context).insertAll(fileToList(context));

    }

    public static void add(final Context context) {
        final Wifi wifi = new Wifi();

        final View myView = LayoutInflater.from(context).inflate(R.layout.dialog_add, null);
        new AlertDialog.Builder(context).setTitle("请输入信息").setView(myView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText ssid = (EditText) myView.findViewById(R.id.edit_ssid);
                        EditText key = (EditText) myView.findViewById(R.id.edit_key);
                        if (TextUtils.isEmpty(ssid.getText().toString())) {
                            Toast.makeText(context,"网络名不能为空",Toast.LENGTH_SHORT).show();
                            add(context);
                            return;
                        }
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
    }

    public static void getNow(Context context) {
        if (NetworkUtils.isWifi(context)) {
            NetworkUtils.getNow(context);
        } else

            Toast.makeText(context, "当前没有连接wifi", Toast.LENGTH_SHORT).show();

    }
}