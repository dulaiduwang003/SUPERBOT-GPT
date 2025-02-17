package com.cn.common.controller;

import com.cn.common.msg.Result;
import com.cn.common.service.AdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;


    @PostMapping(value = "/rewarded", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result incentive() {
        adService.rewardedAds();
        return Result.ok();
    }

}
