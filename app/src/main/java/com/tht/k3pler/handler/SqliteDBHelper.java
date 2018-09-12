package com.tht.k3pler.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tht.k3pler.sub.SqliteDB;

public class SqliteDBHelper {

    private Context context;
    private SqliteDB sqliteDB;
    private static SQLiteDatabase db;

    public SqliteDBHelper(Context context){
        this.context = context;
        sqliteDB = new SqliteDB(context);
    }

    public Boolean insert(String requestAddr){
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(sqliteDB.BLACKLIST_DATA, requestAddr);
            db = sqliteDB.getWritableDatabase();
            db.insert(sqliteDB.TABLE_NAME, null, contentValues);
            db.close();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getAll(String userName)
    {
        String allData = "";
        try {
            db = sqliteDB.getWritableDatabase();
            String[] column = {sqliteDB.BLACKLIST_DATA};
            Cursor read = db.query(sqliteDB.TABLE_NAME, column, null, null, null, null, null);
            while (read.moveToNext()) {
                allData += read.getString(read.getColumnIndex(sqliteDB.BLACKLIST_DATA)) + "~";
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return allData;
    }
    public Boolean update(String requestAddr){
        try {
            db = sqliteDB.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(sqliteDB.BLACKLIST_DATA, requestAddr);
            db.update(sqliteDB.TABLE_NAME, contentValues, sqliteDB.BLACKLIST_DATA + " = ?", new String[]{requestAddr});
            db.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
