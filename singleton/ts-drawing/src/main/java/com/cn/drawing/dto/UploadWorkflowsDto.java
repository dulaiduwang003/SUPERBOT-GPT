package com.cn.drawing.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/10/13 15:16
 */
@Data
@Accessors(chain = true)
public class UploadWorkflowsDto {

    @NotEmpty(message = "工作流标题不能为空")
    private String title;

    @NotEmpty(message = "工作流效果图不能为空")
    private String originalImage;

    @NotNull(message = "工作流类别不能为空")
    private Long workflowsCategoryId;

    @NotEmpty(message = "工作流效果图不能为空")
    private String nowImage;

    @NotEmpty(message = "工作流介绍不能为空")
    private String introduced;

    @NotEmpty(message = "工作流JSON不能为空")
    private String json;

    @NotNull(message = "工作流消耗能量不能为空")
    private Long energy;

    @NotEmpty(message = "工作流输出节点不能为空")
    private String inputNode;

    @NotEmpty(message = "工作流输出结果类型不能为空")
    private String resultType;

    @NotEmpty(message = "工作流表单不能为空")
    private List<WorkflowsForm> workflowsFormList;


    @Data
    @Accessors(chain = true)
    public static class WorkflowsForm {

        private String nodeKey;

        private String tips;

        private String nodeDigital;

        private String type;
    }

}
