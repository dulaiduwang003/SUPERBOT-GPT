package com.cn.chat.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * The type Ai input request dto.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class RequestDto implements Serializable {

    private String messages;

    private String model;

    private Boolean isFiltration;


}
