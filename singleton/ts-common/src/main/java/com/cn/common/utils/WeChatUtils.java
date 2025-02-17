package com.cn.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.cn.common.exceptions.WechatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 * @date 2022/6/11 14:23
 */
@Component
@SuppressWarnings("all")
@RequiredArgsConstructor
@Slf4j
public class WeChatUtils {

    @Value("${we-chat.app-id}")
    private String appId;

    @Value("${we-chat.secret}")
    private String secret;

    @Value("${we-chat.host}")
    private String host;

    @Value("${we-chat.template-id}")
    private String templateId;


    private final RedisUtils redisUtils;

    /**
     * 发送格式数据
     *
     * @param json the json
     */
    private void wxSubscribeMessages(final JSONObject json) {
        final String wechatToken = WeChatTokenUtil.INSTANCE.getWechatToken(host, appId, secret);
        WebClient.create().post().uri(host + "/cgi-bin/message/subscribe/send?access_token=" + wechatToken)
                .body(BodyInserters.fromValue(json))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(res -> {
                    log.info("消息发送结果:{}", res);
                });

    }

    /**
     * 发送数据给指定微信用户
     *
     * @param openId
     * @param succeed
     * @param msg
     * @param createdTime
     * @return
     */
    public void sendNotice(final String openId, boolean succeed, final String msg) {
        final JSONObject body = new JSONObject();
        body.put("touser", openId);
        body.put("template_id", templateId);
        final JSONObject data = new JSONObject();
        data.put("phrase1", jsonCase(succeed ? "成功" : "失败"));
        data.put("thing5", jsonCase(msg));
        data.put("time9", jsonCase(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        body.put("data", data);
        wxSubscribeMessages(body);
    }

    /**
     * 转化微信data
     *
     * @param data
     * @return
     */
    public static JSONObject jsonCase(Object data) {
        final JSONObject character = new JSONObject();
        character.put("value", data);
        return character;
    }

    /**
     * Gets open id.
     *
     * @param code the code
     * @return the open id
     */
    public String getOpenId(final String code) {
        try {
            final String url = host + "/sns/jscode2session?appid=" + this.appId +
                    "&secret=" + this.secret + "&js_code=" + code + "&grant_type=authorization_code";
            final String response = WebClient.builder().build().get().uri(url).retrieve().bodyToMono(String.class).block();
            final JSONObject block = JSONObject.parseObject(response);
            final String openid = block.getString("openid");
            if (openid == null) {
                throw new RuntimeException();
            }
            return openid;
        } catch (Exception e) {
            throw new WechatException("获取微信用户标识失败");
        }
    }



    public String getPhone(final String code) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("code", code);
            final String response = WebClient.builder()
                    .baseUrl(host + "/wxa/business/getuserphonenumber?access_token=" + WeChatTokenUtil.INSTANCE.getWechatToken(host, appId, secret))
                    .build()
                    .post()
                    .body(BodyInserters.fromValue(
                            map
                    ))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            final JSONObject block = JSONObject.parseObject(response);

            final Integer errcode = block.getInteger("errcode");
            if (errcode != 0) {
                throw new WechatException("登录凭证已失效,请重新获取手机号登录凭证");
            }
            assert block != null;

            return block.getJSONObject("phone_info").getString("phoneNumber");
        } catch (Exception e) {
            throw new WechatException(e.getMessage());
        }
    }



}
