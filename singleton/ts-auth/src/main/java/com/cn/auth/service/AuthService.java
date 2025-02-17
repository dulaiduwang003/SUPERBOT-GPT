package com.cn.auth.service;


import com.cn.auth.dto.AdminLoginDto;
import com.cn.auth.dto.WeChatLoginDto;

/**
 * The interface Auth service.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
public interface AuthService {

    String adminLogin(final AdminLoginDto dto);

    String weChatLogin(final WeChatLoginDto dto);


    void logout();
}
