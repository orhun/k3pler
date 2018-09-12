package com.tht.k3pler;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import io.netty.handler.codec.http.HttpRequest;

public class RequestDialog {
    private Context context;
    private Dialog reqDialog;
    private HTTPReq httpReq;

    public RequestDialog(Context context, HTTPReq httpReq){
        this.context = context;
        this.httpReq = httpReq;
    }
    private void initDialog(Dialog dialog){

    }
    @SuppressWarnings("deprecation")
    public void show(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        reqDialog= new Dialog(context, android.R.style.Theme_Black);
        reqDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        reqDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        reqDialog.setContentView(inflater.inflate(R.layout.layout_req_detail, null));
        initDialog(reqDialog);
        reqDialog.show();
    }

}
