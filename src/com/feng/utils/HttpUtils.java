/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.utils;

import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;

import com.feng.domain.HttpDO;
import com.feng.domain.HttpTicketDO;

/**
 * @author v_wuyunfeng
 *
 */
public class HttpUtils {
    private static Logger log = Logger.getLogger(HttpUtils.class); 
   public static HttpDO doGet(String url){
       return doGet(url, null);
   }
   public static HttpDO doPost(String url,String params){
       HttpTicketDO httpTicket = SessionUtils.getHttpTicket();
       Map<String, String> sendHeaders = httpTicket.getSendHeaders();
       return doHttp(url, "post", sendHeaders, params);
   }
   public static HttpDO doPost(String url,String params,Map<String,String> headers){
       HttpTicketDO httpTicket = SessionUtils.getHttpTicket();
       Map<String, String> sendHeaders = httpTicket.getSendHeaders();
       httpTicket.addHeaders(headers);
       return doHttp(url, "post", sendHeaders, params);
   }
   
   public static HttpDO doGet(String url,String params,String cookies){
       HttpTicketDO httpTicket = SessionUtils.getHttpTicket();
       httpTicket.addCookies(cookies);
       return doGet(url,params);
   }
   public static HttpDO doGet(String url,String params){
       HttpTicketDO httpTicket = SessionUtils.getHttpTicket();
       Map<String, String> sendHeaders = httpTicket.getSendHeaders();
       return doHttp(url, "get", sendHeaders, params);
   }
   
   public static HttpDO doHttp(String url,String method,Map<String,String> headers,String params){
       PrintWriter out = null;
       HttpDO httpDO = null;
       try{
           if(url != null ){
               URLConnection httpConn = null;
               url = url.replaceAll("//", "/");
               url = url.replace(":/", "://");
               if(method == null || "get".equals(method.toLowerCase())){
                   if(params != null && !"".equals(params))
                       if(url.indexOf("?") == -1){
                           url += "?" + params;
                       }else{
                           url += "&" + params;
                       }
               }
               URL httpUrl = new URL(url); 
               log.info("==HTTP请求开始:url==="+url);
               httpConn = httpUrl.openConnection();
               httpDO = new HttpDO();
               httpDO.setConnection(httpConn);
               if(url.toLowerCase().startsWith("https://")){
                   ((HttpsURLConnection)httpConn).setSSLSocketFactory(HTTPSUtils.getSSF());
               }
               httpConn.setRequestProperty("accept", "*/*");
               httpConn.setRequestProperty("connection", "Keep-Alive");
               //httpConn.setRequestProperty("Accept-Encoding", "gzip,deflate");
               httpConn.setRequestProperty("Host", "kyfw.12306.cn");
               httpConn.setRequestProperty("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
               httpConn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
               httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
               if(headers != null){
                   for (Entry<String, String> entry : headers.entrySet()) {
                       httpConn.setRequestProperty(entry.getKey(), entry.getValue());
                   }
               }
               if(params != null && !"".equals(params.trim())){
                   if(method != null && "post".equals(method.toLowerCase())){
                       // 发送POST请求必须设置如下两行
                       httpConn.setDoOutput(true);
                       httpConn.setDoInput(true);
                       // 获取URLConnection对象对应的输出流
                       out = new PrintWriter(httpConn.getOutputStream());
                       // 发送请求参数
                       out.print(params);
                       // flush输出流的缓冲
                       out.flush();
                   }
               }
               httpConn.connect();
               httpDO.setHeaders(httpConn.getHeaderFields());
               FileCopyUtils.copy(httpConn.getInputStream(), httpDO.getBaos());
               log.info("==============HTTP请求结束===="+url+"=================");
           }
       }catch(Exception e){
           e.printStackTrace();
       }finally{
           if(out != null){
               out.close();
           }
       }
       return httpDO;
   }
    
}
