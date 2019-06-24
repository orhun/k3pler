package com.k3.k3pler.handler;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.k3.k3pler.R;
import com.k3.k3pler.adapter.RequestAdapter;
import com.k3.k3pler.sub.HTTPReq;

/** Show detailed info about request **/
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
    public RequestDialog(){}
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
            reqDialog.getWindow().setType(getWindowType());
            reqDialog.setContentView(inflater.inflate(R.layout.layout_req_detail, null));
            initDialog(reqDialog);
            txvReqAddr.setText(httpReq.getUri());
            txvReqAddr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    copyToClipBoard(txvReqAddr.getText().toString());
                    reqDialog.dismiss();
                }
            });
            txvReqMethod.setText(httpReq.getMethod());
            txvReqProtocol.setText(httpReq.getProtocol());
            txvReqResult.setText(RequestAdapter.getLongResult(httpReq.getResult()));
            txvReqTime.setText(httpReq.getTime().replace("{", "").replace("}", ""));
            reqDialog.show();
            iBtnBlackList.onInit(btnReqBlacklist, reqDialog, httpReq.getUri());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void copyToClipBoard(String data){
        try{
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(context.getString(R.string.app_name), data);
            clipboard.setPrimaryClip(clip);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressWarnings("deprecation")
    public int getWindowType(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            return WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
    }
}
