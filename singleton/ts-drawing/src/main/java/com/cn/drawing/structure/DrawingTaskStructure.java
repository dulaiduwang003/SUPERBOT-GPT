package com.cn.drawing.structure;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/23 15:03
 */
@Data
@Accessors(chain = true)
public class DrawingTaskStructure implements Serializable {

    private Long userId;

    private String openId;

    private Long workflowsId;

    private String inputNode;

    private String taskId;

    private String resultType;

    private JSONObject json;

    private Long energy;


}
