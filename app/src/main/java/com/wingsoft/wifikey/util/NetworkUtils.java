package com.wingsoft.wifikey.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.thread.GetNowThread;
import com.wingsoft.wifikey.thread.ImportThread;

/**
 * Created by wing on 15/7/7.
 */
public class NetworkUtils {
    public static String _wifi ="";
    public static boolean isWifi(Context context) {

        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (wifi.toString().equals("DISCONNECTED"))
            return false;
        return true;
    }
    public static String getNow(final Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);// ��ȡ��ǰSSID
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid =wifiInfo.getSSID();
        ssid= ssid.replace("\"", "");
        final Wifi wifi = new Wifi();
        wifi.setSsid(ssid);
        new GetNowThread(context,wifi).start();
        return _wifi;
    }
    public static void setWifi(Wifi wifi){
        _wifi = wifi.getSsid()+wifi.getKey();
        }
}
