package com.cn.common.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "chat")
@Data
@Accessors(chain = true)
public class ChatConfiguration {

    private String token;

    private String server;

    private List<Model> modelList;


    @Data
    @Accessors(chain = true)
    public static class Model {

        private String model;

        private String name;

    }
}
