package com.tht.k3pler.handler;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tht.k3pler.R;
import com.tht.k3pler.sub.HTTPReq;

public class RequestDialog {
    private Context context;
    private Dialog reqDialog;
    private HTTPReq httpReq;
    public interface IBtnBlackList{
        void onInit(Button btnReqBlackList, Dialog dialog, String uri);
    }
    // * //
    private TextView txvReqAddr, txvReqMethod, txvReqProtocol, txvReqResult, txvReqTime;
    private Button btnReqBlacklist;

    public RequestDialog(Context context, HTTPReq httpReq){
        this.context = context;
        this.httpReq = httpReq;
    }
    private void initDialog(Dialog dialog){
        txvReqAddr = dialog.findViewById(R.id.txvReqAddr);
        txvReqMethod = dialog.findViewById(R.id.txvReqMethod);
        txvReqProtocol = dialog.findViewById(R.id.txvReqProtocol);
        txvReqResult = dialog.findViewById(R.id.txvReqResult);
        txvReqTime = dialog.findViewById(R.id.txvReqTime);
        btnReqBlacklist = dialog.findViewById(R.id.btnReqBlacklist);
    }
    @SuppressWarnings("deprecation")
    public void show(IBtnBlackList iBtnBlackList){
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            reqDialog = new Dialog(context);
            reqDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            reqDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            reqDialog.setContentView(inflater.inflate(R.layout.layout_req_detail, null));
            initDialog(reqDialog);
            txvReqAddr.setText(httpReq.getUri());
            txvReqMethod.setText(httpReq.getMethod());
            txvReqProtocol.setText(httpReq.getProtocol());
            txvReqResult.setText(httpReq.getResult());
            txvReqTime.setText(httpReq.getTime().replace("{", "").replace("}", ""));
            reqDialog.show();
            iBtnBlackList.onInit(btnReqBlacklist, reqDialog, httpReq.getUri());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
