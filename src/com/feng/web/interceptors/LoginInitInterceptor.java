/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feng.domain.HttpDO;
import com.feng.domain.HttpTicketDO;
import com.feng.utils.HttpUtils;
import com.feng.utils.PropUtils;
import com.feng.utils.SessionUtils;

/**
 * @author v_wuyunfeng
 *
 */
public class LoginInitInterceptor extends HandlerInterceptorAdapter {
    private Logger log = Logger.getLogger(LoginInitInterceptor.class); 
    @Override  
    public boolean preHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler) throws Exception {  
        log.info("==============LoginInitInterceptor===拦截器==================");
        HttpTicketDO httpTicketDO = (HttpTicketDO)request.getSession().getAttribute("httpTicket");
        if(httpTicketDO == null){
            HttpDO httpDO = HttpUtils.doHttp(PropUtils.getProp("ticket.url") + "otn/", "get", null, null);
            if(httpDO != null){
                httpTicketDO = new HttpTicketDO();
                httpTicketDO.setHttpDO(httpDO);
                request.getSession().setAttribute("httpTicket", httpTicketDO);
            }
        }else if(httpTicketDO.getIsLogin()){
            response.sendRedirect(request.getContextPath() +"/ticket/index");
            return false;
        }
        SessionUtils.setHttpTicket(httpTicketDO);
        return true;  
    }  
}
