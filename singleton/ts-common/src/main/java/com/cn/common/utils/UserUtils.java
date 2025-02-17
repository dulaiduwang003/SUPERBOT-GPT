package com.cn.common.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.cn.common.constant.UserConstant;
import com.cn.common.structure.UserInfoStructure;


/**
 * 用户工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
public class UserUtils {


    public static Long getCurrentLoginId() {
        return Long.parseLong(String.valueOf(StpUtil.getLoginId()));
    }


    public static UserInfoStructure getCurrentUserInfo() {
        return (UserInfoStructure) StpUtil.getSession().get(UserConstant.USER_INFO);
    }

    public static void updateUserInfo(final UserInfoStructure userInfo) {
        StpUtil.getSession().set(UserConstant.USER_INFO, userInfo);
    }

    public static void updateUserInfoById(final Long userId, final UserInfoStructure userInfo) {
        StpUtil.getSessionByLoginId(userId).set(UserConstant.USER_INFO, userInfo);

    }

    public static UserInfoStructure getUserInfoById(final Long userId) throws NullPointerException{
        return (UserInfoStructure) StpUtil.getSessionByLoginId(userId).get(UserConstant.USER_INFO);

    }

}
