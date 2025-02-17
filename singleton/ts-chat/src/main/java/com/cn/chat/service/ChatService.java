package com.cn.chat.service;


import com.cn.chat.dto.RequestDto;
import com.cn.chat.vo.ModelVo;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author bdth github@dulaiduwang003
 * @version 1.0
 */
public interface ChatService {


    /**
     * Gets model list.
     *
     * @return the model list
     */
    List<ModelVo> getModelList();


     Flux<String> execution(final RequestDto dto);

}
