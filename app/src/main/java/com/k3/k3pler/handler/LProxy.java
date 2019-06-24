package com.k3.k3pler.handler;


import android.content.Context;

import com.k3.k3pler.frag.BlacklistPageInflater;
import com.k3.k3pler.sub.FilteredResponse;
import com.k3.k3pler.sub.ProxyNotifier;
import com.k3.k3pler.ui.ProxyService;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/** Start LittleProxy with parameters **/
public class LProxy {
    private Context context;
    private String blacklist;
    private int PORT_NUMBER, MAX_BUFFER, MATCH_TYPE, RESPONSE_STATUS;
    public interface IProxyStatus {
        void onReceive(HttpRequest httpRequest, String blacklist);
        void onNotify(NotificationHandler notificationHandler, HttpProxyServer httpProxyServer);
        void onError(Exception e);
    }
    public LProxy(Context context, int PORT_NUMBER, int MAX_BUFFER, int MATCH_TYPE, int RESPONSE_STATUS){
        this.context = context;
        this.PORT_NUMBER = PORT_NUMBER;
        this.MAX_BUFFER = MAX_BUFFER;
        this.MATCH_TYPE = MATCH_TYPE;
        this.RESPONSE_STATUS = RESPONSE_STATUS;
    }
    public void start(final IProxyStatus proxyStatus){
        try {
            HttpProxyServer httpProxyServer = DefaultHttpProxyServer.bootstrap()
                    .withPort(PORT_NUMBER)
                    .withFiltersSource(new HttpFiltersSource() {
                        @Override
                        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                            try {
                                blacklist = new BlacklistPageInflater(context).getBlacklist();
                            }catch (Exception e){e.printStackTrace();}
                            try{
                                proxyStatus.onReceive(originalRequest, blacklist);
                            }catch (Exception e){ e.printStackTrace(); }
                            return new FilteredResponse(originalRequest, blacklist, MATCH_TYPE, getResponseStatus(RESPONSE_STATUS));
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
    private HttpResponseStatus getResponseStatus(int ID){
        switch (ID){
            case 0:
                return HttpResponseStatus.BAD_GATEWAY;
            case 1:
                return HttpResponseStatus.BAD_REQUEST;
            case 2:
                return HttpResponseStatus.FORBIDDEN;
            case 3:
                return HttpResponseStatus.NOT_FOUND;
            default:
                return HttpResponseStatus.BAD_GATEWAY;
        }
    }
}
