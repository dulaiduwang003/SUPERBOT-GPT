package com.cn.common.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "drawing")
@Data
@Accessors(chain = true)
public class DrawingConfiguration {

    private String server;

    private Long concurrent;

    private Long submitTaskMax;
}
