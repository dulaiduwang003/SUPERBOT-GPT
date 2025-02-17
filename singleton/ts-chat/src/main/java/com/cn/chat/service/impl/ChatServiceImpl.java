package com.cn.chat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.chat.dto.RequestDto;
import com.cn.chat.service.ChatService;
import com.cn.chat.vo.ModelVo;
import com.cn.common.configuration.ChatConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.parseObject;

/**
 * The type Gpt service.
 *
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {


    private final ChatConfiguration chatConfiguration;


    @Override
    public List<ModelVo> getModelList() {

        return chatConfiguration.getModelList().stream().map(c -> new ModelVo().setModel(c.getModel()).setName(c.getName())).toList();

    }

    @Override
    @SuppressWarnings("all")
    public Flux<String> execution(final RequestDto dto) {
        return Flux.create(sink -> {
            //转化数据
            final List<JSONObject> messages = parseArray(dto.getMessages(), JSONObject.class);
            //构建请求
            Map<String, Object> map = new HashMap<>();
            map.put("model", dto.getModel());
            map.put("messages", messages);
            map.put("stream", true);
            openAISendRequest(map, chatConfiguration.getToken(), chatConfiguration.getServer())
                    .doFinally(signal -> {
                        sink.complete();
                    })
                    .subscribe(data -> {
                        if (JSON.isValidObject(data)) {
                            JSONObject jsonObject = JSONObject.parseObject(data);
                            JSONArray choices = jsonObject.getJSONArray("choices");
                            if (choices != null && !choices.isEmpty()) {
                                final JSONObject dataSet = choices.getJSONObject(0);
                                if (dataSet.containsKey("delta")) {
                                    final JSONObject delta = dataSet.getJSONObject("delta");
                                    try {
                                        if (delta != null && delta.containsKey("content")) {
                                            sink.next(delta.getString("content"));
                                        }
                                    } catch (Exception ex) {

                                    }
                                }
                            }
                        }
                    }, throwable -> {
                        final String message = throwable.getMessage();
                        sink.next("服务貌似出现了一点问题,请稍后再试 错误原因: \n ```json \n" + message + " \n ```");
                    });
        });
    }

    private static Flux<String> openAISendRequest(final Object body, final String token, final String url) {
        return WebClient.builder()
                .baseUrl(url + "/chat/completions")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build()
                .post()
                .body(BodyInserters.fromValue(
                        body
                ))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            final String errorCode = parseObject(errorBody).getString("error");
                            final JSONObject jsonObject = parseObject(errorCode);
                            return Mono.error(new RuntimeException(jsonObject.toJSONString()));
                        })))
                .bodyToFlux(String.class);
    }

}
