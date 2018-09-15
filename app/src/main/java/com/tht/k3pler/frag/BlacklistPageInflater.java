package com.tht.k3pler.frag;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
    private TextView txvBlacklistOptions;
    private SwipeRefreshLayout swpBlacklist;

    public BlacklistPageInflater(Context context, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
    }
    public BlacklistPageInflater(Context context){
        this.context = context;
    }
    public void init(){
        lstBlacklist = viewGroup.findViewById(R.id.lstBlacklist);
        setBlacklistLstView();
        txvBlacklistOptions = viewGroup.findViewById(R.id.txvBlacklistOptions);
        txvBlacklistOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqliteDBHelper = new SqliteDBHelper(context,
                        new SQLiteBL(context).getWritableDatabase(),
                        SQLiteBL.BLACKLIST_DATA, SQLiteBL.TABLE_NAME);
                sqliteDBHelper.deleteAll();
                sqliteDBHelper.close();
                setBlacklistLstView();
            }
        });
        swpBlacklist = viewGroup.findViewById(R.id.swpBlacklist);
        swpBlacklist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setBlacklistLstView();
                        swpBlacklist.setRefreshing(false);
                    }
                }, 1000);
            }
        });
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
                final String[] options = new String[]{context.getString(R.string.remove_blacklist),
                        context.getString(R.string.edit_blaclist_item)};
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
                        }else if(which == 1){
                            showEditDialog(blackListArr.get(i));
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.show();
            }
        });
    }
    @SuppressWarnings("deprecation")
    private void showEditDialog(final String item){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog);
        builder.setTitle(context.getString(R.string.edit_blaclist_item));
        final EditText editText = new EditText(context);
        editText.setText(item);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout parentLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dpToPx(20), 0, dpToPx(20), 0);
        editText.setLayoutParams(layoutParams);
        parentLayout.addView(editText);
        builder.setView(parentLayout);
        builder.setPositiveButton(context.getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newAddr = editText.getText().toString();
                if(newAddr.length() > 3) {
                    sqliteDBHelper = new SqliteDBHelper(context,
                            new SQLiteBL(context).getWritableDatabase(),
                            SQLiteBL.BLACKLIST_DATA, SQLiteBL.TABLE_NAME);
                    sqliteDBHelper.update(item, newAddr);
                    sqliteDBHelper.close();
                    setBlacklistLstView();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
