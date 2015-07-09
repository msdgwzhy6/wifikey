package com.wingsoft.wifikey.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.enmu.ImportState;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.util.ImportUtils;

/**
 * Created by wing on 15/7/8.
 */
public class GetNowThread extends Thread {

    private Context mContext;
    private Handler mHandler;
    private Wifi mWifi;
    public GetNowThread(Context context,Wifi wifi){

        mContext = context;
        mWifi = wifi;
    }
    public void run(){

        try {
            ImportUtils.importWifi(mContext);
            Wifi dbWifi = WifiDB.getWifiDB(mContext).select(mWifi);
            Main main = (Main)mContext;
            Message msg = main.get_handler().obtainMessage();
            msg.obj = dbWifi;
            msg.what = ImportState.IMPORT_NOW;
            main.get_handler().sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main main = (Main) mContext;
        main.reFresh();


    }
}
