package com.cn.common.service;


import com.cn.common.vo.UserInfoVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
public interface UserService {


    void uploadAvatar(final MultipartFile file);


    void uploadNickName(final String nickName);


    UserInfoVo getCurrentUserInfo();

}
