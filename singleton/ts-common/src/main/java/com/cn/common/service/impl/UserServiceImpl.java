package com.cn.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.common.constant.DayDataConstant;
import com.cn.common.entity.User;
import com.cn.common.enums.FilePathEnum;
import com.cn.common.mapper.UserMapper;
import com.cn.common.service.UserService;
import com.cn.common.structure.UserInfoStructure;
import com.cn.common.utils.RedisUtils;
import com.cn.common.utils.UploadUtil;
import com.cn.common.utils.UserUtils;
import com.cn.common.vo.UserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/10 下午9:06
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UploadUtil uploadUtil;

    private final RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadNickName(final String nickName) {
        userMapper.updateById(
                new User()
                        .setUserId(UserUtils.getCurrentLoginId())
                        .setNickName(nickName)
        );
        //更新用户缓存
        final UserInfoStructure currentUserInfo = UserUtils.getCurrentUserInfo();
        UserUtils.updateUserInfo(currentUserInfo.setNickName(nickName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadAvatar(final MultipartFile file) {
        final String url = uploadUtil.uploadFile(file, FilePathEnum.AVATAR.getDec());
        userMapper.updateById(
                new User()
                        .setUserId(UserUtils.getCurrentLoginId())
                        .setAvatar(url)
        );
        //更新用户缓存
        final UserInfoStructure currentUserInfo = UserUtils.getCurrentUserInfo();
        UserUtils.updateUserInfo(currentUserInfo.setAvatar(url));
    }

    @Override
    public UserInfoVo getCurrentUserInfo() {
        final UserInfoStructure currentUserInfo = UserUtils.getCurrentUserInfo();
        //统计今日系统访问量
        redisUtils.setSet(DayDataConstant.NEW_VISITS_TODAY, currentUserInfo.getUserId());

        User user = userMapper.selectOne(new QueryWrapper<User>()
                .lambda()
                .eq(User::getUserId, currentUserInfo.getUserId())
                .select(User::getEnergy)
        );

        return new UserInfoVo()
                .setOpenId(currentUserInfo.getOpenId())
                .setAvatar(currentUserInfo.getAvatar())
                .setEnergy(user.getEnergy())
                .setNickName(currentUserInfo.getNickName())
                .setRoles(currentUserInfo.getRoles());
    }
}
