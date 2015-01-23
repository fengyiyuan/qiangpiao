/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.core.springMVC;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.feng.domain.AjaxResult;

/**
 * @author v_wuyunfeng
 *
 */
public class MvHelper {
    public static <T> ModelAndView json(final T  data){
        FastJsonJsonView  jsonView=new FastJsonJsonView(){
                @Override
                protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                                         HttpServletResponse response) throws Exception {
                      String text = JSON.toJSONString(data, new SerializerFeature[0]);
                      byte[] bytes = text.getBytes(Charset.forName("UTF-8"));

                      OutputStream stream = response.getOutputStream();
                      response.setContentType("text/html;charset=UTF-8");
                      stream.write(bytes);
                  }
              
          };
          ModelAndView mv=new ModelAndView();
          mv.setView(jsonView);
          return mv;
     }
    
    public static <T> AjaxResult<T> ajaxError(String message,T result){
        AjaxResult<T> ajaxResult = AjaxResult.getErrModule(result);
        if(message != null){
            ajaxResult.setMsg(message);
        }
        return ajaxResult;
    }
    public static <T> AjaxResult<T> ajaxError(){
        return ajaxError(null, null);
    }
    public static <T> AjaxResult<T> ajaxError(String message){
        return ajaxError(message, null);
    }
    public static <T> AjaxResult<T> ajaxSuccess(String message,T result){
        AjaxResult<T> ajaxResult = AjaxResult.getSucModule(result);
        if(message != null){
            ajaxResult.setMsg(message);
        }
        return ajaxResult;
    }
    public static <T> AjaxResult<T> ajaxSuccess(){
        return ajaxSuccess(null, null);
    }
    public static <T> AjaxResult<T> ajaxSuccess(String message){
        return ajaxSuccess(message, null);
    }
    public static <T> AjaxResult<T> ajaxSuccess(T result){
        return ajaxSuccess(null, result);
    }
    
    public static  ModelAndView forward(String url){
        ModelAndView mv= new ModelAndView(url);
        return mv;
    }
    
}
