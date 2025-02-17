package com.cn.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class FileVo implements Serializable {

    private String fileType;

    private String fileUrl;

}
