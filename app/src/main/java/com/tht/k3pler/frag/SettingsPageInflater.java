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
import android.widget.Toast;

import com.tht.k3pler.R;
import com.tht.k3pler.handler.SqliteDBHelper;
import com.tht.k3pler.sub.SQLiteBL;
import com.tht.k3pler.sub.SQLiteSettings;

import java.util.ArrayList;
import java.util.HashMap;


public class SettingsPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    private SqliteDBHelper sqliteDBHelper;
    private String defaultBuffer = "10485760", defaultBufferNum = "10x1024x1024";
    // ** //
    private TextView txvStPort, txvStMaxBuffer;

    public SettingsPageInflater(Context context, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
    }
    public void init(){
        txvStPort = viewGroup.findViewById(R.id.txvStPort);
        txvStMaxBuffer = viewGroup.findViewById(R.id.txvStMaxBuffer);
        setValues();
    }
    private void setValues(){
        ArrayList<String> settings = getSettings();
        settextWithUnderline(txvStPort, settings.get(0));
        if(settings.get(1).equals(defaultBuffer)){
            settextWithUnderline(txvStMaxBuffer, defaultBufferNum);
        }else {
            settextWithUnderline(txvStMaxBuffer, settings.get(1));
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
    }
    private void settextWithUnderline(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }
    public ArrayList<String> getSettings(){
        int port = Integer.parseInt(context.getString(R.string.default_port));
        int buffer = 10 * 1024 * 1024;
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteSettings(context).getWritableDatabase(),
                SQLiteSettings.PORT_DATA, SQLiteSettings.TABLE_NAME);
        try {
            port = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.PORT_DATA));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteSettings(context).getWritableDatabase(),
                SQLiteSettings.BUFFER_DATA, SQLiteSettings.TABLE_NAME);
        try {
            buffer = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.BUFFER_DATA));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        sqliteDBHelper.deleteAll();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(SQLiteSettings.PORT_DATA, String.valueOf(port));
        hashMap.put(SQLiteSettings.BUFFER_DATA, String.valueOf(buffer));
        sqliteDBHelper.insertMultiple(hashMap);
        sqliteDBHelper.close();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(String.valueOf(port));
        arrayList.add(String.valueOf(buffer));
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
}
