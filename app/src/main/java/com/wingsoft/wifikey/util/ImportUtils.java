package com.wingsoft.wifikey.util;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wingsoft.wifikey.db.WifiDB;
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
            Log.i("开始导出", "创建文件夹"+file);
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
//            Toast.makeText(context, "请检查root权限", Toast.LENGTH_SHORT).show();
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

    public static ArrayList fileToList(Context context)throws Exception{
        BufferedReader reader=null;
        StringBuilder content = new StringBuilder();

        File file = new File("/sdcard/wifikey/wpa_supplicant.conf");
        try {
            FileInputStream in = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(in));

            String line = "";
            String str = "";
            while ((line = reader.readLine()) != null) {
                content.append(line+"\n");
            }
            Parser parser = new Parser(content.toString());
            ArrayList<Wifi> alni = parser.parse();
//            Toast.makeText(context,"String长度"+content.length()+" "+alni.size(),Toast.LENGTH_SHORT).show();
            return alni;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("打开文件出错");
//            Toast.makeText(context, "导入出错", Toast.LENGTH_SHORT).show();
        }finally {
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    return null;
    }
    public static void importWifi(Context context) throws Exception{
        sysToFile(context);
        WifiDB.getWifiDB(context).insertAll(fileToList(context));

    }

}