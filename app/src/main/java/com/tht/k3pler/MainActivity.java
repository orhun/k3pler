package com.tht.k3pler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

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
        private static String blockMsg = "Blocked by K3pler.";
        private static HttpResponseStatus blockedRequestStatus = HttpResponseStatus.OK;
        private ByteBuf byteBuf;
        private static String blockedList[] = new String[]{"twitter"};
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
            try{
                byteBuf = Unpooled.wrappedBuffer(blockMsg.getBytes("UTF-8"));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, blockedRequestStatus, byteBuf);
            HttpHeaders.setContentLength(httpResponse, byteBuf.readableBytes());
            HttpHeaders.setHeader(httpResponse, HttpHeaders.Names.CONTENT_TYPE, "text/html");
            return httpResponse;
        }
    }

}


