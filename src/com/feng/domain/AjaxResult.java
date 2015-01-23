/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.domain;

import com.alibaba.fastjson.JSONObject;
import com.feng.core.springMVC.ResultTypeEnum;

/**
 * @author v_wuyunfeng
 *
 */
public class AjaxResult<T> {
    private String msg ;
    private String code ;
    private String type ="2";
    private T result;
    
    public AjaxResult(){
        
    }
    
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }
    
    private AjaxResult(String type,String code,String msg,  T result) {
        super();
        this.msg = msg;
        this.code = code;
        this.type = type;
        this.result = result;
    }
    
    public static <E> AjaxResult<E> getSucModule(E result,String msg){
        return new AjaxResult<E>(ResultTypeEnum.SUCCESS.value,"",msg,result);
        
    }
    public static <E> AjaxResult<E> getSucModule(E result){
        return new AjaxResult<E>(ResultTypeEnum.SUCCESS.value,"","操作成功",result);
        
    }
    public static <E> AjaxResult<E> getLoginModule(E result){
        return new AjaxResult<E>(ResultTypeEnum.NEED_LOGIN.value,"","需要登录",result);
        
    }
    public static <E> AjaxResult<E> getErrModule(E result){
        return new AjaxResult<E>(ResultTypeEnum.ERRORS.value,"","操作失败",result);
        
    }
}
