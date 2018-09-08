package com.tht.k3pler;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public class ProxyService extends Service {

    public HttpProxyServer httpProxyServer;
    public static final int PORT_NUMBER = 8090;
    private static final int MAX_BUFFER = 10 * 1024 * 1024;
    private NotificationHandler notificationHandler;
    private final IBinder mBinder = new Binder();
    private Intent currentIntent;

    public interface IProxyStatus {
        void onNotified(NotificationHandler notificationHandler);
        void onError(Exception e);
    }

    public ProxyService() {}

    @Override
    public IBinder onBind(Intent intent) { return mBinder; }

    @Override
    public void onCreate() {
        onStart();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentIntent = intent;
        onStart();
        return START_NOT_STICKY;
    }

    private void onStart(){
        checkExtras();
        showGUI();
        //startLocalProxy(null);
    }

    private void startLocalProxy(IProxyStatus proxyStarted){
        try {
            httpProxyServer = DefaultHttpProxyServer.bootstrap()
                    .withPort(PORT_NUMBER)
                    .withFiltersSource(new HttpFiltersSource() {
                        @Override
                        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                            return new FilteredResponse(originalRequest);
                        }
                        @Override
                        public int getMaximumRequestBufferSizeInBytes() {
                            return MAX_BUFFER;
                        }

                        @Override
                        public int getMaximumResponseBufferSizeInBytes() {
                            return MAX_BUFFER;
                        }
                    }).start();
            notificationHandler = new NotificationHandler(1, getApplicationContext(), ProxyService.class);
            notificationHandler.notify(getString(R.string.app_name), getString(R.string.proxy_running) +
                    " [" + String.valueOf(ProxyService.PORT_NUMBER) + "]", true);
            if(proxyStarted!=null){
                proxyStarted.onNotified(notificationHandler);
            }
        }catch (Exception e){
            if(proxyStarted!=null){
                proxyStarted.onError(e);
            }
            e.printStackTrace();
        }
    }
    private void checkExtras() {
        if (currentIntent != null) {
            try {
                if (currentIntent.getBooleanExtra(getString(R.string.show_gui), false)) {
                    Log.d(getString(R.string.app_name), "Show command received");
                } else if (currentIntent.getBooleanExtra(getString(R.string.proxy_stop), false)) {
                    stopSelf();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void showGUI(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog guiDialog = new Dialog(getApplicationContext(), android.R.style.Theme_Black);
        guiDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        guiDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        guiDialog.setContentView(inflater.inflate(R.layout.layout_main, null));
        guiDialog.show();
    }
    private void cancelNotifications(){
        try {
            notificationHandler.getNotificationManager().cancelAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void stopProxy(){
        try{
            httpProxyServer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelNotifications();
        stopProxy();
    }
}
