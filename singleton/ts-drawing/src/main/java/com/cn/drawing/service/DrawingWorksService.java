package com.cn.drawing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.drawing.dto.DeleteWorkflowsWorksDto;
import com.cn.drawing.vo.WorkflowsWorksVo;
import com.cn.drawing.vo.WorkflowsWorksPageVo;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2025/1/3 13:58
 */
public interface DrawingWorksService {

    IPage<WorkflowsWorksPageVo> getWorksPage(final Long pageNum);

    void deleteWorks(final DeleteWorkflowsWorksDto dto);

    WorkflowsWorksVo getWorks(final Long WorkflowsWorksId);

}
