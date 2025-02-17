package com.cn.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 明明不是下雨天 github@dulaiduwang003 2024/12/21 15:17
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum StatusEnum {


    TRUE(0L),

    FALSE(1L);

    Long dec;

}
