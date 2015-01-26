/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.dao.mapper;

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
}
