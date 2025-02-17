package com.cn.drawing.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/24 18:09
 */
@Data
@Accessors(chain = true)
public class CreateCommentsDto {

    @NotEmpty(message = "评论内容不能为空")
    private String content;

    @NotNull(message = "评论工作流不能为空")
    private Long workflowsId;

}
