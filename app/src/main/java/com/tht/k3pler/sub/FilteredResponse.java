package com.tht.k3pler.sub;


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
    private HttpResponseStatus httpResponseStatus = HttpResponseStatus.BAD_GATEWAY;
    private String blackListArr[], blackList;
    private int matchType = 0;
    public FilteredResponse(HttpRequest originalRequest,
                            String blackList, int matchType,
                            HttpResponseStatus httpResponseStatus){
        super(originalRequest, null);
        this.blackList = blackList;
        this.blackListArr = blackList.split("["+SqliteDBHelper.SPLIT_CHAR+"]");
        this.matchType = matchType;
        this.httpResponseStatus = httpResponseStatus;
    }
    public FilteredResponse(int matchType){
        super(null, null);
        this.matchType = matchType;
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
                    if(matchType == 0){
                        if(uri.equals(item)){
                            blocked = true;
                            break;
                        }
                    }else {
                        if (uri.contains(item)) {
                            blocked = true;
                            break;
                        }
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