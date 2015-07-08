package com.wingsoft.wifikey.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.db.WifiDB;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.model.Wifi;
import com.wingsoft.wifikey.util.ImportUtils;

/**
 * Created by wing on 15/7/8.
 */
public class GetNowThread extends Thread {

    private Context _context;
    private Handler _handler;
    private Wifi _wifi;
    public GetNowThread(Context context,Wifi wifi){

        _context = context;
        _wifi = wifi;
    }
    public void run(){

        try {
            ImportUtils.importWifi(_context);
            Wifi dbWifi = WifiDB.getWifiDB(_context).select(_wifi);
            Main main = (Main)_context;
            Message msg = main.get_handler().obtainMessage();
            msg.obj = dbWifi;
            msg.what = 0x11;
            main.get_handler().sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main main = (Main) _context;
        main.reFresh();


    }
}
