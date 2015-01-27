/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.feng.domain.PassengerDO;
import com.feng.domain.UserDO;

/**
 * @author v_wuyunfeng
 *
 */
public interface TicketMapper {
    
    public UserDO getUserByUserName(String username);

    /**
     * @param userDO
     */
    public void insertUser(UserDO userDO); 
    
    public void updateUser(UserDO userDO);

    /**
     * @param id
     */
    public void deletePassByUserId(Integer id);

    /**
     * @param jo
     */
    public void addPass(JSONObject jo);

    /**
     * @param codes
     * @param userId
     */
    public List<PassengerDO> queryPassByCodes(@Param("codes")String codes, @Param("userId")Integer userId); 
}
