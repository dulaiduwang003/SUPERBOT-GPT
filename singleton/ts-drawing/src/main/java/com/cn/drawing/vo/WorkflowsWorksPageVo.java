package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/24 19:45
 */
@Data
@Accessors(chain = true)
public class WorkflowsWorksPageVo implements Serializable {

    private Long workflowsWorksId;

    private String image;

    private LocalDateTime createTime;

    private String resultType;


}
