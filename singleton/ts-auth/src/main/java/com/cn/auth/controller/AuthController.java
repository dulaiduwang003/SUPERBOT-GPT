package com.cn.auth.controller;


import com.cn.auth.dto.AdminLoginDto;
import com.cn.auth.dto.WeChatLoginDto;
import com.cn.auth.exceptions.AuthException;
import com.cn.auth.service.AuthService;
import com.cn.common.exceptions.WechatException;
import com.cn.common.msg.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Auth controller.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping(value = "/admin/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result adminLogo(@RequestBody @Validated AdminLoginDto dto) {
        try {
            return Result.data(authService.adminLogin(dto));
        } catch (AuthException e) {
            return Result.error(e.getMessage());
        }
    }



    @PostMapping(value = "/wechat/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result weChatLogin(@RequestBody @Validated WeChatLoginDto dto) {
        try {
            return Result.data(authService.weChatLogin(dto));
        } catch (WechatException e) {
            return Result.error(e.getMessage());
        }
    }


    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result logout() {
        authService.logout();
        return Result.ok();
    }
}
