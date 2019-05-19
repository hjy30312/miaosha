package com.hjy.miaosha.domain;


import lombok.Data;

import java.util.Date;

/**
 * 用户
 */
@Data
public class User {

    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private Integer Jurisdiction;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;

    public User(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    public User() {
    }
}
