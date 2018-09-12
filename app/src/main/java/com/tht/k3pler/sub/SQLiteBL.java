package com.tht.k3pler.sub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteBL extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static String DB_NAME = "k3blacklist.db";
    public static String TABLE_NAME = "k3table";
    public static String BLACKLIST_DATA = "BLACKLIST";
    private String CREATE_DB = "create table "+TABLE_NAME+
            "("+BLACKLIST_DATA+" text);";

    public SQLiteBL(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        try {
           db.execSQL(CREATE_DB);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }catch(Exception e){e.printStackTrace();}
    }
}
