/**
 * Copyright (C) 2014 feng, Inc. All Rights Reserved.
 */
package com.feng.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author v_wuyunfeng
 *
 */
@Controller
public class MainController {
    
    @RequestMapping("/main")
    public String index(){
        return "index";
    }
}
