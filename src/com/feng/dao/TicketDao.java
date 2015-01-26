/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.feng.dao.core.BaseDao;
import com.feng.dao.mapper.TicketMapper;
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
    public void userLogin(String username, String password) {
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
    }
    
}
