package com.cn.common.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName(value = "workflows")
public class Workflows {

    @TableId(type = IdType.AUTO)
    private Long workflowsId;

    private Long energy;

    private Long workflowsCategoryId;

    private String resultType;

    private String title;

    private String originalImage;

    private String nowImage;

    private String introduced;

    private String inputNode;

    private String json;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
