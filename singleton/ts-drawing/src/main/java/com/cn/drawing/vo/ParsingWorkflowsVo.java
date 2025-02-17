package com.cn.drawing.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/10/12 17:50
 */
@Data
@Accessors(chain = true)
public class ParsingWorkflowsVo implements Serializable {

    private String json;

    private List<Node> nodeList;

    private List<InputNode> inputNodeList;


    @Data
    @Accessors(chain = true)
    public static class InputNode {

        private String nodeKey;

        private String tips;

    }

    @Data
    @Accessors(chain = true)
    public static class Node {

        private String tips;

        private String nodeKey;

        private String type;

        private String nodeDigital;

    }

}
