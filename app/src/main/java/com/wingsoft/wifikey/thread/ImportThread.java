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
    public ImportThread(Context context){
        _context = context;
    }
    public void run(){
        Thread importThread = new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    ImportUtils.importWifi(_context);
                    Main main = (Main) _context;
                    main.reFresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        importThread.start();

    }
}
