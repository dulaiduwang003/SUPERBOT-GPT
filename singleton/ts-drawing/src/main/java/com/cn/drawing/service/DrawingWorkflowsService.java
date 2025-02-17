package com.cn.drawing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cn.drawing.dto.UploadWorkflowsDto;
import com.cn.drawing.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2025/1/3 14:20
 */
public interface DrawingWorkflowsService {


    IPage<WorkflowsPageVo> getWorkflowsPage(final Long pageNum, final String prompt, final Long workflowsCategoryId);

    WorkflowsVo getWorkflows(final Long workflowsId);

    List<WorkflowsCategoryVo> getWorkflowsCategoryList();

    WorkflowsInterfaceVo getWorkflowsInterface(final Long workflowsId);

    DrawingUploadFileVo uploadWorkflowsComponentFile(final MultipartFile file);

    ParsingWorkflowsVo parsingWorkflowJson(final MultipartFile file);

    void uploadWorkflows(final UploadWorkflowsDto dto);
}
