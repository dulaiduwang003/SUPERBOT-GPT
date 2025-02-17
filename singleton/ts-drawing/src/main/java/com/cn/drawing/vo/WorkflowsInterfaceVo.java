package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/22 15:39
 */
@Data
@Accessors(chain = true)
public class WorkflowsInterfaceVo implements Serializable {

    private Long workflowsId;

    private String title;

    private Long energy;

    private List<Container> containers;


    @Data
    @Accessors(chain = true)
    public static class Container {

        private String nodeDigital;

        private String nodeKey;

        private String tips;

        private String type;

        private String preview;

        private String nodeValue;

    }

}
