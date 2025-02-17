package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/23 23:01
 */
@Data
@Accessors(chain = true)
public class DrawingProgressVo implements Serializable {

    private String taskId;

    private Long progress;

    private String url;

    private Long workflowsWorksId;

    private String workflowsName;

    private String resultType;

    private String status;

    private Long location;

    private LocalDateTime createTime;
}
