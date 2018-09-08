package com.tht.k3pler;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;


public class MainActivity extends Activity {
    private ServiceController serviceController;

    private void init(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE );
        serviceController = new ServiceController(this, ProxyService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        startProxy();
    }
    private void startProxy(){
        stopProxyService();
        try{
            serviceController.startService();
        }catch (Exception e1){
            e1.printStackTrace();
            stopProxyService();
        }
        finish();
    }
    private void stopProxyService(){
        try{
            serviceController.stopService();
        }catch (Exception e){
           e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {}
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

}


