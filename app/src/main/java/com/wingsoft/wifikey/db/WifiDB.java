package com.wingsoft.wifikey.db;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wingsoft.wifikey.model.Wifi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing on 15/7/6.
 */
public class WifiDB {
    public static final String DB_NAME = "wifi";
    public static final int VERSION = 1;
    private static WifiDB wifiDB;
    private SQLiteDatabase db;
    private WifiDB(Context context){
        WifikeyOpenHelper woh = new WifikeyOpenHelper(context,DB_NAME,VERSION);
        db = woh.getWritableDatabase();
    }
    public static WifiDB getWifiDB(Context context) {
        if (wifiDB == null) {
            wifiDB = new WifiDB(context);

        }
        return wifiDB;
    }
    public void insert(Wifi wifi){
        if(wifi!=null){
            ContentValues cv = new ContentValues();
            cv.put("ssid",wifi.getSsid());
            cv.put("key",wifi.getKey());
            cv.put("comment",wifi.getComment());
            db.insert("Wifi",null,cv);
        }
    }
    public ArrayList<Wifi> fetchAllData(){
        ArrayList<Wifi>list = new ArrayList<Wifi>();
        Cursor cursor = db.rawQuery("select * from " + DB_NAME, null);
        if(cursor.moveToFirst()){
            do{
                Wifi wifi = new Wifi();
                wifi.setSsid(cursor.getString(cursor.getColumnIndex("ssid")));
                wifi.setKey(cursor.getString(cursor.getColumnIndex("key")));
                wifi.setComment(cursor.getString(cursor.getColumnIndex("comment")));
                list.add(wifi);
            }while (cursor.moveToNext());
        }
        return list;
    }

}
