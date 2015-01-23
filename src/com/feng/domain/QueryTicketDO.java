/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.domain;

import com.feng.utils.CommonUtils;

/**
 * @author v_wuyunfeng
 *
 */
public class QueryTicketDO {
    private String fromStation;
    private String toStation;
    private String fromStationStr;
    private String toStationStr;
    private String trainDate;
    private String purposeCodes;
    
    private String _jc_save_fromDate;
    private String _jc_save_toDate;
    private String _jc_save_wfdc_flag = "dc";
    
    public String getQueryTicketCookies(){
        StringBuffer sb = new StringBuffer();
        sb.append("_jc_save_fromStation=" + this.get_jc_save_fromStation());
        sb.append(";_jc_save_toStation=" + this.get_jc_save_toStation());
        sb.append(";_jc_save_fromDate=" + this.trainDate);
        sb.append(";_jc_save_toDate=" + this.trainDate);
        sb.append(";_jc_save_wfdc_flag=" + this.get_jc_save_wfdc_flag());
        return sb.toString();
    }
    
    public String getParams() {
       StringBuffer sb = new StringBuffer();
       sb.append("leftTicketDTO.train_date=" + trainDate);
       sb.append("&leftTicketDTO.from_station=" + fromStation);
       sb.append("&leftTicketDTO.to_station=" + toStation);
       sb.append("&purpose_codes=" + purposeCodes);
        return sb.toString();
    }
    
    public String getFromStation() {
        return fromStation;
    }
    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }
    public String getToStation() {
        return toStation;
    }
    public void setToStation(String toStation) {
        this.toStation = toStation;
    }
    public String getFromStationStr() {
        return fromStationStr;
    }
    public void setFromStationStr(String fromStationStr) {
        this.fromStationStr = fromStationStr;
    }
    public String getToStationStr() {
        return toStationStr;
    }
    public void setToStationStr(String toStationStr) {
        this.toStationStr = toStationStr;
    }
    public String getTrainDate() {
        return trainDate;
    }
    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }
    public String getPurposeCodes() {
        return purposeCodes;
    }
    public void setPurposeCodes(String purposeCodes) {
        this.purposeCodes = purposeCodes;
    }

    public String get_jc_save_fromStation() {
        String str = encodeStr(fromStationStr);
        return str+= fromStation;
    }

    public String get_jc_save_toStation() {
        String str = encodeStr(toStationStr);
        return str+= toStation;
    }

    public String get_jc_save_fromDate() {
        return _jc_save_fromDate;
    }

    public String get_jc_save_toDate() {
        return _jc_save_toDate;
    }

    public String get_jc_save_wfdc_flag() {
        return _jc_save_wfdc_flag;
    }
    
    private String encodeStr(String str){
       return CommonUtils.toUnicode(str + ",").replaceAll("\\\\", "%").toUpperCase().replaceAll("%U", "%u");
    }
    
    public static void main(String[] args) throws Exception {
        String str = "北京,";
        String string = CommonUtils.toUnicode(str).replaceAll("\\\\", "%").toUpperCase().replaceAll("%U", "%u");
        System.out.println(string);
    }
}
