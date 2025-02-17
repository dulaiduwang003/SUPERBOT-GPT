package com.cn.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cn.common.entity.User;
import com.cn.common.exceptions.EnergyException;
import com.cn.common.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/10/17 16:50
 */
@Component
@RequiredArgsConstructor
@EnableTransactionManagement
@Slf4j
public class EnergyUtils {

    private final UserMapper userMapper;


    public void inspection(final Long userId, final Long energy) {
        final User user = userMapper.selectOne(new QueryWrapper<User>()
                .lambda().eq(User::getUserId, userId)
        );
        if (user.getEnergy() < energy) {
            throw new EnergyException("您的SUPER币不足,观看广告获得奖励");
        }

    }


    public void deduct(final Long userId, final Long number) {
        userMapper.update(null, new UpdateWrapper<User>()
                .lambda()
                .eq(User::getUserId, userId)
                .setSql("energy = energy -" + number)
        );

    }

    public void increase(final Long userId, final Long number) {
        userMapper.update(null, new UpdateWrapper<User>()
                .lambda()
                .eq(User::getUserId, userId)
                .setSql("energy = energy +" + number)
        );

    }



}
