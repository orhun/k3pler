package com.tht.k3pler;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ProxyService extends Service {

    private Callbacks callBacks;
    private final IBinder mBinder = new LocalBinder();

    public ProxyService() {}

    @Override
    public IBinder onBind(Intent intent) { return mBinder; }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void onStart(){
        for (int i = 0; i < 10; i++) {
            callBacks.updateUi(String.valueOf(i));
            try {
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class LocalBinder extends Binder {
        public ProxyService getServiceInstance(){
            return ProxyService.this;
        }
    }
    public void registerClient(Activity activity){
        this.callBacks = (Callbacks)activity;
    }
    public interface Callbacks{
        public void updateUi(String data);
    }

}
