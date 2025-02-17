package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/24 17:53
 */
@Data
@Accessors(chain = true)
public class WorkflowsCommentsPageVo implements Serializable {

    private Long workflowsCommentsId;

    private String avatar;
    ;

    private String nickName;

    private String content;

    private Boolean isCurrentUser = false;

    private LocalDateTime createTime;
}
