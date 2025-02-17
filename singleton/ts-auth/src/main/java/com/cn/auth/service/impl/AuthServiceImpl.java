package com.cn.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.auth.dto.AdminLoginDto;
import com.cn.auth.dto.WeChatLoginDto;
import com.cn.auth.exceptions.AuthException;
import com.cn.auth.service.AuthService;
import com.cn.common.configuration.OperationConfiguration;
import com.cn.common.entity.User;
import com.cn.common.enums.RoleEnum;
import com.cn.common.mapper.UserMapper;
import com.cn.common.structure.UserInfoStructure;
import com.cn.common.utils.IdGeneratorUtils;
import com.cn.common.utils.RandomUtil;
import com.cn.common.utils.UserUtils;
import com.cn.common.utils.WeChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Auth service.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserMapper userMapper;

    private final WeChatUtils weChatUtils;

    private final OperationConfiguration operationConfiguration;

    private final IdGeneratorUtils idGeneratorUtils;

    @Override
    public String adminLogin(final AdminLoginDto dto) {
        String account = operationConfiguration.getAuth().getAccount();
        String password = operationConfiguration.getAuth().getPassword();
        if (account.equals(dto.getAccount())&&password.equals(dto.getPassword())) {
            //临时凭证
            long snowflakeId = idGeneratorUtils.getSnowflakeId();
            StpUtil.login(snowflakeId);
            UserUtils.updateUserInfo(new UserInfoStructure()
                    .setRoles(List.of(RoleEnum.ADMIN.getDesc()))
                    .setUserId(snowflakeId));
        }
       throw new AuthException("账号或密码错误");
    }

    @Override
    public String weChatLogin(final WeChatLoginDto dto) {
        return weChatAuthorizedLogin(dto);
    }


    @Override
    public void logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
    }


    private String weChatAuthorizedLogin(final WeChatLoginDto dto) {
        final String openId = weChatUtils.getOpenId(dto.getCode());
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getOpenId, openId)
        );
        if (user == null) {
            //设置默认用户名
            final String nickName = "用户" + RandomUtil.getSixBitRandom();
            user = new User()
                    .setUserId(idGeneratorUtils.getSnowflakeId())
                    .setNickName(nickName)
                    .setEnergy(operationConfiguration.getEnergy().getRegistered())
                    .setOpenId(openId);
            userMapper.insert(user);
        }
        StpUtil.login(user.getUserId());
        UserUtils.updateUserInfo(new UserInfoStructure()
                .setOpenId(openId)
                .setAvatar(user.getAvatar())
                .setNickName(user.getNickName())
                //普通同于
                .setRoles(List.of(RoleEnum.USER.getDesc()))
                .setUserId(user.getUserId()));

        return StpUtil.getTokenValue();
    }


}
