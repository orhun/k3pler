package com.tht.k3pler;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

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
        startLocalProxy(null);
    }

    public void startLocalProxy(IProxyStatus proxyStarted){
        try {
            httpProxyServer = DefaultHttpProxyServer.bootstrap()
                    .withPort(PORT_NUMBER)
                    .withFiltersSource(new HttpFiltersSource() {
                        @Override
                        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                            callBacks.onRequest(originalRequest);
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
            notificationHandler = new NotificationHandler(1, getApplicationContext(), MainActivity.class);
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
    public void cancelNotifications(){
        notificationHandler.getNotificationManager().cancelAll();
    }

    public class LocalBinder extends Binder {
        public ProxyService getServiceInstance(){
            return ProxyService.this;
        }
    }
    public void registerClient(Activity activity){
        this.callBacks = (Callbacks)activity;
    }
    public interface Callbacks {
        void onRequest(HttpRequest httpRequest);
    }
    public interface IProxyStatus {
        void onNotified(NotificationHandler notificationHandler);
        void onError(Exception e);
    }

}
