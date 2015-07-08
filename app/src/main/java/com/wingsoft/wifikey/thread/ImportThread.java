package com.wingsoft.wifikey.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wingsoft.wifikey.activity.Main;
import com.wingsoft.wifikey.fragment.WifiFragment;
import com.wingsoft.wifikey.util.ImportUtils;

/**
 * Created by wing on 15/7/6.
 */
public class ImportThread extends Thread {
    private Context _context;
    private Handler _handler;
    public ImportThread(Context context,Handler handler){
        _context = context;
        _handler = handler;
    }
    public void run(){

        try {
            ImportUtils.importWifi(_context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main main = (Main) _context;
        main.reFresh();


    }
}
