package com.cn.drawing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/25 10:08
 */
@Data
@Accessors(chain = true)
public class DeleteWorkflowsWorksDto {

    @NotNull(message = "作品ID不能为空")
    private Long workflowsWorksId;
}
