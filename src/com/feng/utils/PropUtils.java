/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.utils;

import java.util.Properties;


import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * @author v_wuyunfeng
 *
 */
public class PropUtils {
   public static String getProp(String key){
       String val = null;
       try{
           Resource resource = new ClassPathResource("/config/config.properties");
           Properties props = PropertiesLoaderUtils.loadProperties(resource);
           val = (String) props.get(key);
       }catch(Exception e){
           e.printStackTrace();
       }
       return val;
   }
}
