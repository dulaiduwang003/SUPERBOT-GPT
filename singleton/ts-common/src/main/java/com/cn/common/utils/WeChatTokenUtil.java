
package com.cn.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 微信 AccessToken 封装
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 * @date 2022/6/12 10:03
 */
public enum WeChatTokenUtil {
    INSTANCE;

    private final Token token = new Token();
    private final AtomicLong tokenExpireTime = new AtomicLong(0L);

    public String getWechatToken(final String host,final String appid, final String secret) {
        if ((System.currentTimeMillis() / 1000) + 200 >= tokenExpireTime.get()) {
            resetToken(host,appid, secret);
        }
        return token.getToken();
    }

    private void resetToken(final String host, final String appid, final String secret) {
        final JSONObject block = JSONObject.parseObject(getAccessToken(host, appid, secret));
        long timestamp = System.currentTimeMillis() / 1000;
        String accessToken = block.getString("access_token");
        int expiresIn = block.getInteger("expires_in");
        token.setToken(accessToken);
        tokenExpireTime.set(timestamp + expiresIn);
    }

    private String getAccessToken(final String host, final String appid, final String secret) {
        final String accessTokenUrl = host + "/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;

        return WebClient.create()
                .get().uri(accessTokenUrl)
                .retrieve()
                .bodyToMono(String.class).block(Duration.ofSeconds(5));

    }

    @Accessors(chain = true)
    @Data
    public static class Token {
        public String token;
    }
}
