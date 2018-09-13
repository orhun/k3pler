package com.tht.k3pler.frag;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tht.k3pler.R;
import com.tht.k3pler.adapter.BlacklistAdapter;
import com.tht.k3pler.handler.SqliteDBHelper;
import com.tht.k3pler.sub.SQLiteBL;

import java.util.ArrayList;

public class BlacklistPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    private SqliteDBHelper sqliteDBHelper;
    private ArrayList<String> blackListArr = new ArrayList<>();
    private BlacklistAdapter blacklistAdapter;
    // * //
    private ListView lstBlacklist;

    public BlacklistPageInflater(Context context, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
    }
    public void init(){
        lstBlacklist = viewGroup.findViewById(R.id.lstBlacklist);
        setBlacklistLstView();
    }
    public String getBlacklist(){
        String blackList = "";
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteBL(context).getWritableDatabase(),
                SQLiteBL.BLACKLIST_DATA, SQLiteBL.TABLE_NAME);
        blackList = sqliteDBHelper.getAll();
        sqliteDBHelper.close();
        return blackList;
    }
    @SuppressWarnings("deprecation")
    public void setBlacklistLstView() {
        String[] blackList = getBlacklist().split("["+SqliteDBHelper.SPLIT_CHAR+"]");
        blackListArr.clear();
        for (String item : blackList) {
            if (item.length() > 3) {
                blackListArr.add(item);
            }
        }
        blacklistAdapter = new BlacklistAdapter(context, blackListArr);
        lstBlacklist.setAdapter(blacklistAdapter);
        lstBlacklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final String[] options = new String[]{context.getString(R.string.remove_blacklist)};
                AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog);
                builder.setTitle(blackListArr.get(i));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            sqliteDBHelper = new SqliteDBHelper(context,
                                    new SQLiteBL(context).getWritableDatabase(),
                                    SQLiteBL.BLACKLIST_DATA, SQLiteBL.TABLE_NAME);
                            sqliteDBHelper.delVal(blackListArr.get(i));
                            sqliteDBHelper.close();
                            setBlacklistLstView();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.show();
            }
        });
    }
}
