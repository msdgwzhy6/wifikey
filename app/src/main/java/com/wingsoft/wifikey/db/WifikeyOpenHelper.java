package com.wingsoft.wifikey.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wing on 15/7/6.
 */
public class WifikeyOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE ="create table Wifi("
                                        +"id integer primary key autoincrement,"
                                        +"ssid text unique,"
                                        +"key text,"
                                        +"comment text)";
    public WifikeyOpenHelper(Context context, String name,int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
