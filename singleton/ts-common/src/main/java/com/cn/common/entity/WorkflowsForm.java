package com.cn.common.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value = "workflows_form")
public class WorkflowsForm {

    @TableId(type = IdType.AUTO)
    private Long workflowsFormId;

    private Long workflowsId;

    private String tips;

    private String nodeKey;

    private String nodeDigital;

    private String type;

}
