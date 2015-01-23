/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feng.domain.HttpTicketDO;
import com.feng.utils.SessionUtils;

/**
 * @author v_wuyunfeng
 *
 */
public class TicketInterceptor extends HandlerInterceptorAdapter{
    private Logger log = Logger.getLogger(LoginInitInterceptor.class); 
    @Override  
    public boolean preHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler) throws Exception {  
        log.info("==============TicketInterceptor===拦截器==================");
        HttpTicketDO httpTicketDO = (HttpTicketDO)request.getSession().getAttribute("httpTicket");
        if(httpTicketDO == null || !httpTicketDO.getIsLogin()){
            response.sendRedirect(request.getContextPath() +"/login/index");
            return false;
        }
        SessionUtils.setHttpTicket(httpTicketDO);
        return true;
    }  
}
