package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/10/30 15:14
 */
@Data
@Accessors(chain = true)
public class WorkflowsCategoryVo implements Serializable {

    private Long workflowsCategoryId;


    private String categoryName;

}
