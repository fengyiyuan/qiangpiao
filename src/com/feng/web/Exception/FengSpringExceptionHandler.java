/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.web.Exception;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.feng.core.springMVC.MvHelper;
import com.feng.core.springMVC.ResultTypeEnum;
import com.feng.domain.AjaxResult;
import com.feng.utils.WebUtils;
import com.feng.utils.WebUtils.RequestType;


/**
 * @author v_wuyunfeng
 *
 */
public class FengSpringExceptionHandler  implements HandlerExceptionResolver {
    private static final Logger logger = LogManager.getLogger(FengSpringExceptionHandler.class);
    private static String ERROR_PAGE_ERROR = "error/error";// 程序错误页面
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handlerO, Exception e) {
        System.out.println("===============异常处理===================");
        
        HandlerMethod handler=(HandlerMethod)handlerO;
        Object actionBean=handler.getBean();
        Method method=handler.getMethod();
        e.printStackTrace();
        logger.error("Exception is found in the class:"+actionBean.getClass().getName()+"."+method.getName()+"()");
        logger.error("The exception class:"+e.getClass().getName());
        logger.error("The exception message:"+e.getMessage());
        logger.error("The exception case:"+e.getCause());
         
        
        RequestType reqType = WebUtils.getRequestType(request);
          switch (reqType) {
          case AJAX_REQUEST:
              return handleAjaxException(e.getMessage());
          case UPLOAD_REQUEST:
              return handleAjaxException(e.getMessage());
          case PAGE_REQUEST:
              return handlePageException(e.getMessage());
          default:
              return handlePageException(e.getMessage());
          }
    }
    
    /***
     * @Description: AJAX异常提示
     */
    private ModelAndView handleAjaxException(String message) {
        return MvHelper.json(MvHelper.ajaxError(message));
    }
    
    /****
     * @Method: handlePageException
     */
    private ModelAndView handlePageException(String message) {
        return  MvHelper.forward(ERROR_PAGE_ERROR).addObject("error",message);
    }

}
