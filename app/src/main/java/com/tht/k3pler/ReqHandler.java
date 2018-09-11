package com.tht.k3pler;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

public class ReqHandler {
    private Context context;
    private Dialog reqDialog;
    public ReqHandler(Context context){
        this.context = context;
    }
    private void initDialog(Dialog dialog){

    }
    public void showDetails(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        reqDialog= new Dialog(context, android.R.style.Theme_Black);
        reqDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        reqDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        reqDialog.setContentView(inflater.inflate(R.layout.layout_main, null));
        initDialog(reqDialog);
        reqDialog.show();
    }

}
