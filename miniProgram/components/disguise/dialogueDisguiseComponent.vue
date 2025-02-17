<template>
  <view>
    <scroll-view scroll-y style="height: 100vh;padding-top: 100rpx">
      <view style="padding: 30rpx;padding-top: 0">
        <swiper style="height: 400rpx" circular :indicator-dots="false" :autoplay="true" :interval="2000"
                :duration="duration">
          <swiper-item>
            <view style="border-radius: 10rpx;overflow: hidden;position: relative">
              <image src="https://img2.baidu.com/it/u=845236851,3881769902&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800" style="filter: brightness(0.7);width: 100%;height: 400rpx;border-radius: 10rpx;"/>
              <view style="height: 100%;width: 100%;position: absolute;z-index: 2;left: 0;top: 0">
                <view style="padding: 30rpx;color: white">
                  <view style="font-size: 35rpx;font-weight: bold">
                    SpringBoot专题
                  </view>
                </view>
              </view>
            </view>
          </swiper-item>

        </swiper>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>

</script>

<style lang="scss">


</style>
