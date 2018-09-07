package com.tht.k3pler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import io.netty.handler.codec.http.HttpRequest;

public class MainActivity extends Activity implements ProxyService.Callbacks {
    private ProxyService proxyService;
    private ServiceController serviceController;

    private void init(){
        serviceController = new ServiceController(this, ProxyService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}


