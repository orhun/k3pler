package com.tht.k3pler.sub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** Settings database **/
public class SQLiteSettings extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static String DB_NAME = "k3settings.db";
    public static String TABLE_NAME = "k3table";
    public static String PORT_DATA = "PORT", BUFFER_DATA = "MAX_BUFFER",
            MATCH_DATA = "MATCH_TYPE", RESPONSE_DATA = "BLACKLIST_RESPONSE",
            SPLASH_DATA = "SHOW_SPLASH";
    private String CREATE_DB = "create table "+TABLE_NAME+
            "("+ PORT_DATA + " text, " + BUFFER_DATA + " text, "
            + MATCH_DATA + " text, " + RESPONSE_DATA +" text, "+
            SPLASH_DATA +" text);";

    public SQLiteSettings(Context context) {
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
