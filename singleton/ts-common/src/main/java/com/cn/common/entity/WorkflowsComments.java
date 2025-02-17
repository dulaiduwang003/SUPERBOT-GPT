package com.cn.common.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "workflows_comments")
public class WorkflowsComments {

  @TableId(type = IdType.AUTO)
  private Long workflowsCommentsId;

  private Long workflowsId;

  private Long userId;

  private String content;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;


}
