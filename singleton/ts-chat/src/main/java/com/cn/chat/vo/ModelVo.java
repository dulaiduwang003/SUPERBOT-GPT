package com.cn.chat.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Model vo.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class ModelVo implements Serializable {

    private String name;

    private String model;

}
