/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feng.dao.core.BaseDao;
import com.feng.dao.mapper.TicketMapper;
import com.feng.domain.PassengerDO;
import com.feng.domain.UserDO;

/**
 * @author v_wuyunfeng
 *
 */
@Component("ticketDao")
public class TicketDao{
    @Autowired
    private BaseDao baseDao;
    /**
     * @param username
     * @param password
     */
    public UserDO userLogin(String username, String password) {
        UserDO userDO = baseDao.getSqlSession().getMapper(TicketMapper.class).getUserByUserName(username);
        if(userDO == null){
            userDO = new UserDO();
        }
        userDO.setUsername(username);
        userDO.setLastLogin(System.currentTimeMillis());
        if(userDO.getId() == null){
            baseDao.getSqlSession().getMapper(TicketMapper.class).insertUser(userDO);
        }else{
            baseDao.getSqlSession().getMapper(TicketMapper.class).updateUser(userDO);
        }
        return userDO;
    }
    /**
     * @param pass
     */
    public void updatePassengers(JSONArray pass,UserDO userDO) {
        if(pass != null){
            baseDao.getSqlSession().getMapper(TicketMapper.class).deletePassByUserId(userDO.getId());
            for(int i = 0; i < pass.size(); i++){
                JSONObject jo = pass.getJSONObject(i);
                jo.put("user_id", userDO.getId());
                baseDao.getSqlSession().getMapper(TicketMapper.class).addPass(jo);
            }
        }
    }
    /**
     * @param codes
     * @param id
     * @return 
     */
    public List<PassengerDO> queryPassgersByCodes(String codes, Integer userId) {
       return baseDao.getSqlSession().getMapper(TicketMapper.class).queryPassByCodes(codes,userId);
    }
    
}
