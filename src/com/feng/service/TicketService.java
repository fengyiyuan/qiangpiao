/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.service;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feng.dao.TicketDao;
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
@Transactional
@Service("ticketService")
public class TicketService {
    private Logger log = Logger.getLogger(TicketService.class); 
    
    @Autowired
    private TicketDao ticketDao;
    
    /**
     * @param username
     * @param password
     * @param code
     * @param webRoot 
     * @throws Exception 
     */
    public void login(String username, String password, String code) throws Exception {
        HttpDO httpDO = null;
        String loginUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.url.loginInit");
        parseDynamicJS(loginUrl);
       String params = PropUtils.getProp("ticket.login.username") + "=" + username + "&" +
                       PropUtils.getProp("ticket.login.password") + "=" + password + "&" +
                       PropUtils.getProp("ticket.login.code") + "=" + code + "&" +
                       SessionUtils.getHttpTicket().getDynamicKey() + "=" + SessionUtils.getHttpTicket().getDynamicVal() + "&" +
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
       ticketDao.userLogin(username, password);
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

    /**
     * @return
     */
    public JSONArray queryPassengers() {
        String passUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.query.getPassengers");
        HttpDO httpDO = HttpUtils.doGet(passUrl);
        JSONObject retJO = JSONObject.parseObject(httpDO.getResponseStr());
        return retJO.getJSONObject("data").getJSONArray("normal_passengers");
    }

    /**
     * 
     */
    public void initLeftTicket() {
        String initLTUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.query.initQuery");
        parseDynamicJS(initLTUrl);
    }
    
    private void parseDynamicJS(String url){
        HttpDO httpDO = HttpUtils.doGet(url);
        String regEx = "\"(/otn/dynamicJs/.*?)\"";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(httpDO.getResponseStr());
        String dynamicUrl = null; 
        if(m.find()){
            dynamicUrl = m.group(1);
        }
        httpDO = HttpUtils.doGet(PropUtils.getProp("ticket.url") + dynamicUrl);
        regEx = "gc\\(\\)\\{var\\skey='(.*?)'";
        p = Pattern.compile(regEx);
        m = p.matcher(httpDO.getResponseStr());
        String key= null;
        if(m.find()){
           key = m.group(1);
        }
        if(key != null){
            try{
            String jsStr = new String(FileCopyUtils.copyToByteArray(new File(System.getProperty("webRoot") + "/ticketFiles/12306.js")));
            String value = CommonUtils.runSecretKeyValueMethod(key, jsStr);
            SessionUtils.getHttpTicket().setDynamicKey(key);
            SessionUtils.getHttpTicket().setDynamicVal(value);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        String regEx = "/otn/dynamicJs/(.*?)\"";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher("/otn/dynamicJs/abcdefg\"");
        if(m.find()){
            System.out.println(m.group(1));
        }
    }
}
