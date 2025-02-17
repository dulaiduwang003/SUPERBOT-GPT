package com.cn.drawing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author 明明不是下雨天 github@dulaiduwang003 2024/9/26 16:03
 */

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum DrawingResultTypeEnum {

    VIDEO("VIDEO"),

    AUDIO("AUDIO"),

    MODEL("MODEL"),

    IMAGE("IMAGE");

    String dec;

}
