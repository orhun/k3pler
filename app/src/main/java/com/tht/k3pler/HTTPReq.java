package com.tht.k3pler;


public class HTTPReq {
    private String uri, method, protocol, result, time;
    public HTTPReq(String uri, String method, String protocol,
                   String result, String time){
        this.uri = uri;
        this.method = method;
        this.protocol = protocol;
        this.result = result;
        this.time = time;
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
    public String getResult() {
        return result;
    }

    public String getTime() {
        return time;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setResult(String result) {
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
}
