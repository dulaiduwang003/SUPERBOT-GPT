package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/22 上午12:45
 */
@Data
@Accessors(chain = true)
public class WorkflowsVo implements Serializable {

    private Long workflowsId;

    private String title;

    private String originalImage;

    private String nowImage;

    private String introduced;

    private Long visited ;

    private LocalDateTime createTime;


}
