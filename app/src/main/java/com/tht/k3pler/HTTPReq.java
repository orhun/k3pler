package com.tht.k3pler;


public class HTTPReq {
    private String uri, method, protocol, result;
    public HTTPReq(String uri, String method, String protocol, String result){
        this.uri = uri;
        this.method = method;
        this.protocol = protocol;
        this.result = result;
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
}
