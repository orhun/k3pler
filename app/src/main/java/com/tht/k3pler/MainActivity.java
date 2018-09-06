package com.tht.k3pler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import io.netty.handler.codec.http.HttpRequest;

public class MainActivity extends Activity implements ProxyService.Callbacks {
    private ProxyService proxyService;
    private ServiceController serviceController;

    private void init(){
        serviceController = new ServiceController(MainActivity.this, ProxyService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        startProxy();
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
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ProxyService.LocalBinder binder = (ProxyService.LocalBinder) iBinder;
            proxyService = binder.getServiceInstance();
            proxyService.registerClient(MainActivity.this);
            proxyService.startLocalProxy();
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
        try {
            serviceController.stopService(serviceConnection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


