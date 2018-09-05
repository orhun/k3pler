package com.tht.k3pler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class MainActivity extends Activity implements ProxyService.Callbacks {
    private HttpProxyServer httpProxyServer;
    private static final int PORT_NUMBER = 8090;
    private static final int MAX_BUFFER = 10 * 1024 * 1024;

    private Intent serviceIntent;
    private ProxyService proxyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startLocalProxy();
        startProxyService();
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(MainActivity.this, "onServiceConnected called", Toast.LENGTH_SHORT).show();
            ProxyService.LocalBinder binder = (ProxyService.LocalBinder) iBinder;
            proxyService = binder.getServiceInstance();
            proxyService.registerClient(MainActivity.this);
            proxyService.onStart();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(MainActivity.this, "onServiceDisconnected called", Toast.LENGTH_SHORT).show();
        }
    };
    private void startProxyService(){
        try {
            serviceIntent = new Intent(MainActivity.this, ProxyService.class);
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
    @Override
    public void updateUi(String data) {
        Toast.makeText(proxyService, data, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopProxyService();
    }

    private void startLocalProxy(){
        httpProxyServer = DefaultHttpProxyServer.bootstrap()
                .withPort(PORT_NUMBER)
                .withFiltersSource(new HttpFiltersSource() {
                    @Override
                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                        Log.d("REQUEST:", originalRequest.getUri());
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
    }
    private static class FilteredResponse extends HttpFiltersAdapter{
        private HttpResponse httpResponse;
        private static HttpResponseStatus httpResponseStatus = HttpResponseStatus.FORBIDDEN;
        private static String blockedList[] = new String[]{"twitter", "google"};
        public FilteredResponse(HttpRequest originalRequest){
            super(originalRequest, null);
        }
        @Override
        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
            Boolean block = false;
            for(String item : blockedList){
                if(originalRequest.getUri().contains(item)){
                    block = true;
                    break;
                }
            }
            if(block)
                return getBlockedResponse();
            return super.clientToProxyRequest(httpObject);
        }
        private HttpResponse getBlockedResponse(){
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus);
            HttpHeaders.setHeader(httpResponse, HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            return httpResponse;
        }
    }

}


