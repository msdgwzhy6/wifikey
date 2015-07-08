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
    private static WifiDB mWifiDB;
    private SQLiteDatabase db;
    private WifiDB(Context context){
        WifikeyOpenHelper woh = new WifikeyOpenHelper(context,DB_NAME,VERSION);
        db = woh.getWritableDatabase();
    }
    public static WifiDB getWifiDB(Context context) {
        if (mWifiDB == null) {
            mWifiDB = new WifiDB(context);

        }
        return mWifiDB;
    }
    public Wifi select(Wifi wifi){
        Cursor c =db.rawQuery("select * from Wifi where ssid ='" + wifi.getSsid() + "'", null);
        c.moveToFirst();
        Wifi mWifi = new Wifi();
        wifi.setComment(c.getString(c.getColumnIndex("comment")));
        wifi.setKey(c.getString(c.getColumnIndex("key")));
        wifi.setSsid(c.getString(c.getColumnIndex("ssid")));
        return wifi;
    }
    public void delete(Wifi wifi){
        if(wifi!=null){
            db.execSQL("delete from Wifi where ssid ='"+wifi.getSsid()+"'");
        }
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

    public void insertAll(ArrayList<Wifi>list){
        for (int i = 0;i<list.size();i++){
            insert(list.get(i));
        }

    }
    public void comment(Wifi wifi){
        db.execSQL("update Wifi set comment = '"+wifi.getComment()+"' "+"where ssid ='"+wifi.getSsid()+"'");

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
