package com.cn.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * The type We chat login dto.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class AdminLoginDto {


    @NotEmpty(message = "登录账号不能为空")
    private String account;

    @NotEmpty(message = "登录密码不能为空")
    private String password;
}
