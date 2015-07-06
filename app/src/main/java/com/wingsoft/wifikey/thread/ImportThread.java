package com.wingsoft.wifikey.thread;

import android.content.Context;

import com.wingsoft.wifikey.util.ImportUtils;

/**
 * Created by wing on 15/7/6.
 */
public class ImportThread extends Thread {
    private Context _context;
    public ImportThread(Context context){
        _context = context;
    }
    public void run(){
        Thread importThread = new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    ImportUtils.importWifi(_context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        importThread.start();

    }
}
