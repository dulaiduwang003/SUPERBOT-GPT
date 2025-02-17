package com.cn.drawing.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/23 15:03
 */
@Data
@Accessors(chain = true)
public class DrawingTaskResultStructure implements Serializable {

    private String taskId;

    private Long workflowsId;

    private String workflowsName;

    private String status;

    private Long progress;

    private Long workflowsWorksId;

    private String resultType;

    private Long location;

    private String url;

    private LocalDateTime createTime;


}
