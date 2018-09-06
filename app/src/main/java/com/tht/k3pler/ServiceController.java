package com.tht.k3pler;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class ServiceController {
    private Activity activity;
    private Class service;
    public ServiceController(Activity activity, Class service){
        this.activity = activity;
        this.service = service;
    }
    public Intent getIntent(){
        return new Intent(activity, service);
    }
    public void startServiceWithBind(ServiceConnection serviceConnection) throws Exception {
        activity.startService(getIntent());
        bindService(serviceConnection);
    }
    public void stopService(ServiceConnection serviceConnection) throws Exception {
        unbindService(serviceConnection);
        activity.stopService(getIntent());
    }
    public void bindService(ServiceConnection serviceConnection){
        activity.bindService(getIntent(), serviceConnection, Context.BIND_AUTO_CREATE);
    }
    public void unbindService(ServiceConnection serviceConnection){
        activity.unbindService(serviceConnection);
    }
    public void startProxy() throws Exception{
        activity.stopService(getIntent());
        activity.startService(getIntent());
    }

}
