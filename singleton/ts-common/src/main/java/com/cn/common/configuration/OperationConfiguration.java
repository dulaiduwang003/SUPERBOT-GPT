package com.cn.common.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "operation")
@Data
@Accessors(chain = true)
public class OperationConfiguration {


    private Energy energy;

    private Auth auth;

    @Data
    @Accessors(chain = true)
    public static class Auth {

        private String account;

        private String password;
    }


    @Data
    @Accessors(chain = true)
    public static class Energy {

        private Long registered;

        private Long ad;
    }
}
