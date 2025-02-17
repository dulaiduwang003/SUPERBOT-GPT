package com.cn.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.cn.common.msg.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

/**
 * 路由拦截器
 *
 * @author 时间海 @github dulaiduwang003
 * @version 1.0
 */
@Configuration
@SuppressWarnings("all")
public class RoutePathFilter {


    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**").addExclude("/favicon.ico")
                .setAuth(obj -> {
                    SaRouter.match("/**")
                            .notMatch(
                                    "/chat/**",
                                    "/auth/**",
                                    "/ad/**",
                                    "/disguise/**",
                                    "/chat-config/get/model/list",
                                    "/drawing-comments/get/comments/page",
                                    "/drawing-workflows/get/workflows/category/list",
                                    "/drawing-workflows/get/workflows",
                                    "/drawing-workflows/get/workflows/page",
                                    //可去除
                                    "/drawing-workflows/parsing/workflows/json",
                                    "/drawing-workflows/update/workflows"

                            )
                            .check(r -> StpUtil.checkLogin());
                })
                .setError(e -> {
                    return Result.error("登录身份信息已过期,请重新登录", 401);
                })
                .setBeforeAuth(r -> {
                    SaHolder.getResponse()
                            .setHeader("Access-Control-Allow-Origin", "*")
                            .setHeader("Access-Control-Allow-Methods", "*")
                            .setHeader("Access-Control-Max-Age", "3600")
                            .setHeader("Access-Control-Allow-Headers", "*")
                            .setServer("Zeus");
                    if (SaHolder.getRequest().getMethod().equals(HttpMethod.OPTIONS.toString())) {
                        SaRouter.back();
                    }
                });
    }
}
