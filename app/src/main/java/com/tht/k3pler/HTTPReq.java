package com.tht.k3pler;


public class HTTPReq {
    private String uri, method;
    public HTTPReq(String uri, String method){
        this.uri = uri;
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
