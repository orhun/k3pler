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
                        if(originalRequest.getUri().contains("google") || originalRequest.getUri().contains("gstatic")){
                            return new BlockedResponse(originalRequest);
                        }
                        return null;
                        // /return new AnswerRequestFilter(originalRequest, "");
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
    public class AnswerRequestFilter extends HttpFiltersAdapter{
        private final String answer;
        private ByteBuf buffer;
        private HttpResponse response;

        public AnswerRequestFilter(HttpRequest originalRequest, String answer) {
            super(originalRequest, null);
            this.answer = answer;
        }
        @Override
        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
            try {
                buffer = Unpooled.wrappedBuffer(answer.getBytes("UTF-8"));
                response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                //HttpHeaders.setContentLength(response, buffer.readableBytes());
                //HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, "text/html");
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class BlockedResponse extends HttpFiltersAdapter{
        private HttpResponse httpResponse;
        private HttpResponseStatus blockedRequestStatus = HttpResponseStatus.BAD_REQUEST;
        private String blockMsg = "Blocked by K3pler.";
        private ByteBuf byteBuf;
        public BlockedResponse(HttpRequest originalRequest){
            super(originalRequest, null);
        }
        @Override
        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
            try{
                byteBuf = Unpooled.wrappedBuffer(blockMsg.getBytes("UTF-8"));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, blockedRequestStatus, byteBuf);
            HttpHeaders.setContentLength(httpResponse, byteBuf.readableBytes());
            HttpHeaders.setHeader(httpResponse, HttpHeaders.Names.CONTENT_TYPE, "text/html");
            return httpResponse; //super.clientToProxyRequest(httpObject);
        }
    }

}


