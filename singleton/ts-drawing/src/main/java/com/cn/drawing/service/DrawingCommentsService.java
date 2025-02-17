package com.cn.drawing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.drawing.dto.CreateCommentsDto;
import com.cn.drawing.dto.DeleteCommentsDto;
import com.cn.drawing.vo.WorkflowsCommentsPageVo;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/12 下午5:30
 */
public interface DrawingCommentsService {


    void createComments(final CreateCommentsDto dto);

    void deleteComments(final DeleteCommentsDto dto);

    IPage<WorkflowsCommentsPageVo> getCommentsPage(final Long pageNum, final Long workflowsId);


}
