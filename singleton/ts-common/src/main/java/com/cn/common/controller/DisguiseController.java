package com.cn.common.controller;

import com.cn.common.msg.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/disguise")
@RequiredArgsConstructor
public class DisguiseController {

    @Value("${disguise}")
    private Boolean disguise;

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result incentive() {
        return Result.data(disguise);
    }

}
