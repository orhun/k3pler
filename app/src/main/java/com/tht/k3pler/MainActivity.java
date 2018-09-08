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
        checkExtras();
    }
    private void checkExtras() {
        Intent currentIntent = getIntent();
        if (currentIntent != null) {
            try {
                if (currentIntent.getBooleanExtra(getString(R.string.show_gui), false)) {
                    Log.d(getString(R.string.app_name), "Show command received");
                } else if (currentIntent.getBooleanExtra(getString(R.string.proxy_stop), false)) {
                    Log.d(getString(R.string.app_name), "Stop command received");
                    stopProxyService();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void startProxy(){
        stopProxyService();
        try{
            serviceController.startService();
        }catch (Exception e1){
            e1.printStackTrace();
            try {
                serviceController.stopService();
            }catch (Exception e2){
                e2.printStackTrace();
            }
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


