/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.domain;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author v_wuyunfeng
 *
 */
public class HttpDO {
    private URLConnection connection;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private Map<String,List<String>> headers;
    public URLConnection getConnection() {
        return connection;
    }
    public void setConnection(URLConnection connection) {
        this.connection = connection;
    }
    public ByteArrayOutputStream getBaos() {
        return baos;
    }
    public void setBaos(ByteArrayOutputStream baos) {
        this.baos = baos;
    }
    public Map<String, List<String>> getHeaders() {
        return headers;
    }
    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }
    
    public String getResponseStr(){
        try {
            return new String(baos.toByteArray(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
