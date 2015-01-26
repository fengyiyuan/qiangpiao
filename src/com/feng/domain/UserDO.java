/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.feng.domain;

/**
 * @author v_wuyunfeng
 *
 */
public class UserDO {
    private Integer id;
    private String username;
    private String password;
    private long lastLogin;
    public long getLastLogin() {
        return lastLogin;
    }
    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
