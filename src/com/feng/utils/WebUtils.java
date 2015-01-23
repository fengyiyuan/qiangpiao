/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author v_wuyunfeng
 *
 */
public class WebUtils {
    public enum RequestType {
        PAGE_REQUEST(0), // 页面
        AJAX_REQUEST(1), // AJAX
        UPLOAD_REQUEST(2);// 上传
        public int value;

        private RequestType(int value) {
            this.value = value;
        }

    }
    
    public static RequestType getRequestType(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType))
            return RequestType.AJAX_REQUEST;
        else if (ServletFileUpload.isMultipartContent(request))
            return RequestType.UPLOAD_REQUEST;
        else
            return RequestType.PAGE_REQUEST;

    }
}
