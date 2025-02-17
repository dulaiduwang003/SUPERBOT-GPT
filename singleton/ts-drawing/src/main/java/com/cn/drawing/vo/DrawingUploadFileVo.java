package com.cn.drawing.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/10/15 00:27
 */
@Data
@Accessors(chain = true)
public class DrawingUploadFileVo implements Serializable {

    private String fileName;

    private String preview;

}
