package com.k3.k3pler.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** SQLite connections **/
public class SqliteDBHelper {

    private static SQLiteDatabase db;
    private Context context;
    private String column, table;
    public static String SPLIT_CHAR = "~";

    public SqliteDBHelper(Context context, SQLiteDatabase db, String column, String table){
        this.context = context;
        this.column = column;
        this.table = table;
        this.db = db;
    }
    public void setColumn(String column){
        this.column = column;
    }
    public Boolean insert(String requestAddr){
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(column, requestAddr);
            db.insert(table, null, contentValues);
            return true;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public Boolean insertMultiple(HashMap<String, String> hashMap) {
        try {
            ContentValues contentValues = new ContentValues();
            Iterator it = hashMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                contentValues.put(pair.getKey().toString(),
                        pair.getValue().toString());
                it.remove();
            }

            db.insert(table, null, contentValues);
            return true;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String get(String column1) {
        String data = "";
        try {
            String[] columns = {column1};
            Cursor read = db.query(table, columns, null, null, null, null, null);
            while (read.moveToNext()) {
                data = read.getString(read.getColumnIndex(column1));
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return data;
    }
    public String getAll() {
        String allData = "";
        try {
            String[] columns = {column};
            Cursor read = db.query(table, columns, null, null, null, null, null);
            while (read.moveToNext()) {
                allData += read.getString(read.getColumnIndex(column)) + SPLIT_CHAR;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return allData;
    }
    public Boolean update(String requestAddr, String newAddr){
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(column, newAddr);
            db.update(table, contentValues, column + " = ?", new String[]{requestAddr});
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public Boolean delVal(String requestAddr){
        try {
            db.delete(table, column + " = ?", new String[]{requestAddr});
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public Boolean deleteAll(){
        try {
            db.execSQL("DELETE FROM "+ table);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public Boolean close(){
        try{
            db.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
