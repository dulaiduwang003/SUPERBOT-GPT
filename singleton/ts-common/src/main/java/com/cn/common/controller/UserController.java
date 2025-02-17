package com.cn.common.controller;


import com.cn.common.dto.UploadNickNameDto;
import com.cn.common.exceptions.OssException;
import com.cn.common.msg.Result;
import com.cn.common.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Oss controller.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/upload/avatar", consumes = "multipart/form-data")
    public Result uploadAvatar(@Valid @NotNull(message = "用户头像不能为空") final MultipartFile file) {
        try {
            userService.uploadAvatar(file);
            return Result.ok();
        } catch (OssException ex) {
            return Result.error(ex.getMessage());
        }

    }

    @PostMapping(value = "/upload/nickName", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result uploadNickName(@Validated @RequestBody UploadNickNameDto dto) {
        userService.uploadNickName(dto.getNickName());
        return Result.ok();
    }


    @GetMapping(value = "/get/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result getCurrentUserInfo() {
        return Result.data(userService.getCurrentUserInfo());
    }


}
