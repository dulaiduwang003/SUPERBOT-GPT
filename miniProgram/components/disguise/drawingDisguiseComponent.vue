<template>
  <view v-if="isArticle" style="height: 100vh;padding: 100rpx 30rpx 30rpx;">
    <view style="padding: 30rpx;background-color: #0e0e0e;color: whitesmoke;border-radius: 15rpx;margin-bottom: 20rpx" @click="isArticle=false">
      SpringBoot实现Redis锁
      <view style="font-size: 24rpx;color: #555555;padding-top: 20rpx">
        在Spring Boot应用中实现Redis锁是一种常见的分布式锁机制，用来确保在分布式环境中多个实例不会同时访问同一资源。Redis提供了一个简单而有效的锁机制，
      </view>
      <view style="font-size: 23rpx;color: #555555;padding-top: 10rpx">发布于2025/01/01 12:00:12</view>
    </view>
  </view>
  <view v-if="!isArticle" style="height: 100vh;padding: 100rpx 30rpx 30rpx;">
    <view style="display: flex">
      <view @click="isArticle=true"
            style="background-color: #1E1F22;color: whitesmoke;font-size: 28rpx;border-radius: 15rpx;padding: 20rpx 35rpx">
        返回
      </view>
    </view>
    <view style="color: whitesmoke;padding-top: 30rpx">
      <mp-html :content="content" :selectable="true" :lazy-load="true" :copy-link="true"
               :markdown="true"/>
    </view>
  </view>

</template>

<script setup>
import mpHtml from '/components/mp-html/components/mp-html/mp-html'

import {ref} from 'vue'

const isArticle = ref(true)


const content = ref("### Spring Boot 实现 Redis 锁\n" +
    " \n" +
    "在分布式系统中，为了确保多个实例对共享资源的互斥访问，常常需要使用分布式锁。Redis 提供了一个简单且有效的分布式锁实现。以下是如何在 Spring Boot 应用中利用 Redis 实现分布式锁的指南。\n" +
    " \n" +
    "## 添加依赖\n" +
    " \n" +
    "首先，你需要在项目的 `pom.xml` 文件中添加 Spring Boot 和 Redis 的相关依赖。\n" +
    " \n" +
    "```xml\n" +
    "<dependencies>\n" +
    "    <!-- Spring Boot Starter Web -->\n" +
    "    <dependency>\n" +
    "        <groupId>org.springframework.boot</groupId>\n" +
    "        <artifactId>spring-boot-starter-web</artifactId>\n" +
    "    </dependency>\n" +
    "    \n" +
    "    <!-- Spring Boot Starter Data Redis -->\n" +
    "    <dependency>\n" +
    "        <groupId>org.springframework.boot</groupId>\n" +
    "        <artifactId>spring-boot-starter-data-redis</artifactId>\n" +
    "    </dependency>\n" +
    "    \n" +
    "    <!-- Jedis 连接池（可选，Lettuce 也是支持的）-->\n" +
    "    <dependency>\n" +
    "        <groupId>redis.clients</groupId>\n" +
    "        <artifactId>jedis</artifactId>\n" +
    "    </dependency>\n" +
    "</dependencies>")

</script>


<style lang="scss" scoped>

</style>
