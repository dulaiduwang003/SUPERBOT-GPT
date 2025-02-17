package com.cn.drawing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The enum File enum.
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum DrawingStatusEnum {

    WAIT("WAIT"),

    BUILD("BUILD"),

    SUCCEED("SUCCEED"),

    FAILED("FAILED");

    String dec;

}
