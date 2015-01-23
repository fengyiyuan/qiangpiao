/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.web.controller.ticket;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.feng.core.springMVC.MvHelper;
import com.feng.domain.HttpDO;
import com.feng.service.TicketService;
import com.feng.utils.HttpUtils;
import com.feng.utils.PropUtils;

/**
 * @author v_wuyunfeng
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private TicketService ticketService;
    
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    
    @ResponseBody
    @RequestMapping("/getCodeImage")
    public String getImage(HttpServletRequest request) throws Exception{
        HttpDO httpDO = HttpUtils.doGet(PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.url.loginCode"));
        String path = request.getRealPath("/").toString();
        String filePath = "/temp/"+ UUID.randomUUID().toString().replace("-", "") + ".jpg";
        FileCopyUtils.copy(httpDO.getBaos().toByteArray(), new File(path + filePath));
        return filePath;
    }
    
    @ResponseBody
    @RequestMapping("/validCode")
    public String getImage(String code) throws Exception{
        String checkCodeUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.url.checkCode");
        String params = "rand=sjrand&randCode_validate=&randCode=" + code;
        HttpDO httpDO = HttpUtils.doPost(checkCodeUrl, params);
        return httpDO.getResponseStr();
    }
    
    
    @ResponseBody
    @RequestMapping("/login")
    public String login(String username,String password,String code,HttpServletRequest request) throws Exception{
        String webRoot = request.getRealPath("/");
        ticketService.login(username,password,code,webRoot);
        return JSONObject.toJSONString(MvHelper.ajaxSuccess());
    }
}
