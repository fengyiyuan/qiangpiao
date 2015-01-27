/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.web.controller.ticket;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feng.core.springMVC.MvHelper;
import com.feng.domain.HttpDO;
import com.feng.domain.QueryTicketDO;
import com.feng.service.TicketService;
import com.feng.utils.HttpUtils;
import com.feng.utils.PropUtils;
import com.feng.utils.SessionUtils;

/**
 * @author v_wuyunfeng
 *
 */
@Controller
@RequestMapping("/ticket")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    @RequestMapping("/index")
    public String index() throws Exception{
        return "ticket/index";
    }
    @ResponseBody
    @RequestMapping("/queryTicket")
    public String queryTicket(QueryTicketDO queryTicketDO) throws Exception{
        JSONArray jsonArray = ticketService.queryTicket(queryTicketDO);
        return JSONObject.toJSONString(MvHelper.ajaxSuccess(jsonArray));
    }
    @ResponseBody
    @RequestMapping("/queryPassengers")
    public String queryPassengers() throws Exception{
        JSONArray jsonArray = ticketService.queryPassengers();
        return JSONObject.toJSONString(MvHelper.ajaxSuccess(jsonArray));
    }
    
    @ResponseBody
    @RequestMapping("/initLeftTicket")
    public String initLeftTicket() throws Exception{
        ticketService.initLeftTicket();
        return JSONObject.toJSONString(MvHelper.ajaxSuccess());
    }
    
    @ResponseBody
    @RequestMapping("/submitOrder")
    public String submitOrder(String codes,String type,String secretStr) throws Exception{
        String submitOrder = ticketService.submitOrder(codes,type,secretStr);
        return JSONObject.toJSONString(MvHelper.ajaxSuccess(JSONObject.parse(submitOrder)));
    }
    
    @ResponseBody
    @RequestMapping("/getCodeImage")
    public String getImage() throws Exception{
        HttpDO httpDO = HttpUtils.doGet(PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.submit.code"));
        String filePath = "/temp/"+ UUID.randomUUID().toString().replace("-", "") + ".jpg";
        FileCopyUtils.copy(httpDO.getBaos().toByteArray(), new File(System.getProperty("webRoot") + filePath));
        return filePath;
    }
    
    @ResponseBody
    @RequestMapping("/validCode")
    public String getImage(String code) throws Exception{
        String checkCodeUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.url.checkCode");
        String params = "randCode="+code+"&rand=randp&_json_att=&REPEAT_SUBMIT_TOKEN=" + SessionUtils.getHttpTicket().getGlobalRepeatSubmitToken();
        HttpDO httpDO = HttpUtils.doPost(checkCodeUrl, params);
        return httpDO.getResponseStr();
    }
    @ResponseBody
    @RequestMapping("/checkOrderInfo")
    public String checkOrderInfo(String code) throws Exception{
        String checkCodeUrl = PropUtils.getProp("ticket.url") + PropUtils.getProp("ticket.url.checkCode");
        String params = "randCode="+code+"&rand=randp&_json_att=&REPEAT_SUBMIT_TOKEN=" + SessionUtils.getHttpTicket().getGlobalRepeatSubmitToken();
        HttpDO httpDO = HttpUtils.doPost(checkCodeUrl, params);
        return httpDO.getResponseStr();
    }
    
}
