package com.cn.drawing.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.common.entity.User;
import com.cn.common.entity.WorkflowsComments;
import com.cn.common.mapper.UserMapper;
import com.cn.common.mapper.WorkflowsCommentsMapper;
import com.cn.common.utils.UserUtils;
import com.cn.drawing.dto.CreateCommentsDto;
import com.cn.drawing.dto.DeleteCommentsDto;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.service.DrawingCommentsService;
import com.cn.drawing.vo.WorkflowsCommentsPageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/12 下午5:31
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DrawingCommentsServiceImpl implements DrawingCommentsService {


    private final UserMapper userMapper;

    private final WorkflowsCommentsMapper workflowsCommentsMapper;


    @Override
    public void createComments(final CreateCommentsDto dto) {
        workflowsCommentsMapper.insert(new WorkflowsComments().setContent(dto.getContent()).setWorkflowsId(dto.getWorkflowsId()).setUserId(UserUtils.getCurrentLoginId()));
    }

    @Override
    public void deleteComments(final DeleteCommentsDto dto) {
        LambdaQueryWrapper<WorkflowsComments> queryWrapper = new QueryWrapper<WorkflowsComments>().lambda().eq(WorkflowsComments::getWorkflowsCommentsId, dto.getWorkflowsCommentsId());

        Long l = workflowsCommentsMapper.selectCount(queryWrapper);
        if (l <= 0) {
            throw new DrawingException("该评论不存在");
        }
        workflowsCommentsMapper.delete(queryWrapper);
    }

    @Override
    public IPage<WorkflowsCommentsPageVo> getCommentsPage(final Long pageNum, final Long workflowsId) {
        boolean isLogin = StpUtil.isLogin();
        return workflowsCommentsMapper.selectPage(new Page<>(pageNum, 30), new QueryWrapper<WorkflowsComments>().lambda().eq(WorkflowsComments::getWorkflowsId, workflowsId).orderByDesc(WorkflowsComments::getCreateTime)).convert(c -> {
            final WorkflowsCommentsPageVo vo = new WorkflowsCommentsPageVo();
            if (isLogin && Objects.equals(c.getUserId(), UserUtils.getCurrentLoginId())) {
                vo.setIsCurrentUser(true);
            }

            final User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUserId, c.getUserId()).select(User::getAvatar, User::getNickName));
            return vo.setWorkflowsCommentsId(c.getWorkflowsCommentsId()).setContent(c.getContent()).setAvatar(user.getAvatar()).setNickName(user.getNickName()).setCreateTime(c.getCreateTime());
        });
    }


}
