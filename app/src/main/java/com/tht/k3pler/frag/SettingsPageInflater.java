package com.tht.k3pler.frag;


import android.content.Context;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tht.k3pler.R;
import com.tht.k3pler.handler.SqliteDBHelper;
import com.tht.k3pler.sub.SQLiteSettings;


public class SettingsPageInflater {
    private Context context;
    private ViewGroup viewGroup;
    private SqliteDBHelper sqliteDBHelper;
    // ** //
    private TextView txvStPort, txvStMaxBuffer;

    public SettingsPageInflater(Context context, ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
    }
    public void init(){
        txvStPort = viewGroup.findViewById(R.id.txvStPort);
        txvStMaxBuffer = viewGroup.findViewById(R.id.txvStMaxBuffer);

        settextWithUnderline(txvStPort, String.valueOf(getPortSetting()));
        settextWithUnderline(txvStMaxBuffer, txvStMaxBuffer.getText().toString());
    }
    private void settextWithUnderline(TextView textView, String text){
        SpannableString content = new SpannableString(textView.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    public int getPortSetting(){
        int port = Integer.parseInt(context.getString(R.string.default_port));
        try {
            sqliteDBHelper = new SqliteDBHelper(context,
                    new SQLiteSettings(context).getWritableDatabase(),
                    SQLiteSettings.PORT_DATA, SQLiteSettings.TABLE_NAME);
            port = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.PORT_DATA));
            sqliteDBHelper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return port;
    }
}
