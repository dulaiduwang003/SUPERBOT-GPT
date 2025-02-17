package com.cn.drawing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/24 18:09
 */
@Data
@Accessors(chain = true)
public class DeleteCommentsDto {

    @NotNull(message = "评论ID不能为空")
    private Long workflowsCommentsId;

}
