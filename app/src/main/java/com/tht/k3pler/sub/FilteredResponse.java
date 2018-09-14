package com.tht.k3pler.sub;


import android.util.Log;

import com.tht.k3pler.handler.SqliteDBHelper;

import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class FilteredResponse extends HttpFiltersAdapter {
    private HttpResponse httpResponse;
    private static HttpResponseStatus httpResponseStatus = HttpResponseStatus.BAD_GATEWAY;
    private String blackListArr[], blackList;
    public FilteredResponse(HttpRequest originalRequest,
                            String blackList){
        super(originalRequest, null);
        this.blackList = blackList;
        this.blackListArr = blackList.split("["+SqliteDBHelper.SPLIT_CHAR+"]");
    }
    public FilteredResponse(){
        super(null, null);
    }
    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        if(isBlacklisted(originalRequest.getUri(), blackListArr))
            return getBlockedResponse();
        return super.clientToProxyRequest(httpObject);
    }
    public boolean isBlacklisted(String uri, String[] bl){
        Boolean blocked = false;
        if(bl.length>0) {
            for (String item : bl) {
                if(item != null && item.length()>3) {
                    if (uri.contains(item)) {
                        blocked = true;
                        break;
                    }
                }
            }
        }
        return blocked;
    }
    private HttpResponse getBlockedResponse(){
        httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus);
        HttpHeaders.setHeader(httpResponse, HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        return httpResponse;
    }
}