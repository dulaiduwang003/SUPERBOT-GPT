package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/21 下午9:15
 */
@Data
@Accessors(chain = true)
public class WorkflowsPageVo implements Serializable {

    private Long workflowsId;

    private String originalImage;

    private String nowImage;

    private String categoryName;

    private String title;

    private Long visited;

    private LocalDateTime createTime;

}
