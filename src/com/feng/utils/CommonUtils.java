/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * @author v_wuyunfeng
 * 
 */
public class CommonUtils {

    public static String runSecretKeyValueMethod(String mark, String jsStr) throws Exception {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByExtension("js");
        se.eval(jsStr);
        String value = (String) se.eval("eval(\"encode32(bin216(Base32.encrypt('1111','" + mark + "')))\")");
        System.out.println("secret value = " + value);
        return value;
    }

    public static String toUnicode(String str) {
        char[] arChar = str.toCharArray();
        int iValue = 0;
        String uStr = "";
        for (int i = 0; i < arChar.length; i++) {
            iValue = (int) str.charAt(i);
            if (iValue <= 256) {
                // uStr+="& "+Integer.toHexString(iValue)+";";
                uStr += "\\" + Integer.toHexString(iValue);
            } else {
                // uStr+="&#x"+Integer.toHexString(iValue)+";";
                uStr += "\\u" + Integer.toHexString(iValue);
            }
        }
        return uStr;
    }
    
    public static String unicodeToGB(String s) {
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(s, "\\u");
        while (st.hasMoreTokens()) {
            sb.append((char) Integer.parseInt(st.nextToken(), 16));
        }
        return sb.toString();
    }
    //只转换中文
    public static String toUnicodeString(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                sb.append("\\u" + Integer.toHexString(c));
            }
        }
        return sb.toString();
    }
    
    public static String getLastDateStr(String date,String pattern){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date2 = sdf.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date2);
            cal.add(Calendar.DAY_OF_MONTH,-1);
            return sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * @param trainDate
     * @return
     */
    public static String getLastDateStr(String date) {
        return getLastDateStr(date, "yyyy-MM-dd");
    }
    
    public static String dateToString(Date date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String format = sdf.format(date);
        return format;
    }
    public static String dateToString(Date date){
        return dateToString(date, "yyyy-MM-dd");
    }
    
    public static String getCurrentDateStr(){
        return dateToString(new Date());
    }
    
    public static void main(String[] args) {
        String lastDateStr = getLastDateStr("2014-08-01");
        System.out.println(lastDateStr);
    }
}
