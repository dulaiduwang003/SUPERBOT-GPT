package com.cn.drawing.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/23 01:19
 */
@Data
@Accessors(chain = true)
public class SubmitTaskDto {

    @NotNull(message = "工作流ID不能为空")
    private Long workflowsId;

    @NotEmpty(message = "操作节点不能为空")
    private List<Container> containers;


    @Data
    @Accessors(chain = true)
    public static class Container {

        private String nodeDigital;

        private String nodeKey;

        private String nodeValue;

    }
}
