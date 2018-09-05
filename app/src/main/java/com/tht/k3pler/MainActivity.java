package com.tht.k3pler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity implements ProxyService.Callbacks {
    private Intent serviceIntent;
    private ProxyService proxyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(MainActivity.this, ProxyService.class);
        startProxyServiceWithBind();
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
    private void startProxyServiceWithBind(){
        try {
            startService(serviceIntent);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }catch (Exception e){
            e.printStackTrace();
            stopProxyService();
        }
    }
    private void stopProxyService(){
        try {
            unbindService(serviceConnection);
            stopService(serviceIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void startProxy(){
        try {
            stopService(serviceIntent);
            startService(serviceIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void updateUi(String data) {
        Toast.makeText(proxyService, data, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProxyService();
    }



}


