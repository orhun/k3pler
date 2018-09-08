package com.tht.k3pler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import io.netty.handler.codec.http.HttpRequest;

public class MainActivity extends Activity implements ProxyService.Callbacks {
    private ProxyService proxyService;
    private ServiceController serviceController;

    private void init(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        serviceController = new ServiceController(this, ProxyService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        startProxy();
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
        try{
            serviceController.startServiceWithBind(serviceConnection);
        }catch (Exception e1){
            e1.printStackTrace();
            try {
                serviceController.stopService(serviceConnection);
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }
    private void stopProxyService(){
        try{
            proxyService.cancelNotifications();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            proxyService.httpProxyServer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            serviceController.stopService(serviceConnection);
        }catch (Exception e){
           e.printStackTrace();
        }
        finish();
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ProxyService.LocalBinder binder = (ProxyService.LocalBinder) iBinder;
            proxyService = binder.getServiceInstance();
            proxyService.registerClient(MainActivity.this);
            proxyService.startLocalProxy(new ProxyService.IProxyStatus() {
                @Override
                public void onNotified(NotificationHandler notificationHandler) {
                    checkExtras();
                }
                @Override
                public void onError(Exception e) {
                    Log.d(getString(R.string.app_name), e.getMessage());
                }
            });

        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };
    @Override
    public void onRequest(HttpRequest httpRequest) {
        Log.d("REQUEST:", httpRequest.getUri());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProxyService();
    }
    @Override
    public void onBackPressed() {}
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

}


