package com.tht.k3pler;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import java.util.ArrayList;
import java.util.Collections;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public class ProxyService extends Service {

    public HttpProxyServer httpProxyServer;
    public static final int PORT_NUMBER = 8090;
    private static final int MAX_BUFFER = 10 * 1024 * 1024;
    private NotificationHandler notificationHandler;
    private final IBinder mBinder = new Binder();
    private Intent currentIntent;
    private ArrayList<HTTPReq> httpReqs = new ArrayList<>();
    private Dialog guiDialog;
    private String decoderResult = "";
    // ** //
    private RecyclerView recyclerView;
    private ViewPager viewPager;

    public interface IProxyStatus {
        void onReceive(HttpRequest httpRequest);
        void onNotify(NotificationHandler notificationHandler);
        void onError(Exception e);
    }

    public ProxyService() {}

    @Override
    public IBinder onBind(Intent intent) { return mBinder; }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent currentIntent, int flags, int startId) {
        this.currentIntent = currentIntent;
        this.checkExtras();
        return START_NOT_STICKY;
    }
    private void checkExtras() {
        if (currentIntent != null) {
            try {
                if (currentIntent.getBooleanExtra(getString(R.string.show_gui), false)) {
                    showGuiDialog();
                } else if (currentIntent.getBooleanExtra(getString(R.string.proxy_stop), false)) {
                    stopSelf();
                } else{
                    showGUI();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            showGUI();
        }
    }
    private void initGUI(Dialog dialog){
        viewPager = dialog.findViewById(R.id.viewPager);
        viewPager.setAdapter(new LayoutPagerAdapter(getApplicationContext(), new LayoutPagerAdapter.IViewPager() {
            @Override
            public void onViewsAdded(ArrayList<ViewGroup> layouts) {
                recyclerView = layouts.get(0).findViewById(R.id.recycler_view);
                try {
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                }catch (Exception e){e.printStackTrace();}
            }
        }));
    }
    private void showGUI(){
        try {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            guiDialog = new Dialog(getApplicationContext(), android.R.style.Theme_Black);
            guiDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            guiDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            guiDialog.setContentView(inflater.inflate(R.layout.layout_main, null));
            initGUI(guiDialog);
            guiDialog.show();

            startLocalProxy(new IProxyStatus() {
                @Override
                public void onReceive(HttpRequest httpRequest) {
                    if(httpRequest.getDecoderResult().isSuccess())
                        decoderResult = "S";
                    else if(httpRequest.getDecoderResult().isFinished())
                        decoderResult = "F";
                    else if(httpRequest.getDecoderResult().isFailure())
                        decoderResult = "X";
                    httpReqs.add(new HTTPReq(httpRequest.getUri(), httpRequest.getMethod().name(),
                            httpRequest.getProtocolVersion().text(), decoderResult));
                    ArrayList<HTTPReq> tmpHttpReqs = new ArrayList<>(httpReqs);
                    Collections.reverse(tmpHttpReqs);
                    recyclerView.setAdapter(new RequestAdapter(getApplicationContext(), tmpHttpReqs, new RequestAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(HTTPReq item, int i) {
                            Toast.makeText(ProxyService.this, item.getUri(), Toast.LENGTH_SHORT).show();
                        }
                    }));
                }

                @Override
                public void onNotify(NotificationHandler notificationHandler) {}

                @Override
                public void onError(Exception e) {
                    Log.d(getString(R.string.app_name), e.toString());
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Log.d(getString(R.string.app_name), "GUI start error.");
            stopSelf();
        }
    }
    private void startLocalProxy(final IProxyStatus proxyStatus){
        try {
            httpProxyServer = DefaultHttpProxyServer.bootstrap()
                    .withPort(PORT_NUMBER)
                    .withFiltersSource(new HttpFiltersSource() {
                        @Override
                        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                            try {
                                proxyStatus.onReceive(originalRequest);
                            }catch (Exception e){ e.printStackTrace(); }
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
            if(httpProxyServer != null){
                new ProxyNotifier(getApplicationContext(), httpProxyServer, notificationHandler).execute();
            }else{
                throw new Exception("Failed to start proxy.");
            }
            if(proxyStatus!=null){
                proxyStatus.onNotify(notificationHandler);
            }
        }catch (Exception e){
            if(proxyStatus!=null){
                proxyStatus.onError(e);
            }
            e.printStackTrace();
        }
    }
    private void showGuiDialog(){
        try{
            if(guiDialog != null && !guiDialog.isShowing()){
                guiDialog.show();
            }else if (guiDialog == null){
                cancelNotifications();
                showGUI();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void cancelNotifications(){
        try {
            notificationHandler.getNotificationManager().cancelAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void stopProxy(){
        if(guiDialog != null && guiDialog.isShowing()) {
            guiDialog.cancel();
            guiDialog = null;
        }
        try{
            httpProxyServer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            httpProxyServer.abort();
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
