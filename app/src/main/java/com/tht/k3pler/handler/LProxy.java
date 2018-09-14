package com.tht.k3pler.handler;


import android.content.Context;
import android.util.Log;

import com.tht.k3pler.frag.BlacklistPageInflater;
import com.tht.k3pler.sub.FilteredResponse;
import com.tht.k3pler.sub.ProxyNotifier;
import com.tht.k3pler.ui.ProxyService;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public class LProxy {
    private Context context;
    private String blacklist;
    private int PORT_NUMBER, MAX_BUFFER;
    public interface IProxyStatus {
        void onReceive(HttpRequest httpRequest, String blacklist);
        void onNotify(NotificationHandler notificationHandler, HttpProxyServer httpProxyServer);
        void onError(Exception e);
    }
    public LProxy(Context context, int PORT_NUMBER, int MAX_BUFFER){
        this.context = context;
        this.PORT_NUMBER = PORT_NUMBER;
        this.MAX_BUFFER = MAX_BUFFER;
    }
    public void start(final IProxyStatus proxyStatus){
        try {
            HttpProxyServer httpProxyServer = DefaultHttpProxyServer.bootstrap()
                    .withPort(PORT_NUMBER)
                    .withFiltersSource(new HttpFiltersSource() {
                        @Override
                        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                            blacklist = new BlacklistPageInflater(context).getBlacklist();
                            try{
                                proxyStatus.onReceive(originalRequest, blacklist);
                            }catch (Exception e){ e.printStackTrace(); }
                            return new FilteredResponse(originalRequest, blacklist);
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
            NotificationHandler notificationHandler = new NotificationHandler(1, context, ProxyService.class);
            if(httpProxyServer != null){
                new ProxyNotifier(context, httpProxyServer, notificationHandler).execute();
            }else{
                throw new Exception("Failed to start proxy.");
            }
            if(proxyStatus!=null){
                proxyStatus.onNotify(notificationHandler, httpProxyServer);
            }
        }catch (Exception e){
            if(proxyStatus!=null){
                proxyStatus.onError(e);
            }
            e.printStackTrace();
        }
    }
}
