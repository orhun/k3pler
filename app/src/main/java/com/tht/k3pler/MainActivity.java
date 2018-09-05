package com.tht.k3pler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.Launcher;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class MainActivity extends AppCompatActivity {
    private HttpProxyServer httpProxyServer;
    private static final int PORT_NUMBER = 8090;
    private static final int MAX_BUFFER = 10 * 1024 * 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLocalProxy();
    }
    private void startLocalProxy(){
        httpProxyServer = DefaultHttpProxyServer.bootstrap()
                .withPort(PORT_NUMBER)
                .withFiltersSource(new HttpFiltersSource() {
                    @Override
                    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                        Log.d("REQUEST:", originalRequest.getUri());
                        return null;
                    }

                    @Override
                    public int getMaximumRequestBufferSizeInBytes() {
                        return MAX_BUFFER;
                    }

                    @Override
                    public int getMaximumResponseBufferSizeInBytes() {
                        return MAX_BUFFER;
                    }
                })
                .start();
    }
}
