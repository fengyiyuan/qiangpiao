/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.web.controller.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feng.core.springMVC.MvHelper;
import com.feng.domain.QueryTicketDO;
import com.feng.service.TicketService;

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
    @RequestMapping("/submitOrderRequest")
    public String submitOrder(String secretStr) throws Exception{
        //ticketService.submitOrder();
        return JSONObject.toJSONString(MvHelper.ajaxSuccess());
    }
    
    
}
