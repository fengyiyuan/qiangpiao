/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;


/**
 * @author v_wuyunfeng
 *
 */
public class HttpTicketDO {
    private String BIGipServerotn;
    private String JSESSIONID;
    private Boolean isLogin = false;
    private String userName;
    
    private String dynamicKey;
    private String dynamicVal;
    
    private Map<String,String> sendHeaders = new HashMap<String,String>();
    public String getBIGipServerotn() {
        return BIGipServerotn;
    }
    public void setBIGipServerotn(String bIGipServerotn) {
        BIGipServerotn = bIGipServerotn;
    }
    public String getJSESSIONID() {
        return JSESSIONID;
    }
    public void setJSESSIONID(String jSESSIONID) {
        JSESSIONID = jSESSIONID;
    }
    public Boolean getIsLogin() {
        return isLogin;
    }
    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getDynamicKey() {
        return dynamicKey;
    }
    public void setDynamicKey(String dynamicKey) {
        this.dynamicKey = dynamicKey;
    }
    public String getDynamicVal() {
        return dynamicVal;
    }
    public void setDynamicVal(String dynamicVal) {
        this.dynamicVal = dynamicVal;
    }
    public HttpTicketDO(){}
  
    public void setHttpDO(HttpDO httpDO){
        Map<String, List<String>> headerFields = httpDO.getHeaders();
        if(headerFields !=null){
            List<String> list = headerFields.get("Set-Cookie");
            String cookiesStr = JSONArray.toJSONString(list);
            this.BIGipServerotn = cookiesStr.substring(cookiesStr.indexOf("BIGipServerotn=") + "BIGipServerotn=".length(), cookiesStr.indexOf(";",cookiesStr.indexOf("BIGipServerotn=") + "BIGipServerotn=".length()));
            this.JSESSIONID = cookiesStr.substring(cookiesStr.indexOf("JSESSIONID=") + "JSESSIONID=".length(), cookiesStr.indexOf(";",cookiesStr.indexOf("JSESSIONID=") + "JSESSIONID=".length()));
        }
        this.sendHeaders.put("Cookie", "JSESSIONID=" + JSESSIONID + ";BIGipServerotn="+ BIGipServerotn +";current_captcha_type=C");
    }
    
    public Map<String,String> getSendHeaders(){
       return this.sendHeaders;
    }
    
    /**
     * @param cookies
     */
    public Map<String,String> addCookies(String cookies) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("Cookie", cookies);
        this.addHeaders(map);
        return this.sendHeaders;
    }
    
    public Map<String,String> addHeaders(Map<String,String> headers){
        for (Entry<String, String> entry : headers.entrySet()) {
            String val = this.sendHeaders.get(entry.getKey());
            if(val != null && !val.equals("")){
                val += ";" + entry.getValue();
            }else{
                val = entry.getValue();
            }
            this.sendHeaders.put(entry.getKey(),val);
        }
        return this.sendHeaders;
    }
}
