package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/24 02:17
 */
@Data
@Accessors(chain = true)
public class WorkflowsWorksVo implements Serializable {

    private Long workflowsWorksId;

    private Long workflowsId;

    private String url;

    private String resultType;

    private Boolean isCurrentUser = false;

}
