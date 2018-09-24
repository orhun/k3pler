package com.tht.k3pler.sub;

import io.netty.handler.codec.DecoderResult;

/** HTTP Request implementation **/
public class HTTPReq {
    private String uri, method, protocol, time;
    private DecoderResult result;
    private Boolean blocked;
    public HTTPReq(String uri, String method, String protocol,
                   DecoderResult result, String time, Boolean blocked){
        this.uri = uri;
        this.method = method;
        this.protocol = protocol;
        this.result = result;
        this.time = time;
        this.blocked = blocked;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public String getProtocol() {
        return protocol;
    }

    public DecoderResult getResult() {
        return result;
    }

    public String getTime() {
        return time;
    }

    public Boolean getBlocked() {
        return blocked;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setResult(DecoderResult result) {
        this.result = result;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }
}
