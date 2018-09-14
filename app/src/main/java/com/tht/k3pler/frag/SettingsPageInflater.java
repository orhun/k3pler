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
        setValues();
    }
    private void setValues(){
        settextWithUnderline(txvStPort, String.valueOf(getPortSetting()));
        settextWithUnderline(txvStMaxBuffer, txvStMaxBuffer.getText().toString());
        txvStPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(txvStPort.getText().toString(), SQLiteSettings.PORT_DATA);
            }
        });
    }
    private void settextWithUnderline(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    public int getPortSetting(){
        int port = Integer.parseInt(context.getString(R.string.default_port));
        sqliteDBHelper = new SqliteDBHelper(context,
                new SQLiteSettings(context).getWritableDatabase(),
                SQLiteSettings.PORT_DATA, SQLiteSettings.TABLE_NAME);
        try {
            port = Integer.parseInt(sqliteDBHelper.get(SQLiteSettings.PORT_DATA));
        }catch (NumberFormatException e){
            e.printStackTrace();
            sqliteDBHelper.insert(String.valueOf(port));
        }
        sqliteDBHelper.close();
        return port;
    }
    @SuppressWarnings("deprecation")
    private void showEditDialog(final String currentItem, final String DATA){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog);
        builder.setTitle(context.getString(R.string.edit_setting));
        final EditText editText = new EditText(context);
        editText.setText(currentItem);
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
                String item = editText.getText().toString();
                if(item.length() > 3) {
                    sqliteDBHelper = new SqliteDBHelper(context,
                            new SQLiteSettings(context).getWritableDatabase(),
                            DATA, SQLiteSettings.TABLE_NAME);
                    sqliteDBHelper.update(currentItem, item);
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
