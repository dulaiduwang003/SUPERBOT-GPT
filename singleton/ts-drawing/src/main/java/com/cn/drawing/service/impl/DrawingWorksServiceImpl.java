package com.cn.drawing.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.common.entity.WorkflowsWorks;
import com.cn.common.mapper.WorkflowsWorksMapper;
import com.cn.common.utils.UserUtils;
import com.cn.drawing.dto.DeleteWorkflowsWorksDto;
import com.cn.drawing.exceptions.DrawingException;
import com.cn.drawing.service.DrawingWorksService;
import com.cn.drawing.vo.WorkflowsWorksPageVo;
import com.cn.drawing.vo.WorkflowsWorksVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2025/1/3 13:59
 */
@Service
@RequiredArgsConstructor
public class DrawingWorksServiceImpl implements DrawingWorksService {

    private final WorkflowsWorksMapper workflowsWorksMapper;


    @Override
    public IPage<WorkflowsWorksPageVo> getWorksPage(final Long pageNum) {
        return workflowsWorksMapper.selectPage(new Page<>(pageNum, 10),
                        new QueryWrapper<WorkflowsWorks>().lambda().eq(WorkflowsWorks::getUserId, UserUtils.getCurrentLoginId())
                                .orderByDesc(WorkflowsWorks::getCreateTime))
                .convert(c -> new WorkflowsWorksPageVo()
                        .setResultType(c.getResultType())
                        .setCreateTime(c.getCreateTime())
                        .setImage(c.getUrl())
                        .setWorkflowsWorksId(c.getWorkflowsWorksId()));

    }

    @Override
    public void deleteWorks(final DeleteWorkflowsWorksDto dto) {
        workflowsWorksMapper.delete(new QueryWrapper<WorkflowsWorks>().lambda().eq(WorkflowsWorks::getWorkflowsWorksId, dto.getWorkflowsWorksId()).eq(WorkflowsWorks::getUserId, UserUtils.getCurrentLoginId()));
    }

    @Override
    public WorkflowsWorksVo getWorks(final Long workflowsWorksId) {
        final WorkflowsWorks workflowsWorks = workflowsWorksMapper.selectOne(new QueryWrapper<WorkflowsWorks>().lambda().eq(WorkflowsWorks::getWorkflowsWorksId, workflowsWorksId));

        if (workflowsWorks == null) {
            throw new DrawingException("绘制结果不存在");
        }

        final WorkflowsWorksVo workflowsWorksVo = new WorkflowsWorksVo().setResultType(workflowsWorks.getResultType()).setWorkflowsWorksId(workflowsWorks.getWorkflowsWorksId()).setUrl(workflowsWorks.getUrl()).setWorkflowsId(workflowsWorks.getWorkflowsId());
        if (StpUtil.isLogin()) {
            //是否登录 如果登录检查是否拥有删除权限
            workflowsWorksVo.setIsCurrentUser(workflowsWorks.getUserId().equals(UserUtils.getCurrentLoginId()));
        }
        return workflowsWorksVo;

    }


}
