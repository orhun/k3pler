package com.tht.k3pler.frag;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tht.k3pler.R;
import com.tht.k3pler.handler.SqliteDBHelper;
import com.tht.k3pler.sub.SQLiteSettings;

import java.util.ArrayList;
import java.util.HashMap;


public class SettingsPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    private SqliteDBHelper sqliteDBHelper;
    private String defaultBuffer = "10485760", defaultBufferNum = "10x1024x1024";
    private String[] respOptions, matchOptions, splashOptions;
    // ** //
    private TextView txvStPort, txvStMaxBuffer, txvStMatchType, txvStBlResponse, txvStShowSplash, txvStRestart;

    public SettingsPageInflater(Context context, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
    }
    public void init(){
        txvStPort = viewGroup.findViewById(R.id.txvStPort);
        txvStMaxBuffer = viewGroup.findViewById(R.id.txvStMaxBuffer);
        txvStMatchType = viewGroup.findViewById(R.id.txvStMatchType);
        txvStBlResponse = viewGroup.findViewById(R.id.txvStBlResponse);
        txvStShowSplash = viewGroup.findViewById(R.id.txvStShowSplash);
        txvStRestart = viewGroup.findViewById(R.id.txvStRestart);
        respOptions = new String[]{
                context.getString(R.string.resp_status_1),
                context.getString(R.string.resp_status_2),
                context.getString(R.string.resp_status_3),
                context.getString(R.string.resp_status_4),};
        matchOptions = new String[]{
                context.getString(R.string.match_type_1),
                context.getString(R.string.match_type_2)};
        splashOptions = new String[]{
                context.getString(R.string.option_y),
                context.getString(R.string.option_n)
        };
        setValues();
    }
    private void setValues(){
        ArrayList<String> settings = getSettings();
        setTextWithUnderline(txvStPort, settings.get(0));
        if(settings.get(1).equals(defaultBuffer)){
            setTextWithUnderline(txvStMaxBuffer, defaultBufferNum);
        }else {
            setTextWithUnderline(txvStMaxBuffer, settings.get(1));
        }
        if(Integer.valueOf(settings.get(2))==0){
            setTextWithUnderline(txvStMatchType, context.getString(R.string.match_type_1));
        }else{
            setTextWithUnderline(txvStMatchType, context.getString(R.string.match_type_2));
        }
        setTextWithUnderline(txvStBlResponse, getRespText(Integer.valueOf(settings.get(3))));
        if(Integer.valueOf(settings.get(4))==0){
            setTextWithUnderline(txvStShowSplash, context.getString(R.string.option_y));
        }else{
            setTextWithUnderline(txvStShowSplash, context.getString(R.string.option_n));
        }
        txvStPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(context.getString(R.string.lyt_port_addr).replace(":", ""),
                        txvStPort.getText().toString(), SQLiteSettings.PORT_DATA);
            }
        });
        txvStMaxBuffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(context.getString(R.string.lyt_max_buffer).replace(":", ""),
                        txvStMaxBuffer.getText().toString(), SQLiteSettings.BUFFER_DATA);
            }
        });
        txvStMatchType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialog(context.getString(R.string.lyt_match_type).replace(":", ""),
                        txvStMatchType.getText().toString(), SQLiteSettings.MATCH_DATA, matchOptions);
            }
        });
        txvStBlResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialog(context.getString(R.string.lyt_blacklist_response).replace(":", ""),
                        txvStBlResponse.getText().toString(), SQLiteSettings.RESPONSE_DATA, respOptions);
            }
        });
        txvStShowSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialog(context.getString(R.string.lyt_show_splash).replace(":", ""),
                        txvStShowSplash.getText().toString(), SQLiteSettings.SPLASH_DATA, splashOptions);
            }
        });
    }
    private void setTextWithUnderline(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }
    public ArrayList<String> getSettings(){
        Boolean notFound = false;
        int port = Integer.parseInt(context.getString(R.string.default_port));
        int buffer = 10 * 1024 * 1024;
        int match =  Integer.parseInt(context.getString(R.string.default_matchtype));
        int resp = 0;
        int splash = 0;
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteSettings(context).getWritableDatabase(),
                SQLiteSettings.PORT_DATA, SQLiteSettings.TABLE_NAME);
        try {
            port = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.PORT_DATA));
        }catch (NumberFormatException e){
            e.printStackTrace();
            notFound = true;
        }
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteSettings(context).getWritableDatabase(),
                SQLiteSettings.BUFFER_DATA, SQLiteSettings.TABLE_NAME);
        try {
            buffer = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.BUFFER_DATA));
        }catch (NumberFormatException e){
            e.printStackTrace();
            notFound = true;
        }
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteSettings(context).getWritableDatabase(),
                SQLiteSettings.MATCH_DATA, SQLiteSettings.TABLE_NAME);
        try {
            match = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.MATCH_DATA));
        }catch (NumberFormatException e){
            e.printStackTrace();
            notFound = true;
        }
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteSettings(context).getWritableDatabase(),
                SQLiteSettings.RESPONSE_DATA, SQLiteSettings.TABLE_NAME);
        try {
            resp = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.RESPONSE_DATA));
        }catch (NumberFormatException e){
            e.printStackTrace();
            notFound = true;
        }
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteSettings(context).getWritableDatabase(),
                SQLiteSettings.SPLASH_DATA, SQLiteSettings.TABLE_NAME);
        try {
            splash = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.SPLASH_DATA));
        }catch (NumberFormatException e){
            e.printStackTrace();
            notFound = true;
        }
        if(notFound){
            sqliteDBHelper.deleteAll();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(SQLiteSettings.PORT_DATA, String.valueOf(port));
            hashMap.put(SQLiteSettings.BUFFER_DATA, String.valueOf(buffer));
            hashMap.put(SQLiteSettings.MATCH_DATA, String.valueOf(match));
            hashMap.put(SQLiteSettings.RESPONSE_DATA, String.valueOf(resp));
            hashMap.put(SQLiteSettings.SPLASH_DATA, String.valueOf(splash));
            sqliteDBHelper.insertMultiple(hashMap);
        }
        sqliteDBHelper.close();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(String.valueOf(port));
        arrayList.add(String.valueOf(buffer));
        arrayList.add(String.valueOf(match));
        arrayList.add(String.valueOf(resp));
        arrayList.add(String.valueOf(splash));
        return arrayList;
    }

    @SuppressWarnings("deprecation")
    private void showEditDialog(String title, String currentItem, final String DATA){
        if(currentItem.equals(defaultBufferNum)){currentItem=defaultBuffer;}
        final String currentItem1 = currentItem;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog);
        builder.setTitle(title);
        final EditText editText = new EditText(context);
        editText.setText(currentItem);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout parentLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(dpToPx(20), dpToPx(10), dpToPx(20), 0);
        editText.setLayoutParams(layoutParams);
        parentLayout.addView(editText);
        builder.setView(parentLayout);
        builder.setPositiveButton(context.getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = editText.getText().toString();
                if(item.length() > 3) {
                    sqliteDBHelper = new SqliteDBHelper(context,
                            new SQLiteSettings(context).getWritableDatabase(),
                            DATA, SQLiteSettings.TABLE_NAME);
                    sqliteDBHelper.update(currentItem1, item);
                    sqliteDBHelper.close();
                    setValues();
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
    @SuppressWarnings("deprecation")
    private void showSelectDialog(String title, String currentItem, final String DATA, String[] options){
        if(DATA.equals(SQLiteSettings.MATCH_DATA)){
            if(currentItem.equals(context.getString(R.string.match_type_1))){currentItem="0";}else{currentItem="1";}
        }else if(DATA.equals(SQLiteSettings.RESPONSE_DATA)){
            currentItem = String.valueOf(getRespID(currentItem));
        }else if(DATA.equals(SQLiteSettings.SPLASH_DATA)){
            if(currentItem.equals(context.getString(R.string.option_y))){currentItem="0";}else{currentItem="1";}
        }
        final String currentItem1 = currentItem;
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog);
        builder.setTitle(title);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sqliteDBHelper = new SqliteDBHelper(context,
                        new SQLiteSettings(context).getWritableDatabase(),
                        DATA, SQLiteSettings.TABLE_NAME);
                sqliteDBHelper.update(currentItem1, String.valueOf(which));
                sqliteDBHelper.close();
                setValues();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
    }
    private int getRespID(String resp){
        if(resp.equals(context.getString(R.string.resp_status_1)))
            return 0;
        else if(resp.equals(context.getString(R.string.resp_status_2)))
            return 1;
        else if(resp.equals(context.getString(R.string.resp_status_3)))
            return 2;
        else if(resp.equals(context.getString(R.string.resp_status_4)))
            return 3;
        else
            return -1;
    }
    private String getRespText(int ID){
        switch (ID){
            case 0:
                return context.getString(R.string.resp_status_1);
            case 1:
                return context.getString(R.string.resp_status_2);
            case 2:
                return context.getString(R.string.resp_status_3);
            case 3:
                return context.getString(R.string.resp_status_4);
            default:
                return "0";
        }
    }

}
