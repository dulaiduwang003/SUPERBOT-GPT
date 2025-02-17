package com.cn.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The enum File content type enum.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum FileContentTypeEnum {


    IMAGE("image/png"),

    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),

    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),

    PDF("application/pdf");

    String dec;

}
