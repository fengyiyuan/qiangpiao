/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.service;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feng.domain.HttpDO;
import com.feng.domain.HttpTicketDO;
import com.feng.domain.QueryTicketDO;
import com.feng.utils.CommonUtils;
import com.feng.utils.HttpUtils;
import com.feng.utils.PropUtils;
import com.feng.utils.SessionUtils;

/**
 * @author v_wuyunfeng
 *
 */
@Service("ticketService")
public class TicketService {
    private Logger log = Logger.getLogger(TicketService.class); 

    /**
     * @param username
     * @param password
     * @param code
     * @param webRoot 
     * @throws Exception 
     */
    public void login(String username, String password, String code, String webRoot) throws Exception {
        HttpDO httpDO = null;
        String loginUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.url.loginInit");
        httpDO = HttpUtils.doGet(loginUrl);
        // 读取服务器的响应内容并显示 
       String str = httpDO.getResponseStr();
       int mid = str.indexOf("dynamicJs");
       String dynamicJsUrl = str.substring(str.substring(0, mid).lastIndexOf("\"") + 1,str.substring(mid).indexOf("\"")+mid);
       httpDO = HttpUtils.doGet(PropUtils.getProp("ticket.url") + dynamicJsUrl);
       String dynamicJs = httpDO.getResponseStr();
       String key = dynamicJs.substring(dynamicJs.indexOf("gc(){var key='") + "gc(){var key='".length(), dynamicJs.indexOf("'",dynamicJs.indexOf("gc(){var key='") + "gc(){var key='".length()));
       String jsStr = new String(FileCopyUtils.copyToByteArray(new File(webRoot+ "/ticketFiles/12306.js")));
       String value = CommonUtils.runSecretKeyValueMethod(key, jsStr);
       String params = PropUtils.getProp("ticket.login.username") + "=" + username + "&" +
                       PropUtils.getProp("ticket.login.password") + "=" + password + "&" +
                       PropUtils.getProp("ticket.login.code") + "=" + code + "&" +
                       key + "=" + value + "&" +
                      "myversion=undefined&randCode_validate=";
       loginUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.url.login");
       httpDO = HttpUtils.doPost(loginUrl, params);
       log.info("=======================登陆验证结果==============================");
       log.info(httpDO.getResponseStr());
       JSONObject jo = JSONObject.parseObject(httpDO.getResponseStr());
       String loginCheck = jo.get("data") == null?null: (String)((JSONObject)jo.get("data")).get("loginCheck");
       log.info(jo.get("messages"));
       if(loginCheck == null || !"Y".equals(loginCheck)){
           throw new Exception(jo.get("messages").toString());
       }
       log.info("=============================================================");
       String reloginSuccessUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.url.relogin");
       httpDO = HttpUtils.doPost(reloginSuccessUrl, "_json_att=");
       log.info(httpDO.getHeaders());
       SessionUtils.getHttpTicket().setIsLogin(true);
       SessionUtils.getHttpTicket().setUserName(username);
    }

    /**
     * @param queryTicketDO
     */
    public JSONArray queryTicket(QueryTicketDO queryTicketDO) {
        String queryTicketUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.query.queryTicketLog");
        HttpDO httpDO = HttpUtils.doGet(queryTicketUrl,queryTicketDO.getParams(),queryTicketDO.getQueryTicketCookies());
        System.out.println(httpDO.getResponseStr());
        
        queryTicketUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.query.queryTicket");
        httpDO = HttpUtils.doGet(queryTicketUrl,queryTicketDO.getParams(),queryTicketDO.getQueryTicketCookies());
        System.out.println(httpDO.getResponseStr());
        JSONObject jsonResult = JSONObject.parseObject(httpDO.getResponseStr());
        return (JSONArray)jsonResult.get("data");
    }
    
}
