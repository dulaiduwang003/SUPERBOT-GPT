package com.cn.common.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "workflows_works")
public class WorkflowsWorks {

    @TableId(type = IdType.AUTO)
    private Long workflowsWorksId;

    private Long workflowsId;

    private Long userId;

    private String url;

    private String resultType;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
