package com.cn.common.enums;

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
public enum WorkflowsNodeEnum {

    TEXT_PROMPT("TEXT_PROMPT"),

    IMAGE_UPLOAD("IMAGE_UPLOAD"),

    VIDEO_UPLOAD("VIDEO_UPLOAD");

    String dec;

}
